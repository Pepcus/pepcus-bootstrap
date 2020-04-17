package com.pepcus.apps.api.db.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Entity
@Table(name = "app_throne_permissions")
@Data
@JsonInclude(Include.NON_EMPTY)
public class ThronePermission implements SearchableEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "skuId")
    private Integer skuId;
    
    @Column(name = "displayLabel")
    private String displayLabel;
    
    @Column(name = "featureKey")
    private String featureKey;
    
    @Column(name = "apiResource")
    private String apiResource;
    
    @Column(name = "privilege")
    private String privilege;
    
    @Column(name = "permissionKey")
    private String permissionKey;
    
    @Column(name = "description")
    private String description;

    @Override
    @JsonIgnore
    public List<String> getSearchFields() {
        // Not supporting search
        return null;
    }

    @Override
    @JsonIgnore
    public String getNodeName() {
        return "permission";
    }

    @Override
    @JsonIgnore
    public String getMultiDataNodeName() {
        return "permissions";
    }

    @Override
    public void clearDefaultValues() {
        // TODO Auto-generated method stub
        
    }

}