package com.pepcus.apps.api.services;

import static com.pepcus.apps.api.constant.ApplicationConstants.ROLE_TYPE_BROKER;
import static com.pepcus.apps.api.constant.ApplicationConstants.ROLE_TYPE_RE;
import static com.pepcus.apps.api.constant.ApplicationConstants.ROLE_TYPE_THINKHR;
import static com.pepcus.apps.api.constant.ApplicationConstants.SPECIAL_CASE_BROKER1;
import static com.pepcus.apps.api.constant.ApplicationConstants.SPECIAL_CASE_BROKER2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.pepcus.apps.api.db.entities.ThroneRole;
import com.pepcus.apps.api.exception.APIErrorCodes;
import com.pepcus.apps.api.exception.ApplicationException;
import com.pepcus.apps.api.repositories.ThroneRoleRepository;

/**
 * Class to validate user specific roles
 * 
 */
@Component
public class UserRoleValidator {
    
    @Value("${app.default.tenant}")
    private Integer thinkhrBrokerId;
    
    @Autowired
    private ThroneRoleRepository roleRepository;
    
    /**
     * @param brokerId
     * @param companyId
     * @param role
     * @param companyName
     */
    public void validateRoleForUser(Integer brokerId, 
            Integer companyId, 
            String roleName, 
            String companyName, 
            ThroneRole thrRole) {

        String type = null; //By default setting role type as RE;
        
        if (isBrokerUser(brokerId, companyId)) { //i.e. Broker User role assignment
            type = ROLE_TYPE_BROKER;
            if (isThinkhrBroker(brokerId)) {
                type = ROLE_TYPE_THINKHR;
            } 
        } else {
            type = ROLE_TYPE_RE;
        }
        
        Integer brokerIdForRoleCheck = (brokerId.equals(SPECIAL_CASE_BROKER1) ||
                brokerId.equals(SPECIAL_CASE_BROKER2)) ? brokerId : null; //For Paychex, we need to check with brokerId, else null

        boolean isValid = isValidRole(roleName, thrRole, type,
                brokerIdForRoleCheck);
        
        if (!isValid) {
            throw ApplicationException.createBadRequest(APIErrorCodes.INVALID_ROLE, "name", roleName, companyName);
        }
        
    }

    /**
     * @param roleName
     * @param role
     * @param type
     * @param brokerIdForRoleCheck
     * @return
     */
    private boolean isValidRole(String roleName,
            ThroneRole role, String type,
            Integer brokerIdForRoleCheck) {

        if ( type == null) {
            return false;
        }

        if (role == null) {
            return roleRepository.findFirstByNameAndTypeAndBrokerId(roleName, type, brokerIdForRoleCheck) != null ;
        } else {
            return type.equalsIgnoreCase(role.getType()) && (brokerIdForRoleCheck == null || 
                    (brokerIdForRoleCheck != null && brokerIdForRoleCheck.equals(role.getBrokerId()))) ;
        }
    }

    /**
     * @param brokerId
     * @return
     */
    private boolean isThinkhrBroker(Integer brokerId) {
        return brokerId.equals(thinkhrBrokerId);
    }

    /**
     * @param brokerId
     * @param companyId
     * @return
     */
    private boolean isBrokerUser(Integer brokerId, Integer companyId) {
        return companyId.equals(brokerId);
    }
    
}