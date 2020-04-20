package com.pepcus.apps.db.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Formula;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

/**
 * Database entity object for 'role' database table
 * 
 * @author Sandeep.Vishwakarma
 *
 */
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

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIgnore
  private List<RolePermissionRelationEntity> rolePermissions;

  // TODO: Remove Transient and enable association
  /*
   * @ManyToMany(targetEntity = PermissionEntity.class, cascade = CascadeType.MERGE )
   * 
   * @JoinTable(name="role_permission", joinColumns=@JoinColumn(name="role_id"),
   * inverseJoinColumns=@JoinColumn(name="permission_id"))
   */
  @Transient
  public Set<PermissionEntity> permissions = new HashSet<>();


}
