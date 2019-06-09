package rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRest {
	@CrossOrigin
    @RequestMapping(value="/",produces = {"text/plain"})
    public String index(HttpServletRequest request, HttpServletResponse response) {
//		if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//		    response.setStatus(HttpServletResponse.SC_OK);
//		}
        return "Greetings from Spring Boot!";
    }

}
