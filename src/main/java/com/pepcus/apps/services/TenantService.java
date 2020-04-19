package com.pepcus.apps.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pepcus.apps.db.entities.TenantEntity;
import com.pepcus.apps.db.repositories.TenantRepository;
import com.pepcus.apps.exception.APIErrorCodes;
import com.pepcus.apps.exception.ApplicationException;

@Service
public class TenantService {

  @Autowired
  TenantRepository tenantRepository;

  public List<TenantEntity> getTenants() {
    return (List<TenantEntity>) tenantRepository.findAll();
  }

  public TenantEntity getTenantByKey(String tenantKey) {
    TenantEntity tenantEntity = tenantRepository.findByKey(tenantKey);
    if (tenantEntity == null) {
      throw ApplicationException.createEntityNotFoundError(APIErrorCodes.ENTITY_NOT_FOUND, "tenant",
          "tenantKey = " + tenantKey);
    }
    return tenantEntity;
  }

}
