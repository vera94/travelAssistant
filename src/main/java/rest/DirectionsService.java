package rest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.maps.DirectionsApiRequest.Waypoint;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;

import model.DirectionsRequestDto;
import model.Landmark;
import repository.ILandmarkRepository;;

public class DirectionsService {
	@Autowired 
	private ILandmarkRepository landmarkRepository;
	
	public DirectionsResult getDirections(DirectionsRequestDto request) throws ApiException, InterruptedException, IOException {
		GMapsClient client = new GMapsClient("");
		DirectionsResult directions = client.getDirections(request, null);
		Iterable<Landmark> allLandmarks = landmarkRepository.findAll();
		EncodedPolyline path = directions.routes[0].overviewPolyline;
		List<Waypoint> waypoints = extractLandmarkWaypoints(request, allLandmarks, path);
		DirectionsResult directionsResultWithoutHotels = client.getDirections(request, (Waypoint[]) waypoints.toArray());
		List<Waypoint> hotelWaypoints = findHotels(client, directionsResultWithoutHotels, request.getHotelStays());
		waypoints.addAll(hotelWaypoints);
		DirectionsResult directionsResultWithHotels = client.getDirections(request, (Waypoint[]) waypoints.toArray());
		return directionsResultWithHotels;
		
	}

	private List<Waypoint> findHotels(GMapsClient client, DirectionsResult directionsResult, int hotelStays) throws ApiException, InterruptedException, IOException {
		List<Waypoint> hotels = new ArrayList<>();
		DirectionsLeg[] directionsLegs = directionsResult.routes[0].legs;
		long length = 0;
//		long duration = 0;
		for (DirectionsLeg directionsLeg : directionsLegs) {
			length += directionsLeg.distance.inMeters;
//			duration += directionsLeg.duration.inSeconds / 60; //minutes
		}
		long pathForStay = length/hotelStays;
		int distanceTmp= 0;
		for (DirectionsLeg directionsLeg : directionsLegs) {
			if((distanceTmp + directionsLeg.distance.inMeters) < pathForStay) {
				distanceTmp += directionsLeg.distance.inMeters;
				continue;
			} 
			
			for(DirectionsStep step : directionsLeg.steps) {
				if ((distanceTmp + step.distance.inMeters) < pathForStay) {
					distanceTmp += directionsLeg.distance.inMeters;
					continue;
				} else {
					LatLng startLocation = step.startLocation;
					//findnearestHotel on this location; add to waypoints
					String hotelPlaceId = client.findHotelNearby(startLocation);
					Waypoint waypoint = new Waypoint(hotelPlaceId);
					hotels.add(waypoint);
					pathForStay += length/hotelStays;
					distanceTmp += directionsLeg.distance.inMeters;
				}
			}
		}
		return hotels;
	}

	private List<Waypoint> extractLandmarkWaypoints(DirectionsRequestDto request, Iterable<Landmark> allLandmarks, EncodedPolyline path) {
		List<Waypoint> waypoints = new ArrayList<>();
		List<Landmark> nearByLandmarks = new ArrayList<>();
		List<LatLng> decodePath = path.decodePath();
		for (LatLng latLng : decodePath) {
			Iterator<Landmark> iterator = allLandmarks.iterator();
			while (iterator.hasNext()) {
				Landmark landmark = iterator.next();
				double distance = distance(landmark.getLat(), landmark.getLng(), latLng.lat, latLng.lng);
				if(distance <= request.getMaxDeviationFromPath()) {
					nearByLandmarks.add(landmark);
					iterator.remove();
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
			waypoints.add(waypoint);
			waypointsCount--;
		}
		
		return waypoints;
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
}
