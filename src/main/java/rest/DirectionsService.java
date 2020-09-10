package rest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.DirectionsApiRequest.Waypoint;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResult;

import model.DirectionsRequest;
import model.Landmark;
import model.LandmarkType;
import model.UserEntity;
import repository.IDirectionsRequestRepository;
import repository.ILandmarkRepository;
import repository.IUserRepository;;
@Service
public class DirectionsService {
	@Autowired 
	private ILandmarkRepository landmarkRepository;
	
	@Autowired 
	private IUserRepository userRepository;
	
	@Autowired 
	private IDirectionsRequestRepository requestRepository;
	
	@Value("${gmapKey}")
	private String apiKey;
	
	public List<PlaceType> getGmapTypes(){
		return Arrays.asList(PlaceType.values());
	}
	
	public DirectionsRequest getDirections(DirectionsRequest request) throws ApiException, InterruptedException, IOException {
		GMapsClient client = new GMapsClient(apiKey);
		DirectionsResult directions = client.getDirections(request, null);
		UserEntity userEntity = userRepository.fingByEmail(request.getEmail());
		Collection<String> prefferedLandmarkTypesAsStrings = userEntity.getPrefferedLandmarkTypesAsStrings();
		Iterable<Landmark> allLandmarks = new ArrayList<>();
		if (prefferedLandmarkTypesAsStrings.isEmpty()) {
			allLandmarks = landmarkRepository.findAll();
		} else {
			allLandmarks = landmarkRepository.getAllLandmarksByType(prefferedLandmarkTypesAsStrings);
		}
		EncodedPolyline path = directions.routes[0].overviewPolyline;
		List<Float[]> wayptsResult = new ArrayList<>();
		List<Waypoint> waypoints = extractLandmarkWaypoints(request, allLandmarks, path, wayptsResult, client, userEntity.getPrefferedLandmarkTypes());
		Waypoint[] array = new Waypoint[waypoints.size()];
		array = waypoints.toArray(array);
		DirectionsResult directionsResultWithoutHotels = client.getDirections(request, array);
		List<Float[]> hotelWaypoints = findHotels(client, directionsResultWithoutHotels, request.getHotelStays());
		wayptsResult.addAll(hotelWaypoints);
		request.setWaypoints(wayptsResult );
		return request;
	}

