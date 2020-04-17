package com.pepcus.apps.api.services;

import static com.pepcus.apps.api.constant.ApplicationConstants.TOTAL_RECORDS;
import static com.pepcus.apps.api.utils.RequestUtils.setRequestAttribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pepcus.apps.api.db.entities.ThronePermission;
import com.pepcus.apps.api.db.entities.ThroneRole;
import com.pepcus.apps.api.exception.ApplicationException;
import com.pepcus.apps.api.model.AppAuthData;
import com.pepcus.apps.api.utils.RequestUtils;

@Service
public class PermissionService extends CommonService { 
    
    /**
    *
    * Fetch permissions of authenticated user
    * 
    * @return List<ThronePermission> object 
    * @throws ApplicationException 
    * 
    */
   public List<ThronePermission> getPermissionsOfAuthenticatedUser() 
           throws ApplicationException {
       
       AppAuthData authData = RequestUtils.getAppAuthData();

       ThroneRole role = throneRoleRepository.findByNameAndBrokerId(authData.getRole(), authData.getBrokerId());     
       
       if (role.getPermissions() == null || role.getPermissions().isEmpty()) {
           setRequestAttribute(TOTAL_RECORDS, 0);
           return Collections.EMPTY_LIST;
       } else {
           setRequestAttribute(TOTAL_RECORDS, role.getPermissions().size());
           return new ArrayList<>(role.getPermissions());       
       }
   }    
}
