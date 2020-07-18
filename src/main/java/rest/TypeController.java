package rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.maps.model.PlaceType;

import model.LandmarkType;
import model.UserEntity;
import repository.ILandmarkTypeRepository;
import repository.IUserRepository;

@Controller    
@RequestMapping(path="/types") 
public class TypeController {
	@Autowired
	ServletContext context; 
	@Autowired 
	private ILandmarkTypeRepository landmarkTypeRepository;
	@Autowired // This means to get the bean called userRepository
	private IUserRepository userRepository;
	
	
	
	@GetMapping(path="/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<LandmarkType> getLandmarkTypes() throws IOException {
		List<LandmarkType> allTypes = (List<LandmarkType>) landmarkTypeRepository.getAllTypes();
		List<LandmarkType> result = new ArrayList<>();
		prepareResponseFroUI(allTypes.get(0), allTypes);
		result.add(allTypes.get(0));
		return result;
	}
	
	@GetMapping(path="/gmap", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<PlaceType> getGoogleMapsTypes() throws IOException {
		DirectionsService directionService = new DirectionsService();
		return directionService.getGmapTypes();
	}
	
	private void prepareResponseFroUI(LandmarkType currentType, List<LandmarkType> allTypes) {
		List<LandmarkType> children = new ArrayList<>();
		String currentPath = currentType.getPath();
		for (int j = 0; j < allTypes.size(); j++) {
			String jPath = allTypes.get(j).getPath();
			if (jPath.startsWith(currentPath) && jPath.split("/").length == (currentPath.split("/").length + 1)) {
				children.add(allTypes.get(j));
			}
		}
		currentType.setChildren(children);
		if(!children.isEmpty()) {
			for (LandmarkType landmarkType : children) {
				prepareResponseFroUI(landmarkType, allTypes);
			}
		}
	}

	@GetMapping(path="/userTypes", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Iterable<LandmarkType> getUserLandmarkTypes(HttpServletRequest httpServletRequest) throws IOException {
		String email = httpServletRequest.getAttribute("email").toString();
		UserEntity userByEmail = userRepository.fingByEmail(email);
		Collection<LandmarkType> prefferedLandmarkTypes = userByEmail.getPrefferedLandmarkTypes();
		Set<LandmarkType> result = new HashSet<>();
		for (LandmarkType landmarkType : prefferedLandmarkTypes) {
			List<LandmarkType> allTypeDescendants = landmarkTypeRepository.getAllTypeDescendants(landmarkType.getPath());
			result.addAll(allTypeDescendants);
		}
		return result;
	}

	@CrossOrigin(origins = "*")
	@PostMapping()
	public @ResponseBody LandmarkType addNew(@RequestBody LandmarkType landmarkType) throws Exception {
		if (landmarkType.getType() == null || landmarkType.getType().isEmpty()) {
			throw new Exception("Invalid landmark type!");
		}
		LandmarkType savedType = landmarkTypeRepository.save(landmarkType);
		savedType.setPath(landmarkType.getParentPath() + savedType.getId() + "/");
		landmarkTypeRepository.save(savedType);
		return savedType;
	}
	
	@PutMapping()
	public @ResponseBody LandmarkType editLandmark(@RequestBody LandmarkType landmarkType) throws Exception {
		if (landmarkType.getType() == null || landmarkType.getType().isEmpty()) {
			throw new Exception("Invalid landmark type!");
		}
		if (!landmarkTypeRepository.existsById(landmarkType.getId())) {
			throw new EntityNotFoundException("Could not find object with id :" + landmarkType.getId());
		}
		return landmarkTypeRepository.save(landmarkType);
	}
	
	@CrossOrigin(origins = "*")
	@DeleteMapping(path="/delete/{landmarkTypeId}")
	public ResponseEntity<LandmarkType> deleteLandmark(@PathVariable("landmarkTypeId") long landmarkTypeId ) {
		if (!landmarkTypeRepository.existsById(landmarkTypeId)) {
			throw new EntityNotFoundException("Could not find object with id :" + landmarkTypeId);
		}
		Optional<LandmarkType> findById = landmarkTypeRepository.findById(landmarkTypeId);
		List<LandmarkType> allTypeDescendants = landmarkTypeRepository.getAllTypeDescendants(findById.get().getPath()+"%");
		for (LandmarkType landmarkType : allTypeDescendants) {
			landmarkTypeRepository.deleteById(landmarkType.getId());
		}
		return new ResponseEntity<LandmarkType>(HttpStatus.OK);
	}
	
}
