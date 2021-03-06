package com.partha.userService.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.partha.userService.dto.ForgotPasswordDto;
import com.partha.userService.dto.UsernameAvailabilityDto;
import com.partha.userService.entities.GeneratedPassword;
import com.partha.userService.entities.User;
import com.partha.userService.service.UserService;

@RestController
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value="/users/{id}")
	public  ResponseEntity<User> findWithId(@PathVariable Integer id){
		logger.info("UserController.findWithId() :: start");
		return new ResponseEntity<>(userService.findWithId(id), HttpStatus.OK);	
	}
	
	
	@GetMapping(value="/users/username/{username}")
	public  ResponseEntity<User> findWithUsername(@PathVariable("username") String  username){
		   logger.info("UserController.findWithUsername() :: start");
			return new ResponseEntity<>(userService.findWithUsername(username), HttpStatus.OK);		
	}
	
	
	@PostMapping(value="/users/register")
	public ResponseEntity<User> register(@RequestBody User user){
		logger.info("UserController.register() :: start");
		User createdUser = userService.register(user);
		return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
	}
	
	
	@GetMapping(value="/users/checkUsernameAvailability")
	public ResponseEntity<UsernameAvailabilityDto> checkUsernameAvailability(@RequestParam("username") String username){
		logger.info("UserController.checkUsernameAvailability() :: start");
		UsernameAvailabilityDto response= new UsernameAvailabilityDto();
		boolean flag= userService.checkUsernameAvailability(username);
		response.setUsernameExists(flag);
		return new ResponseEntity<UsernameAvailabilityDto>(response,HttpStatus.OK);
	}
	
	
	@GetMapping(value="/users/{username}/generatePassword")
	public ResponseEntity<GeneratedPassword> generatePassword(@PathVariable("username") String  username){
		logger.info("UserController.generatePassword() :: start");
		return new ResponseEntity<GeneratedPassword>(userService.generatePassword(username),HttpStatus.OK);
	}
	
	@GetMapping(value="/users/{username}/forgotPassword")
	public ResponseEntity<ForgotPasswordDto> forgotPassword(@PathVariable("username") String  username){
		logger.info("UserController.forgotPassword() :: start");
		return new ResponseEntity<ForgotPasswordDto>(userService.forgotPassword(username),HttpStatus.OK);
	}
	
		

}
