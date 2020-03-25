package rest;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import model.Role;
import model.UserEntity;
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
    public ResponseEntity<Object> login(@RequestBody UserEntity user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setGrantedAuthoritiesList(Arrays.asList(Role.USER));
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
    
	@GetMapping(path="/all")
	public @ResponseBody Iterable<UserEntity> getAllUsers() {
		// This returns a JSON or XML with the users
		userRepository.fingByEmail("admins");
		return userRepository.findAll();
	}
	
	@GetMapping(path="/")
	public @ResponseBody String getGreeting() {
		// This returns a JSON or XML with the users
		return "Hey";
	}
}