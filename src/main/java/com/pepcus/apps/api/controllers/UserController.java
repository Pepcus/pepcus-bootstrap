package com.pepcus.apps.api.controllers;

import static com.pepcus.apps.api.constant.ApplicationConstants.BROKER_ID_PARAM;
import static com.pepcus.apps.api.constant.ApplicationConstants.SUPPRESS_EMAIL_PARAM;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pepcus.apps.api.db.entities.User;
import com.pepcus.apps.api.exception.ApplicationException;
import com.pepcus.apps.api.exception.MessageResourceHandler;
import com.pepcus.apps.api.services.UserService;

/**
 * User Controller for performing operations
 * related with User object.
 * 
 */
@RestController
@Validated
@RequestMapping(path="/v1/users")
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    
    @Autowired
    MessageResourceHandler resourceHandler;

    /**
     * Get all users from repository
     * @return List<User>
     * 
     */
    @PreAuthorize("hasPermission('users', 'list')")
    @RequestMapping(method=RequestMethod.GET)
    List<User> getAllUser(@Range(min = 0l, message = "Please select positive integer value for 'offset'") 
                @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                @Range(min = 1l, message = "Please select positive integer and should be greater than 0 for 'limit'")
                @RequestParam(value = "limit", required = false, defaultValue = "50") Integer limit, 
                @RequestParam(value = "sort", required = false) String sort,
                @RequestParam(value = "searchSpec", required = false) String searchSpec, 
                @RequestParam(value = "fields", required = false) String fields,
                @RequestParam Map<String, String> allRequestParams) throws ApplicationException {

        return userService.getAllUser(offset, limit, sort, searchSpec, fields, allRequestParams);
    }

    /**
     * Get user with given id from repository
     * @param id user id
     * @return User object
     * @throws ApplicationException 
     * 
     */
    @PreAuthorize("hasPermission(#userId, 'users', 'view')")
    @RequestMapping(method=RequestMethod.GET, value="/{userId}")
    public User getById(@PathVariable(name="userId", value = "userId") Integer userId,
            @RequestParam(value = "fields", required = false) String fields) throws ApplicationException { 
        return userService.getUser(userId, fields);
    }


    /**
     * Update a user in database
     * 
     * @param User object
     */
    @PreAuthorize("hasPermission(#userId, 'users', 'update')")
    @RequestMapping(method=RequestMethod.PUT, value="/{userId}")
    public ResponseEntity <User> updateUser(@PathVariable(name="userId", value = "userId") Integer userId, 
            @RequestBody String userJson, @RequestAttribute(name = BROKER_ID_PARAM) Integer brokerId)
            throws ApplicationException , IOException {
        
        User updatedUser = userService.updateUser(userId, userJson , brokerId);
        
        return new ResponseEntity<User> (updatedUser, HttpStatus.OK);
    }


    /**
     * Add a user in database
     * 
     * @param User object
     */
    @PreAuthorize("hasPermission(#user.companyName, 'users', 'create')")
    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity<User> addUser(@Valid @RequestBody User user,
            @RequestParam(name = BROKER_ID_PARAM, required = false) Integer opBrokerId,
            @RequestAttribute(name = BROKER_ID_PARAM) Integer brokerId,
            @RequestParam(name = SUPPRESS_EMAIL_PARAM, required = false) Boolean opSuppressEmail) throws ApplicationException {

        if (opBrokerId == null) {
            opBrokerId = brokerId;
        }

        userService.addUser(user, opBrokerId, opSuppressEmail);
        return new ResponseEntity<User>(user, HttpStatus.CREATED);
    }
    
}

