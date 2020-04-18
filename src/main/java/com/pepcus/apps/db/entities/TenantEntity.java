package com.pepcus.apps.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Entity
@Table(name = "apps_tenant")
@Data
@JsonInclude(Include.NON_EMPTY)
  
public class TenantEntity extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(name = "key")
  private String key;

  @NotNull
  @Column(name = "name")
  private String name;
  
  @NotNull
  @Column(name = "description")
  private String description;
  
}
