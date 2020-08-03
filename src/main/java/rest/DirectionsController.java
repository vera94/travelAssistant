package rest;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import model.DirectionsRequestDto;

@Controller    
@RequestMapping(path="/directions") 
public class DirectionsController {
	@Autowired
	ServletContext context; 
	@Autowired 
	private DirectionsService directionsService;
	
	@CrossOrigin(origins = "*")
	@PostMapping()
	public @ResponseBody DirectionsRequestDto getWaypoints(@RequestBody DirectionsRequestDto request, HttpServletRequest httpServletRequest) {
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
	
}
