package rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping("/sign-up")
    public void signUp(@RequestBody UserEntity user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    
	@PostMapping(path="/add") // Map ONLY GET Requests
	public @ResponseBody String addNewUser () {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request

		UserEntity n = new UserEntity();
		n.setFirstName("vera");
		n.setEmail("email");
		userRepository.save(n);
		return "Saved";
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