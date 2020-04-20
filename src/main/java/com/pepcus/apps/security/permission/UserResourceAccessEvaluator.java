package com.pepcus.apps.security.permission;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.pepcus.apps.services.UserService;
import com.pepcus.apps.utils.RequestUtil;

/**
 * Class to evaluate different levels of access with given permission for 'users' resource
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Service("user")
public class UserResourceAccessEvaluator implements ResourceAccessEvaluator {

  @Autowired
  UserService userService;

  @Override
  public boolean hasAdminAccess(Authentication authentication,
      String resource,
      String permission,
      Serializable objectId) {

    return authentication.getAuthorities().contains("SYSTEM_ADMINISTRATOR");
  }


  @Override
  public boolean hasFirstLevelAdminAccess(Authentication authentication,
      String resource,
      String permission,
      Serializable objectId) {
    return authentication.getAuthorities().contains("TENANT_ADMIN");
  }


  @Override
  public boolean hasSelfAccess(Authentication authentication,
      String resource,
      String permission,
      Serializable objectId) {

    return RequestUtil.getAppAuthData().getUserId().equals((Integer) objectId);
  }

}
