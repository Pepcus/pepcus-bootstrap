package com.pepcus.apps.db.entities;

import java.util.Date;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Entity
@Data
@Table(name="users")
@DynamicUpdate
@DynamicInsert
@JsonInclude(Include.NON_EMPTY)
public class UserEntity extends BaseEntity {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotNull(message = "First Name is mandatory")
  @Pattern(regexp = "^[a-zA-Z]*$", message = "should contains only Alphabets")
  @Column(name = "first_name")
  private String firstName;

  @NotNull(message = "Last Name is mandatory")
  @Pattern(regexp = "^[a-zA-Z]*$", message = "should contains only Alphabets")
  @Column(name = "last_name")
  private String lastName;

  @NotNull(message = "Date of Birth is mandatory")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @Column(name = "dob")
  private Date dateOfBirth;

  @Pattern(regexp = "^\\s*|[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",
      message = "should be valid email.")
  @Column(name = "email")
  private String email;

  @Column(name="username")
  private String username;
  
  @Column(name="encrypted_password")
  private String encryptedPassword;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tenant_id")
  private TenantEntity tenant;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "role_id")
  private RoleEntity role;
  
}
