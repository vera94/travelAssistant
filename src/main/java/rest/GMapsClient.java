package rest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DirectionsApiRequest.Waypoint;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.google.maps.model.TravelMode;

import model.DirectionsRequest;;

public class GMapsClient {
	private GeoApiContext context;
	public GMapsClient(String apiKey) {
		context = new GeoApiContext.Builder().apiKey(apiKey).build();
	}


	public DirectionsResult getDirections(DirectionsRequest request, Waypoint[] waypoints) throws ApiException, InterruptedException, IOException {
		DirectionsApiRequest directionsRequest = DirectionsApi.getDirections(context, "place_id:" + request.getOrigin(), "place_id:" +request.getDestination());
		directionsRequest.mode(TravelMode.valueOf(request.getTravelMode()));
		if (waypoints != null && waypoints.length > 0) {
			directionsRequest.optimizeWaypoints(true);
			directionsRequest.waypoints(waypoints);
		}
		DirectionsResult result = directionsRequest.await();
		return result;
	}
	
	public LatLng findHotelNearby(LatLng place) throws ApiException, InterruptedException, IOException {
		NearbySearchRequest nearbySearchQuery = PlacesApi.nearbySearchQuery(context, place);
		nearbySearchQuery.keyword("hotel"); //bg
		nearbySearchQuery.rankby(RankBy.DISTANCE);
		PlacesSearchResponse searchResponse = nearbySearchQuery.await();
		return searchResponse.results[0].geometry.location;
	}
	
	public List<PlacesSearchResult> findPlaces(PlaceType placeType, LatLng place, int distance) throws ApiException, InterruptedException, IOException {
		NearbySearchRequest nearbySearchQuery = PlacesApi.nearbySearchQuery(context, new LatLng(42.698334, 23.319941));
		nearbySearchQuery.type(placeType);
		if (distance > 0) {
			nearbySearchQuery.radius(distance);
		} else {
			nearbySearchQuery.rankby(RankBy.DISTANCE);
		}
		List<PlacesSearchResult> places = new ArrayList<>();
		PlacesSearchResponse searchResponse = null;
		searchResponse = nearbySearchQuery.await();
		PlacesSearchResult[] resultPlaces = searchResponse.results;
		places.addAll(Arrays.asList(resultPlaces));
		while (searchResponse.nextPageToken != null && !searchResponse.nextPageToken.isEmpty()) {
			Thread.sleep(2000);
			NearbySearchRequest nearbySearchNextPage = PlacesApi.nearbySearchNextPage(context, searchResponse.nextPageToken);
			searchResponse = nearbySearchNextPage.await();
			PlacesSearchResult[] resultPlaces2 = searchResponse.results;
			places.addAll(Arrays.asList(resultPlaces2));
			
		} 
		
		return places;
	}
}
