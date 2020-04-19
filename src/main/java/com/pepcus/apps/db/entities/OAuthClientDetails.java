package com.pepcus.apps.db.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.URL;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

/**
 * 
 * Database entity object for OAuthClientDetails
 * 
 * Name of database table is auth_tenant_details
 * 
 *
 */
@Entity
@Table(name = "auth_tenant_details")
@Data
@JsonInclude(Include.NON_EMPTY)
public class OAuthClientDetails extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tenant_id")
    private TenantEntity tenant;
    
    @Column(name = "client_id")
    private String clientId;
    
    @Column(name = "client_secret")
    private String clientSecret;
    
    @URL
    @Column(name = "redirect_url")
    private String redirectUrl;
    
    @Formula("select key from tenant where id= tenantId")
    private String tenantKey;
}
