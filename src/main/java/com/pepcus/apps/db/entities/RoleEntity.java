package com.pepcus.apps.db.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Entity
@Table(name = "role")
@Data
@JsonInclude(Include.NON_EMPTY)
public class RoleEntity extends BaseEntity {
  
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
  
  @NotNull
  @Column(name = "type")
  private String type;
  
  //Add association between role and permission
  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(
          name = "permission", 
          joinColumns = { @JoinColumn(name = "roleId") }, 
          inverseJoinColumns = { @JoinColumn(name = "permissionId") }
  )

  private Set<PermissionEntity> permissions;
}
