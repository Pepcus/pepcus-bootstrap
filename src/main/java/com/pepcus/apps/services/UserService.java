package com.pepcus.apps.services;

import static com.pepcus.apps.utils.EntitySearchUtil.getEntitySearchSpecification;
import static com.pepcus.apps.utils.EntitySearchUtil.getPageable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.pepcus.apps.db.entities.RoleEntity;
import com.pepcus.apps.db.entities.UserEntity;
import com.pepcus.apps.exception.APIErrorCodes;
import com.pepcus.apps.exception.ApplicationException;
import com.pepcus.apps.services.crypto.AppEncryptorDecryptor;

/**
 * The UserService class provides a collection of all services related with users
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Service
public class UserService extends CommonEntityService {

  private static final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private AppEncryptorDecryptor encDecyptor;

  private String encryptedDefaultPassword;

  private Map<String, RoleEntity> roleMap;


  /**
   * Fetch all users from database based on offset, limit and sortField and search criteria
   * 
   * @param Integer offset First record index from database after sorting. Default value is 0
   * @param Integer limit Number of records to be fetched. Default value is 50
   * @param String sortField Field on which records needs to be sorted
   * @param String searchSpec Search string for filtering results
   * @return List<User> object
   */

  public List<UserEntity> getAllUser(Integer offset,
      Integer limit,
      String sortField,
      String searchSpec,
      String fields,
      Map<String, String> requestParameters) throws ApplicationException {

    Pageable pageable = getPageable(offset, limit, sortField, getDefaultSortField());

    logger.debug("Request parameters to filter, size and paginate records {}", requestParameters);

    Class kclass = UserEntity.class;
    Specification<UserEntity> spec =
        getEntitySearchSpecification(searchSpec, requestParameters, kclass, new UserEntity());

    // return findAll(pageable, spec, userRepository, USER_ID_PARAM);
    return Collections.emptyList();
  }

  /**
   * Fetch specific user from system with fields
   * 
   * @param userId
   * @return User object
   */
  public UserEntity getUser(Integer userId, String fields) {
    Class kclass = UserEntity.class;
    Map<String, List<String>> classVsFieldMap = validateFieldsAndGetClassVsFieldMap(fields, kclass);
    UserEntity user = findOne(userId, kclass, userRepository, classVsFieldMap);
    if (user == null) {
      throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "user",
          "userId = " + userId);
    }
    return user;
  }

}
