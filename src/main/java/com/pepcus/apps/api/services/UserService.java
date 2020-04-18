package com.pepcus.apps.api.services;

import static com.pepcus.apps.api.constant.ApplicationConstants.DEFAULT_SORT_BY_USER_NAME;
import static com.pepcus.apps.api.constant.ApplicationConstants.SPACE;
import static com.pepcus.apps.api.constant.ApplicationConstants.UNDERSCORE;
import static com.pepcus.apps.api.constant.ApplicationConstants.USER;
import static com.pepcus.apps.api.constant.ApplicationConstants.USER_ID_PARAM;
import static com.pepcus.apps.api.utils.CommonUtil.getCurrentDateInUTC;
import static com.pepcus.apps.api.utils.EntitySearchUtil.getEntitySearchSpecification;
import static com.pepcus.apps.api.utils.EntitySearchUtil.getPageable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.pepcus.apps.api.db.entities.Contact;
import com.pepcus.apps.api.db.entities.ThroneRole;
import com.pepcus.apps.api.db.entities.User;
import com.pepcus.apps.api.exception.APIErrorCodes;
import com.pepcus.apps.api.exception.ApplicationException;
import com.pepcus.apps.api.services.crypto.AppEncryptorDecryptor;

/**
 * The UserService class provides a collection of all
 * services related with users
 *
 */

