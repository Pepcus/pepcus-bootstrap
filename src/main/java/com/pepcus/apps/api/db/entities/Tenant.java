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
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Entity
@Table(name = "tenant")
@Data
@JsonInclude(Include.NON_EMPTY)
public class Tenant implements SearchableEntity {
  
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
