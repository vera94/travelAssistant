package rest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import model.LandmarkType;
import model.Role;
import model.UserEntity;
import model.UserEntityDto;
import repository.IUserRepository;

@Controller    
@RequestMapping(path="/user") 
public class UserController {
	@Autowired // This means to get the bean called userRepository
	private IUserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(IUserRepository userRepository,
                          BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> login(@RequestBody UserEntity user) throws Exception {
    	if(userRepository.fingByEmail(user.getEmail()) != null) {
    		throw new Exception("Email already exists");
    	}
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setGrantedAuthoritiesList(Arrays.asList(Role.USER));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping()
    public ResponseEntity<Object> update(@RequestBody UserEntityDto userDto) throws Exception {
    	UserEntity userEntity = userRepository.fingByEmail(userDto.getEmail());
		if(userEntity == null) {
    		throw new Exception("User does not exist.");
    	}
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		String password = userDto.getPassword();
		if(password != null && !password.isEmpty()) {
			userEntity.setPassword(bCryptPasswordEncoder.encode(password));
		}
        userRepository.save(userEntity);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping(path="/prefferdTypes")
    public ResponseEntity<Object> updatePrefferedTypes(@RequestBody  List<LandmarkType> prefferedTypes, HttpServletRequest httpServletRequest) throws Exception {
    	String email = httpServletRequest.getAttribute("email").toString();
		UserEntity userEntity = userRepository.fingByEmail(email);
		userEntity.setPrefferedLandmarkTypes(prefferedTypes);
        userRepository.save(userEntity);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping(path="/role")
    public ResponseEntity<Object> updateUserRole(@RequestBody UserEntity user) throws Exception {
		UserEntity userEntity = userRepository.fingByEmail(user.getEmail());
		userEntity.setGrantedAuthoritiesList(user.getGrantedAuthoritiesList());
        userRepository.save(userEntity);
        return ResponseEntity.ok().build();
    }
    
	@GetMapping(path="/all")
	public @ResponseBody Iterable<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping()
	public @ResponseBody UserEntityDto getCurrentUserInfo(HttpServletRequest httpServletRequest) {
		String email = httpServletRequest.getAttribute("email").toString();
		UserEntity userByEmail = userRepository.fingByEmail(email);
		UserEntityDto userDto = new UserEntityDto(userByEmail);
		return userDto;
	}
}