@Service
public class UserService extends CommonService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AppEncryptorDecryptor encDecyptor;
    
    @Autowired
    private UserRoleValidator roleValidator;
    
    @Value("${com.pepcus.apps.api.user.default.password}")
    private String defaultPassword;

    private String encryptedDefaultPassword;

    @Value("${com.pepcus.apps.api.emailService.enabled}")
    private boolean isSendEmailEnabled;

    private Map<String, ThroneRole> roleMap;
    
    @PostConstruct
    public void init() {
        encryptedDefaultPassword = encDecyptor.encrypt(defaultPassword);
    }

    private static final String resource = USER;
    
    /**
     * Fetch all users from database based on offset, limit and sortField and search criteria
     * 
     * @param Integer offset First record index from database after sorting. Default value is 0
     * @param Integer limit Number of records to be fetched. Default value is 50
     * @param String sortField Field on which records needs to be sorted
     * @param String searchSpec Search string for filtering results
     * @return List<User> object 
     */
    public List<Contact> getAllUser(Integer offset, Integer limit, String sortField, 
            String searchSpec, String fields, 
            Map<String, String> requestParameters) throws ApplicationException  {

        Pageable pageable = getPageable(offset, limit, sortField, getDefaultSortField());

        if(logger.isDebugEnabled()) {
            logger.debug("Request parameters to filter, size and paginate records ");
            if (requestParameters != null) {
                requestParameters.entrySet().stream().forEach(entry -> { logger.debug(entry.getKey() + ":: " + entry.getValue()); });
            }
        }
        
        Class kclass = Contact.class;
        Map<String, List<String>> classVsFieldMap = validateFieldsAndGetClassVsFieldMap(fields, kclass);
        
        Specification<Contact> spec = getEntitySearchSpecification(searchSpec, requestParameters, kclass, new Contact());

        return findAll(false, pageable, spec, kclass, userRepository, USER_ID_PARAM, classVsFieldMap);
    }

    /**
     * Fetch specific user from system with fields
     * @param userId
     * @return User object 
     */
    public Contact getUser(Integer userId, String fields) {
        Class kclass = Contact.class;
        Map<String, List<String>> classVsFieldMap = validateFieldsAndGetClassVsFieldMap(fields, kclass);
        
        Contact user = findOne(userId, kclass, userRepository, classVsFieldMap);
        
        if (user == null) {
            throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "user", "userId = "+ userId);
        }
        
        return user;
    }
    
    /**
     * Fetch specific user from system
     * @param userId
     * @return User object 
     */
    public Contact getUser(Integer userId) {
        //Contact user = userRepository.findOne(userId);
        Contact user = null;
        if (user == null) {
            throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "user", "userId = "+ userId);
        }
        return user;
    }
    
    /**
     * Fetch specific active user from system
     * @param userId
     * @return User object 
     */
    public Contact getActiveUser(Integer userId) {
        //Contact user = userRepository.findActiveOne(userId);
      Contact user = null;
        if (user == null) {
            throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "user", "userId = "+ userId);
        }
        return user;
    }

    /**
     * Add a user in system
     * Also adds learnUser
     * 
     * @param Contact object
     */

    @Transactional
    public Contact addUser(Contact user, Integer brokerId, Boolean suppressEmail) {
        //Saving default password
        user.setPasswordApps(encryptedDefaultPassword);

        Boolean defaultSuppressEmail = false;

        Contact throneUser = addUser(user, brokerId, defaultSuppressEmail);

        return throneUser;
    }

    /**
     * Update a user in database
     * 
     * @param Contact object
     * @throws ApplicationException
     * @throws IOException 
     */
    @Transactional
    public Contact updateUser(Integer userId, String userJson, Integer brokerId) throws ApplicationException, IOException {

    	//Contact userInDb = userRepository.findActiveOne(userId);
      Contact userInDb = null;

    	if (null == userInDb) {
    		throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "user", "userId="+userId);
    	}

    	//Fetch Old User Name
    	String userNameInDb = userInDb.getUserName();

    	Contact user = update(userJson, userInDb);
    	validateObject(user);

    	//Fetch new user name and assign old user name back in Persistence Context for Duplicate check
    	String userName = user.getUserName();
    	user.setUserName(userNameInDb);

    	userName = generateUserName(user.getUserName(), user.getEmail(), user.getFirstName(),
    			user.getLastName());

    	user.setUserName(userName);

    	user.setActivationDate(getCurrentDateInUTC());

    	// If not passed in model, then object will become in-active.
    	//return userRepository.save(user);
    	return new Contact();
    }

    /**
     * Check for duplicate username in database
     * @param username
     * @return
     */
    public Contact getUser(String username) {
        if (username == null) {
            return null;
        }

        List<Contact> usersInDb = null;
        
        try {
           // usersInDb = userRepository.findByUserNameStartingWith(username);
        } catch(JpaObjectRetrievalFailureException e) {
            throw ApplicationException.createBadRequest(APIErrorCodes.DUPLICATE_PORTAL_USER_RECORD, username);
        }

        if (null != usersInDb && !usersInDb.isEmpty()) {
            for (Contact user : usersInDb) {
                if (null != user) {
                    return user;
                }
            }
        }

        return null;
    }

    /**
     * Logic to generate username from email, firstName and lastName if it is blank
     * If it is not blank and duplicate then throws exception for duplicate user
     * 
     * JIRA = THR-3927,4202
     * 
     * @param userName
     * @param email
     * @param firstName
     * @param lastName
     * @return
     */
    protected String generateUserName(String userName, String email, String firstName, String lastName) {
        if (!StringUtils.isBlank(userName)) {
            Contact user = getUser(userName);
            if (user == null) {
                return userName;
            } else if (user.getUserName().equalsIgnoreCase(userName)) {
                throw ApplicationException.createBadRequest(APIErrorCodes.DUPLICATE_ACTIVE_USER_RECORD, userName);
            } else if (user.getUserName().toLowerCase().startsWith(userName.concat(UNDERSCORE).concat(UNDERSCORE).toLowerCase())) {
                throw ApplicationException.createBadRequest(APIErrorCodes.DUPLICATE_INACTIVE_USER_RECORD, userName);
            }
        }

        // Make email as userName if userName is Blank
        userName = email;

        int i = 1;
        while (true) {
            if (!StringUtils.isBlank(userName) && getUser(userName) == null) { // Not blank and not duplicate
                break;
            }
            firstName = firstName != null ? firstName.replace(SPACE, UNDERSCORE) : null;
            lastName = lastName != null ? lastName.replace(SPACE, UNDERSCORE) : null;
            userName = firstName + UNDERSCORE + lastName + UNDERSCORE + i;
            i = i + 1;
        }

        return userName;
    }

    /**
     * Return default sort field for user service
     * 
     * @return String 
     */
    @Override
    public String getDefaultSortField()  {
        return DEFAULT_SORT_BY_USER_NAME;
    }

    /**
     * Validates roleId from the Database.
     * 
     * @param roleId
     * @return
     */
    public boolean validateRoleIdFromDB(Integer roleId) {
        return throneRoleRepository.findOne(roleId) == null ? false : true;
    }
    

    public Contact getUserByEmail(String email) {
        //Contact user = userRepository.findFirstByEmailAndIsActive(email, 1);
        Contact user = null;
        if (user == null) {
            throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "email", "email = "+ email);
        }
        return user;
    }

}