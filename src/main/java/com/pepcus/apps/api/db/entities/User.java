package com.pepcus.apps.api.db.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.cache.annotation.Cacheable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Entity
@Data
@Table(name="user")
@DynamicUpdate
@DynamicInsert
@JsonInclude(Include.NON_EMPTY)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements SearchableEntity{


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
  
  @NotNull
  @Column(name = "is_active")
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
  
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tenant_id")
  private Tenant tenant;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "login_id")
  private Login login;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "role_id")
  private Role role;
  
  
  @Override
  @JsonIgnore
  public List<String> getSearchFields() {
      List<String> searchColumns = new ArrayList<String>();
      searchColumns.add("lastName");
      searchColumns.add("firstName");
      searchColumns.add("email");
      return searchColumns;
  }

  @Override
  @JsonIgnore
  public String getNodeName() {
      return "user";
  }

  @Override
  @JsonIgnore
  public String getMultiDataNodeName() {
      return "users";
  }
  
  @Override
  @JsonIgnore
  public void clearDefaultValues() {
      this.setIsActive(null);
  }

}
