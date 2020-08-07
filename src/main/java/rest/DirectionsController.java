package rest;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.maps.errors.ApiException;

import model.DirectionsRequest;

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
			return directionsService.getDirections(request);
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	
	
}
