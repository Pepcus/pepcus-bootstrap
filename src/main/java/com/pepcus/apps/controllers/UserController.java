package com.pepcus.apps.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.pepcus.apps.exception.MessageResourceHandler;
import com.pepcus.apps.services.UserService;

/**
 * User Controller for performing operations related with User object.
 * 
 */
@RestController
@Validated
@RequestMapping(path = "/v1/users")
public class UserController {
	  
	  private static Logger logger = LoggerFactory.getLogger(UserController.class);
	  
	  @Autowired 
	  UserService userService;
	  
	  @Autowired 
	  MessageResourceHandler resourceHandler;
	  
}
