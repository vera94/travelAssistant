package rest;
import java.io.IOException;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DirectionsApiRequest.Waypoint;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.google.maps.model.TravelMode;

import model.DirectionsRequestDto;;

public class GMapsClient {
	private GeoApiContext context;
	public GMapsClient(String apiKey) {
		context = new GeoApiContext.Builder().apiKey(apiKey).build();
	}


	public DirectionsResult getDirections(DirectionsRequestDto request, Waypoint[] waypoints) throws ApiException, InterruptedException, IOException {
		DirectionsApiRequest directionsRequest = DirectionsApi.getDirections(context, request.getOrigin(), request.getDestination());
		directionsRequest.mode(TravelMode.valueOf(request.getTravelMode()));
		DirectionsResult result = directionsRequest.await();
		if (waypoints != null && waypoints.length > 0) {
			directionsRequest.optimizeWaypoints(true);
			directionsRequest.waypoints(waypoints);
		}
		return result;
	}
	
	public String findHotelNearby(LatLng place) throws ApiException, InterruptedException, IOException {
		NearbySearchRequest nearbySearchQuery = PlacesApi.nearbySearchQuery(context, place);
		nearbySearchQuery.keyword("hotel"); //bg
		nearbySearchQuery.rankby(RankBy.DISTANCE);
		PlacesSearchResponse searchResponse = nearbySearchQuery.await();
		return searchResponse.results[0].placeId;
	}
}
