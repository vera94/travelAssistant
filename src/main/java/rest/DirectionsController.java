package rest;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.maps.errors.ApiException;

import model.DirectionsRequest;
import model.Landmark;

@Controller    
@RequestMapping(path="/directions") 
public class DirectionsController {
	@Autowired
	ServletContext context; 
	@Autowired 
	private DirectionsService directionsService;

	
	@CrossOrigin(origins = "*")
	@PostMapping()
	public @ResponseBody DirectionsRequest getWaypoints(@RequestBody DirectionsRequest request, HttpServletRequest httpServletRequest) {
			String email = httpServletRequest.getAttribute("email").toString();
			request.setEmail(email);
		try {
			validateRequest(request);
			return directionsService.getDirections(request);
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void validateRequest(DirectionsRequest request) {
		if(request.getDestination() == null || request.getDestination().isEmpty()) {
			throw new IllegalArgumentException("Destination can not be empty");
		}
		if(request.getOrigin() == null || request.getOrigin().isEmpty()) {
			throw new IllegalArgumentException("Origin can not be empty");
		}
		if(request.getHotelStays() < 0) {
			throw new IllegalArgumentException("Hotel stays property must be a positive number");
		}
	}

	@CrossOrigin(origins = "*")
	@PostMapping(path="/request")
	public ResponseEntity<Object>  saveRequest(@RequestBody DirectionsRequest request, HttpServletRequest httpServletRequest) {
			String email = httpServletRequest.getAttribute("email").toString();
		try {
			directionsService.saveRequest(request, email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().build();
	}
	
	@CrossOrigin(origins = "*")
	@GetMapping(path="/requests")
	public @ResponseBody Collection<DirectionsRequest>  getRequest(HttpServletRequest httpServletRequest) {
			String email = httpServletRequest.getAttribute("email").toString();
		try {
			return directionsService.getRequests(email);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@CrossOrigin(origins = "*")
	@DeleteMapping(path = "/request/{requestId}")
	public ResponseEntity<Object> deleteRequest(@PathVariable("requestId") long requestId, HttpServletRequest httpServletRequest) {
		String email = httpServletRequest.getAttribute("email").toString();
		directionsService.delete(requestId, email);
		return ResponseEntity.ok().build();
	}
}
