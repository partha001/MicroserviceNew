package com.partha.gatewayService.config;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;



@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("partha").password(encoder().encode("partha")).roles("ADMIN").build());
		//manager.createUser(User.withUsername("test").password(encoder().encode("test")).roles("USER").build());
		return manager;
	}

	//	@Bean
	//	public PasswordEncoder encoder() {
	//		return new BCryptPasswordEncoder();
	//	}


	@Bean
	public NoOpPasswordEncoder encoder() {
		return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}

	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		//adding cors by default it looks for a bean with name corsConfigurationSource
		.cors().and()
		.authorizeRequests()
		.antMatchers("/login",
				"/test1",
				"/partners",
				"/authenticationFailed",
				"/entrypoint",
				"/api/userService/users/register",
				"/api/userService/users/checkUsernameAvailability").permitAll()
		.antMatchers("/home").hasRole("ADMIN")
		.anyRequest()
		.authenticated()				
		.and()
		.formLogin()	
		.loginPage("/login").defaultSuccessUrl("/home",true)
		//will be invoked if authencation fails
		.failureHandler(failureHandler())
		//will be invoked if there are other exceptions like resource not found,etc
		//else without this user will be redirected to login page by default configuration
		.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
		//disabling csrf
		.and().csrf().disable();						
	}
	

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();      
        //config.addAllowedOrigin("*");
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200",""));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        //configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


	private AuthenticationFailureHandler failureHandler(){
		return new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException exception) throws IOException, ServletException {
				System.out.println("inside failure handler");
				System.out.println("requested uri :"+request.getRequestURI());
				response.sendRedirect("/authenticationFailed");
			}
		};
	}
	
	
	private AuthenticationEntryPoint authenticationEntryPoint(){
		return new AuthenticationEntryPoint(){
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				System.out.println("inside authenticationEntryPoint");
				System.out.println("requested uri :"+request.getRequestURI());
				response.sendRedirect("/entrypoint");				
			}
		};		
	}
	
}
