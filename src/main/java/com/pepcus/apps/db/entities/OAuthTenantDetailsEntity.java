package com.pepcus.apps.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

/**
 * Database entity object for 'oauth_tenant_details' database table
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Entity
@Table(name = "oauth_tenant_details")
@Data
@JsonInclude(Include.NON_EMPTY)
public class OAuthTenantDetailsEntity extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "tenant_id")
  private Integer tenantId;

  @Column(name = "client_id")
  private String clientId;

  @Column(name = "client_secret")
  private String clientSecret;

  @Column(name = "scope")
  private String scope;

  @Column(name = "authorized_grant_types")
  private String authorizedGrantTypes;

  @Column(name = "redirect_uri")
  private String redirectUri;

  @Column(name = "authorities")
  private String authorities;

  @Column(name = "access_token_validity")
  private Integer accessTokenValidity;

  @Column(name = "refresh_token_validity")
  private String refreshTokenValidity;

  @Column(name = "additional_information")
  private String additionalInformation;

  @Column(name = "autoapprove")
  private String autoapprove;

}
