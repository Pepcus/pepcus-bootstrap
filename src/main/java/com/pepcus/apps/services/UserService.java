package com.pepcus.apps.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pepcus.apps.db.entities.RoleEntity;
import com.pepcus.apps.db.entities.UserEntity;
import com.pepcus.apps.exception.APIErrorCodes;
import com.pepcus.apps.exception.ApplicationException;
import com.pepcus.apps.services.crypto.AppEncryptorDecryptor;

/**
 * The UserService class provides a collection of all
 * services related with users
 *
 */

@Service
public class UserService extends CommonEntityService {

    @Autowired
    private AppEncryptorDecryptor encDecyptor;
    
    @Value("${com.pepcus.apps.user.default.password}")
    private String defaultPassword;

    private String encryptedDefaultPassword;

    private Map<String, RoleEntity> roleMap;
    
    @PostConstruct
    public void init() {
        encryptedDefaultPassword = encDecyptor.encrypt(defaultPassword);
    }

    /**
     * Fetch all users from database based on offset, limit and sortField and search criteria
     * 
     * @param Integer offset First record index from database after sorting. Default value is 0
     * @param Integer limit Number of records to be fetched. Default value is 50
     * @param String sortField Field on which records needs to be sorted
     * @param String searchSpec Search string for filtering results
     * @return List<User> object 
     */
    public List<UserEntity> getAllUser(Integer offset, Integer limit, String sortField, 
            String searchSpec, String fields, 
            Map<String, String> requestParameters) throws ApplicationException  {
    	
    	return Collections.EMPTY_LIST;
    }

    /**
     * Fetch specific user from system with fields
     * @param userId
     * @return User object 
     */
    public UserEntity getUser(Integer userId, String fields) {
        Class kclass = UserEntity.class;
        Map<String, List<String>> classVsFieldMap = validateFieldsAndGetClassVsFieldMap(fields, kclass);
        
        UserEntity user = findOne(userId, kclass, userRepository, classVsFieldMap);
        
        if (user == null) {
            throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "user", "userId = "+ userId);
        }
        
        return user;
    }

}