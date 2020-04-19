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
import org.hibernate.annotations.Formula;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Entity
@Table(name = "role")
@Data
@JsonInclude(Include.NON_EMPTY)
public class RoleEntity extends BaseEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "key")
  private String key;

  @NotNull
  @Column(name = "name")
  private String name;
  
  @NotNull
  @Column(name = "description")
  private String description;
  
  @Column(name = "tenant_key")
  private String tenantKey;
  
  @NotNull
  @Column(name = "type")
  private String type;
  
  @ManyToMany(targetEntity = PermissionEntity.class)
  @JoinTable(name="sysadm_permission_role_relns",
      joinColumns=@JoinColumn(name="role_id"), 
      inverseJoinColumns=@JoinColumn(name="permission_id"))
  public Set<PermissionEntity> permissions;
  
  
}
