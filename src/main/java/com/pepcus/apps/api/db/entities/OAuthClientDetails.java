package com.pepcus.apps.api.db.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;

/**
 * 
 * Database entity object for OAuthClientDetails
 * 
 * Name of database table is app_throne_oauth_client_details
 * 
 *
 */
@Entity
@Table(name = "oauth_client_details")
@Data
@JsonInclude(Include.NON_EMPTY)
public class OAuthClientDetails implements SearchableEntity{
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    
    /*@JsonProperty(access = Access.READ_ONLY)
    @Column(name = "client_id")
    private Integer client;*/
    
    
    @Column(name = "client_id")
    private String clientId;
    
    @Column(name = "client_secret")
    private String clientSecret;
    
    @URL
    @Column(name = "redirect_url")
    private String redirectUrl;
    
    @Column(name = "is_active")
    @Pattern(regexp = "0|1", message="must be either 0 or 1")
    private String isActive;
   
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on")
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_on")
    private Date modifiedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_by")
    private String modifiedBy;
   

    @Override
    @JsonIgnore
    public List<String> getSearchFields() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @JsonIgnore
    public String getNodeName() {
        return "credentials";
    }

    @Override
    @JsonIgnore
    public String getMultiDataNodeName() {
        return "credentials";
    }
    
    @Override
    @JsonIgnore
    public void clearDefaultValues() {
        // TODO Auto-generated method stub
    }
    
}
