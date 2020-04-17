package com.pepcus.apps.api.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppAuthData {
    
    private Integer brokerId;
    private Integer clientId;
    private String clientName;
    private Integer userId;
    private Integer configurationId;
    private String user;
    private String iss;
    private String role;
    private Integer sub;
    private List<String> permissions;
    
    /**
     * 
     * @param permission
     */
    public void addPermission(String permission) {
        if (permissions == null) {
            permissions = new ArrayList<String>();
        }
        
        permissions.add(permission);
    }
}
