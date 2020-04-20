package com.pepcus.apps.services;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.jcabi.aspects.Loggable;
import com.pepcus.apps.constant.ApplicationConstants;
import com.pepcus.apps.db.entities.OAuthTenantDetailsEntity;
import com.pepcus.apps.db.entities.TenantEntity;
import com.pepcus.apps.db.repositories.OAuthClientDetailsRepository;
import com.pepcus.apps.db.repositories.TenantRepository;
import com.pepcus.apps.exception.APIErrorCodes;
import com.pepcus.apps.exception.ApplicationException;
import com.pepcus.apps.utils.CommonUtil;
import com.pepcus.apps.utils.RequestUtil;

/**
 * The TenantServie class provides a collection of all services related with tenant
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Service
public class TenantService {

  @Value("${com.pepcus.apps.clientId.size}")
  private Integer clientIdSize;

  @Value("${com.pepcus.apps.clientSecret.size}")
  private Integer clientSecretSize;

  @Autowired
  TenantRepository tenantRepository;

  @Autowired
  OAuthClientDetailsRepository oAuthClientDetailsRepository;

  /**
   * To return all tenant
   * 
   * @return
   */
  public List<TenantEntity> getTenants() {
    List<TenantEntity> list = (List<TenantEntity>) tenantRepository.findAll();
    RequestUtil.setRequestAttribute(ApplicationConstants.TOTAL_RECORDS, list.size());
    return list;
  }

  /**
   * Get Teant entity from tenantKey
   * 
   * @param tenantKey
   * @return
   */
  public TenantEntity getTenantByKey(String tenantKey) {
    TenantEntity tenantEntity = tenantRepository.findByKey(tenantKey);
    if (tenantEntity == null) {
      throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "tenant",
          "tenantKey = " + tenantKey);
    }
    return tenantEntity;
  }

  public OAuthTenantDetailsEntity registerTenant(String tenantKey, OAuthTenantDetailsEntity oauthTenantDetails) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Create tenant oautn details and Save in DB
   * 
   * @param tenantKey
   * @param clientDetails
   * @return
   */
  @Loggable(prepend = true, value = Loggable.DEBUG, ignore = {ApplicationException.class})
  private OAuthTenantDetailsEntity createAndSaveClientDetails(String tenantKey,
      OAuthTenantDetailsEntity clientDetails) {

    OAuthTenantDetailsEntity oAuthTenantDetailsEntity = new OAuthTenantDetailsEntity();

    Integer tenantId = getTenantByKey(tenantKey).getId();
    oAuthTenantDetailsEntity.setTenantId(tenantId);

    if (StringUtils.isBlank(clientDetails.getClientId())) {
      oAuthTenantDetailsEntity.setClientId(CommonUtil.generateRandomAlphanumericHexString(clientIdSize));
    }

    if (StringUtils.isBlank(clientDetails.getClientSecret())) {
      oAuthTenantDetailsEntity.setClientSecret(CommonUtil.generateRandomAlphanumericHexString(clientSecretSize));
    }

    oAuthTenantDetailsEntity.setRedirectUri(clientDetails.getRedirectUri());
    oAuthTenantDetailsEntity.setIsActive(true);

    oAuthClientDetailsRepository.save(oAuthTenantDetailsEntity);

    return oAuthTenantDetailsEntity;
  }

}
