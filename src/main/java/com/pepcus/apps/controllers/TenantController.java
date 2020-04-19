package com.pepcus.apps.controllers;


import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pepcus.apps.db.entities.TenantEntity;
import com.pepcus.apps.exception.ApplicationException;
import com.pepcus.apps.services.TenantService;

/**
 * User Controller for performing operations related with tenant object.
 * 
 */
@RestController
@Validated
@RequestMapping(path = "/v1/tenants")
public class TenantController {

  private static Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  TenantService tenantService;


  @GetMapping
  List<TenantEntity> getAllTenants(@RequestParam Map<String, String> allRequestParams) throws ApplicationException {
    return tenantService.getTenants();
  }

  @GetMapping("/{tenantKey}")
  TenantEntity getTenant(@PathVariable(value = "key", required = false) String key) throws ApplicationException {
    return tenantService.getTenantByKey(key);
  }

}
