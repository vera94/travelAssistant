package rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import model.KeyValuePair;
import model.Landmark;
import model.LandmarkType;
import repository.ILandmarkRepository;

@Controller    
@RequestMapping(path="/landmark") 
public class LandmarkController {
	@Autowired
	ServletContext context; 
	@Autowired 
	private ILandmarkRepository landmarkRepository;
	
	@GetMapping(path="/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<Landmark> getAllLandmarks() throws IOException {
		 Iterable<Landmark> landmarks = landmarkRepository.findAll();
		 for (Landmark landmark : landmarks) {
			 if(landmark.getPhotoUrl() != null && !landmark.getPhotoUrl().isEmpty()) {
				 File photo = new File(landmark.getPhotoUrl());
				 landmark.setPhoto(Files.readAllBytes(photo.toPath()));
			 }
			 if(landmark.getType() != null) {
				 landmark.setLandmarkTypeName(landmark.getType().getName());
			 }
		}
		 return landmarks;
	}
	
	@GetMapping(path="/types", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<KeyValuePair<LandmarkType, String>> getLandmarkTypes() throws IOException {
		 return LandmarkType.getTypesAsKeyValuePairs();
	}
	
	
	@GetMapping(path="/landmark/{landmarkId}")
	public @ResponseBody Optional<Landmark> getLandmark(@PathVariable("landmarkId") long landmarkId) {
		if (!landmarkRepository.existsById(landmarkId)) {
			throw new EntityNotFoundException("Could not find object with id :" + landmarkId);
		}
		return landmarkRepository.findById(landmarkId);
	}
	@CrossOrigin(origins = "*")
	@PostMapping()
	public @ResponseBody Landmark addNew(@RequestPart("landmark") Landmark landmark,
	        @RequestPart(required = false,value = "photo") MultipartFile photo) {
		if(photo != null) {
		    File file = new File("C:\\AngularPics", photo.getOriginalFilename());
		    try {
				photo.transferTo(file);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		    landmark.setPhotoUrl(file.getPath());
		}
		return landmark = landmarkRepository.save(landmark);
	}
	
	@PutMapping()
	public @ResponseBody Landmark editLandmark(@RequestPart("landmark") Landmark landmark,
	        @RequestPart(required = false,value = "photo") MultipartFile photo) {
		if (!landmarkRepository.existsById(landmark.getId())) {
			throw new EntityNotFoundException("Could not find object with id :" + landmark.getId());
		}
		if(photo != null) {
		    File file = new File("C:\\AngularPics", photo.getOriginalFilename());
		    try {
				photo.transferTo(file);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		    landmark.setPhotoUrl(file.getPath()); 
		}
		return landmark = landmarkRepository.save(landmark);
	}
	
	@CrossOrigin(origins = "*")
	@DeleteMapping(path="/delete/{landmarkId}")
	public ResponseEntity<Landmark> deleteLandmark(@PathVariable("landmarkId") long landmarkId ) {
		if (!landmarkRepository.existsById(landmarkId)) {
			throw new EntityNotFoundException("Could not find object with id :" + landmarkId);
		}
		landmarkRepository.deleteById(landmarkId);
		return new ResponseEntity<Landmark>(HttpStatus.OK);
	}
	
	@GetMapping(path="/type/{type}")
	public @ResponseBody Iterable<Landmark> getLandmarksByType(@PathVariable("type") String type) {
		// This returns a JSON or XML with the users
		return landmarkRepository.getAllLandmarksByType(type);
	}
	
}
