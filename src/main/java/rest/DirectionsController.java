package rest;

import java.io.IOException;

import javax.servlet.ServletContext;

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
	
	@CrossOrigin(origins = "*")
	@PostMapping()
	public @ResponseBody DirectionsResult getDirections(@RequestBody DirectionsRequestDto request) {
		//set email;
		try {
			return new DirectionsService().getDirections(request);
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
