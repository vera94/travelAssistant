package rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import model.Landmark;
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
		}
		 return landmarks;
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
	public void addNew(@RequestPart("landmark") Landmark landmark,
	        @RequestPart(required = false,value = "photo") MultipartFile photo) {
		if(photo != null) {
			String destination = context.getRealPath("");
		    File file = new File(destination, photo.getOriginalFilename());
		    try {
				photo.transferTo(file);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		    landmark.setPhotoUrl(file.getPath());
		}
		landmark = landmarkRepository.save(landmark);
	}
	
	@PutMapping()
	public void editLandmark(@RequestPart("landmark") Landmark landmark,
	        @RequestPart(required = false,value = "photo") MultipartFile photo) {
		if (!landmarkRepository.existsById(landmark.getId())) {
			throw new EntityNotFoundException("Could not find object with id :" + landmark.getId());
		}
		if(photo != null) {
			String destination = context.getRealPath("");
		    File file = new File(destination, photo.getOriginalFilename());
		    try {
				photo.transferTo(file);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		    landmark.setPhotoUrl(file.getPath()); 
		}
		landmark = landmarkRepository.save(landmark);
	}
	@CrossOrigin(origins = "*")
	@DeleteMapping(path="/delete/{landmarkId}")
	public void deleteLandmark(@PathVariable("landmarkId") long landmarkId ) {
		if (!landmarkRepository.existsById(landmarkId)) {
			throw new EntityNotFoundException("Could not find object with id :" + landmarkId);
		}
		landmarkRepository.deleteById(landmarkId);
	}
	
	@GetMapping(path="/type/{type}")
	public @ResponseBody Iterable<Landmark> getLandmarksByType(@PathVariable("type") String type) {
		// This returns a JSON or XML with the users
		return landmarkRepository.getAllLandmarksByType(type);
	}
	
}
