package com.pepcus.apps.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class to hold all authorization/authentication/permission related attribute fetched from
 * token
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Data
@NoArgsConstructor
public class AppAuthData {

  private Integer userId;
  private String user;
  private String iss;
  private String role;
  private Integer sub;
  private String tenantKey;
  private List<String> permissions;

  /**
   * 
   * @param permission
   */
  public void addPermission(String permission) {
    if (permissions == null) {
      permissions = new ArrayList<String>();
    }

    permissions.add(role);
  }
}
