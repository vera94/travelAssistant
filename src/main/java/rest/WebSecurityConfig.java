package rest;

import static model.EnpointConstants.REGISTRATION_URL;
import static model.EnpointConstants.SIIN_UP_URL;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	 private UserDetailsServiceImpl userDetailsService;
	    private BCryptPasswordEncoder bCryptPasswordEncoder;

	    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
	        this.userDetailsService = userDetailsService;
	        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.cors().and().csrf().disable().authorizeRequests()
	                .antMatchers(HttpMethod.POST, SIIN_UP_URL).permitAll()
	                .antMatchers(HttpMethod.POST, REGISTRATION_URL).permitAll()
	                .antMatchers(HttpMethod.PUT, "/landmark/**")
	                  .hasAuthority("ADMIN")
	                  .antMatchers(HttpMethod.POST, "/landmark/**")
	                  .hasAuthority("ADMIN")
	                  .antMatchers(HttpMethod.DELETE, "/landmark/**")
	                  .hasAuthority("ADMIN")
	                  .antMatchers(HttpMethod.PUT, "/types/**")
	                  .hasAuthority("ADMIN")
	                  .antMatchers(HttpMethod.POST, "/types/**")
	                  .hasAuthority("ADMIN")
	                  .antMatchers(HttpMethod.DELETE, "/types/**")
	                  .hasAuthority("ADMIN")
	                .anyRequest().authenticated()
	                .and()
	                .logout()
		            .logoutSuccessHandler(new CustumLogoutFilter()).and()
	                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
	                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
	                // this disables session creation on Spring Security
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    }

	    @Override
	    public void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	    }

//	  @Bean
//	  CorsConfigurationSource corsConfigurationSource() {
//	    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
//	    return source;
//	  }
	    @Bean
	    CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(Arrays.asList("*"));
	        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT", "OPTIONS"));
	        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin"));
	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        source.registerCorsConfiguration("/**", configuration);
	        return source;
	    }
}