	public List<Landmark> findAllPlaces(Collection<LandmarkType> collection) {
		List<Landmark> results = new ArrayList<>();
		GMapsClient client = new GMapsClient(apiKey);
		for (LandmarkType landmarkType : collection) {
			String gmapPlaceTypeString = landmarkType.getGmapMapping();
			if (gmapPlaceTypeString != null && !gmapPlaceTypeString.isEmpty()) {
				try {
					PlaceType placeType = PlaceType.valueOf(landmarkType.getGmapMapping());
					List<PlacesSearchResult> findPlaces = client.findPlaces(placeType, null, 0);
					for (PlacesSearchResult placesSearchResult : findPlaces) {
						Landmark landmark = new Landmark();
						LatLng latLng = placesSearchResult.geometry.location;
						landmark.setLat((float) latLng.lat);
						landmark.setLng((float) latLng.lng);
						landmark.setName(placesSearchResult.name);
						landmark.setRating(placesSearchResult.rating);
						results.add(landmark);
					}
				} catch (ApiException | InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return results;
	}
	
	private List<Float[]> findHotels(GMapsClient client, DirectionsResult directionsResult, int hotelStays) throws ApiException, InterruptedException, IOException {
		List<Float[]> hotels = new ArrayList<>();
		if (hotelStays > 0) {
			DirectionsLeg[] directionsLegs = directionsResult.routes[0].legs;
			long length = 0;
			for (DirectionsLeg directionsLeg : directionsLegs) {
				length += directionsLeg.distance.inMeters;
			}
			long pathForStay = length / hotelStays;
			int distanceTmp = 0;
			for (DirectionsLeg directionsLeg : directionsLegs) {
				if ((distanceTmp + directionsLeg.distance.inMeters) < pathForStay) {
					distanceTmp += directionsLeg.distance.inMeters;
					continue;
				}

				for (DirectionsStep step : directionsLeg.steps) {
					if ((distanceTmp + step.distance.inMeters) < pathForStay) {
						System.out.println("distanceTmp : " + distanceTmp +"  stemeters : " + step.distance.inMeters);
						distanceTmp += step.distance.inMeters;
						continue;
					} else {
						LatLng startLocation = step.startLocation;
						// findnearestHotel on this location; add to waypoints
						LatLng hotelPlaceId = client.findHotelNearby(startLocation);
						Float[] e = {(float) hotelPlaceId.lat, (float) hotelPlaceId.lng};
						hotels.add(e);
						if(hotels.size() == hotelStays) {
							return hotels;
						}
						pathForStay += length / hotelStays;
						distanceTmp += step.distance.inMeters;
					}
				}
			}
		}
		return hotels;
	}

	private List<Waypoint> extractLandmarkWaypoints(DirectionsRequest request, Iterable<Landmark> allLandmarks, 
			EncodedPolyline path, List<Float[]> wayptsResult, GMapsClient client, Collection<LandmarkType> preferedTypes) {
		List<Waypoint> waypoints = new ArrayList<>();
		List<Landmark> nearByLandmarks = new ArrayList<>();
		List<LatLng> decodePath = path.decodePath();
		List<String> gmapPrefferedTypes = extractGmapPrefferedTypes(preferedTypes);
		LatLng lastFoundPlace = null;
		for (LatLng latLng : decodePath) {
			if(lastFoundPlace == null) {
				lastFoundPlace = latLng;
			}
			Iterator<Landmark> iterator = allLandmarks.iterator();
			while (iterator.hasNext()) {
				Landmark landmark = iterator.next();
				double distance = distance(landmark.getLat(), landmark.getLng(), latLng.lat, latLng.lng);
				if(distance <= request.getMaxDeviationFromPath()) {
					nearByLandmarks.add(landmark);
					iterator.remove();
				}
			}
			double distance = distance(lastFoundPlace.lat, lastFoundPlace.lng, latLng.lat, latLng.lng);
			if(distance > request.getMaxDeviationFromPath()) {
				lastFoundPlace = latLng;
				for (String gmapType : gmapPrefferedTypes) {
					try {
						lastFoundPlace = latLng;
						List<PlacesSearchResult> findPlaces = client.findPlaces(PlaceType.valueOf(gmapType), latLng, request.getMaxDeviationFromPath()*1000);
						for (PlacesSearchResult place : findPlaces) {
							Landmark landmark = new Landmark();
							LatLng placeLatLng = place.geometry.location;
							landmark.setLat((float) placeLatLng.lat);
							landmark.setLng((float) placeLatLng.lng);
							landmark.setName(place.name);
							landmark.setRating(place.rating);
							if(!nearByLandmarks.contains(landmark)) {
								nearByLandmarks.add(landmark);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
			}
		}
		int waypointsCount = nearByLandmarks.size();
		if (request.getStopovers() < nearByLandmarks.size()) {
			Collections.sort(nearByLandmarks);
			waypointsCount = request.getStopovers();
		}
		for (Landmark landmark : nearByLandmarks) {
			if(waypointsCount == 0) {
				break;
			}
			Waypoint waypoint = new Waypoint(new LatLng(landmark.getLat(), landmark.getLng()));
			Float[] e = {landmark.getLat(), landmark.getLng()};
			wayptsResult.add(e);
			waypoints.add(waypoint);
			waypointsCount--;
		}
		
		return waypoints;
	}

	private List<String> extractGmapPrefferedTypes(Collection<LandmarkType> preferedTypes) {
		List<String> gmapTypes = new ArrayList<>();
		for (LandmarkType landmarkType : preferedTypes) {
			String gmapMapping = landmarkType.getGmapMapping();
			if(gmapMapping != null && !gmapMapping.isEmpty()) {
				gmapTypes.add(gmapMapping);
			}
		}
		return gmapTypes;
	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(lon1 - lon2));
		dist = rad2deg(Math.acos(dist));
		dist = dist * 60 * 1.1515;
		dist = dist * 1.609344;
		return (dist);
	}

	private double deg2rad(double deg) {
		  return (deg * Math.PI / 180.0);
		}
	
	private double rad2deg(double rad) {
		  return (rad * 180.0 / Math.PI);
		}

	public void saveRequest(DirectionsRequest request, String userEmail) {
		UserEntity userEntity = userRepository.fingByEmail(userEmail);
		request.setId(null);
		userEntity.getSavedRequests().add(request);
		requestRepository.save(request);
		userRepository.save(userEntity);
	}
	
	public Collection<DirectionsRequest> getRequests(String userEmail) {
		UserEntity userEntity = userRepository.fingByEmail(userEmail);
		return userEntity.getSavedRequests();
	}

	public void delete(long requestId, String userEmail) {
		UserEntity userEntity = userRepository.fingByEmail(userEmail);
		DirectionsRequest request = requestRepository.findById(requestId).get();
		if (request == null) {
			throw new EntityNotFoundException("Could not find object with id :" + requestId);
		}
		userEntity.getSavedRequests().remove(request);
		userRepository.save(userEntity);
		requestRepository.delete(request);
	}

}
