package rest;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import model.Landmark;
import repository.ILandmarkRepository;

@Controller    
@RequestMapping(path="/landmark") 
public class LandmarkController {
	@Autowired 
	private ILandmarkRepository landmarkRepository;
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Landmark> getAllLandmarks() {
		return landmarkRepository.findAll();
	}
	
	@GetMapping(path="/landmark/{landmarkId}")
	public @ResponseBody Optional<Landmark> getLandmark(@PathParam("landmarkId") long landmarkId) {
		if (!landmarkRepository.existsById(landmarkId)) {
			throw new EntityNotFoundException("Could not find object with id :" + landmarkId);
		}
		return landmarkRepository.findById(landmarkId);
	}
	
	@PostMapping()
	public @ResponseBody Landmark addNew(@RequestBody Landmark landmark) {
		return landmarkRepository.save(landmark);
	}
	
	@PutMapping()
	public @ResponseBody Landmark editLandmark(@RequestBody Landmark landmark) {
		if (!landmarkRepository.existsById(landmark.getId())) {
			throw new EntityNotFoundException("Could not find object with id :" + landmark.getId());
		}
		return landmarkRepository.save(landmark);
	}
	
	@DeleteMapping(path="/delete/{landmarkId}")
	public void deleteLandmark(@PathParam("landmarkId") long landmarkId ) {
		if (!landmarkRepository.existsById(landmarkId)) {
			throw new EntityNotFoundException("Could not find object with id :" + landmarkId);
		}
		landmarkRepository.deleteById(landmarkId);
	}
	
	@GetMapping(path="/type/{type}")
	public @ResponseBody Iterable<Landmark> getLandmarksByType(@PathParam("type") String type) {
		// This returns a JSON or XML with the users
		return landmarkRepository.getAllLandmarksByType(type);
	}
	
}
