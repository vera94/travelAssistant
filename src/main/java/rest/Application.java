package rest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@EnableJpaRepositories(basePackages = {"repository", "rest"})
@EntityScan( basePackages = {"model"} )
@ComponentScan(basePackages = {"rest", "repository", "model"})
@SpringBootApplication()
public class Application extends SpringBootServletInitializer {
  public static void main(String[] args) {
      SpringApplication.run(Application.class, args);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
      return new BCryptPasswordEncoder();
  }

  @Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:4200", "https://travel-assistat-1558797246660.ey.r.appspot.com/" );
			}
		};
	}
  
  @Bean
  public MultipartResolver multipartResolver() {
      return new StandardServletMultipartResolver();
  }
  
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(Application.class);
  }
  
  @Bean
  public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
      ObjectMapper mapper = new ObjectMapper();
      mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      MappingJackson2HttpMessageConverter converter = 
          new MappingJackson2HttpMessageConverter(mapper);
      return converter;
  }
}