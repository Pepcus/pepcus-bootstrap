package com.pepcus.apps.api.db.entities;

import static com.pepcus.apps.api.constant.ApplicationConstants.DATE_PATTERN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.cache.annotation.Cacheable;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.Data;


/**
 * 
 * Database entity object for User
 * 
 * Name of database table is contacts
 * 
 */
@Entity
@Data
@Table(name="contacts")
@DynamicUpdate
@DynamicInsert
@JsonInclude(Include.NON_EMPTY)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements SearchableEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @JsonProperty(access = Access.READ_ONLY)
    @Column(name="contactID")
    private Integer userId;

    @NotBlank
    @Column(name="First_Name", nullable=false)
    private String firstName;

    @NotBlank
    @Column(name="Last_Name")
    private String lastName;

    @Column(name="UserName")
    private String userName;

    @NotBlank
    @Email
    @Column(name="Email")
    private String email;

    @NotBlank
    @Formula("(select c.Client_Name from clients c where c.clientID = Client_ID)")
    private String companyName;

    @JsonProperty(access = Access.READ_ONLY) 
    @Formula("(select c.Broker from clients c where c.clientID = Client_ID)")
    private Integer brokerId;
    
    @JsonProperty(access = Access.READ_ONLY)
    @Formula("(SELECT b.client_name from clients b, clients c where b.clientID = c.Broker and c.clientID = Client_ID)")
    private String brokerName;

    @JsonProperty(access = Access.READ_ONLY)
    @Column(name="client_id", insertable=false, updatable=false)
    private Integer companyId;
    
    @JsonIgnore
    @Column(name="t1_roleId")
    private Integer roleId;

    @Formula("(select r.name from app_throne_roles r where r.id = t1_roleId)")
    private String role;
    
    @Column(name="Mobile")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String mobile;

    @NotBlank
    @Column(name="Phone")
    @Size(max = 25)
    private String phone;

    @Column(name="password_apps")
    @JsonIgnore
    private String passwordApps;

    @JsonProperty(access = Access.READ_ONLY)
    @Column(name="lastUpdated")
    private Integer lastUpdated;

    @NotNull
    @Column(name="search_help")
    private String searchHelp = ""; 

    @Column(name="Title")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String title;

    @NotNull
    @Column(name="blockedAccount", nullable=false)
    private Integer blockedAccount = 0;

    @NotNull
    @Column(name="mkdate", nullable=false)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String mkdate = "";

    @JsonIgnore
    @Column(name = "bounced")
    private Integer bounced;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activationDate")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Date activationDate;

    @Column(name="codevalid")
    @JsonIgnore
    private String codevalid = "";

    @Column(name = "active")
    @JsonProperty(access = Access.READ_ONLY)
    private Integer isActive = 1;

    @JsonIgnore
    @Formula("(select c.t1_is_active from clients c where c.clientID = Client_ID)")
    private Integer isThroneUser = 1; 
    
    @Column(name = "addedBy")
    private String addedBy;

    /*@Column(name="client_hours")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String clientHours;*/

    @Column(name="client_status")
    private String clientStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deactivationDate")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Date deactivationDate;

    @Column(name="deleted")
    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer deleted;

    @Column(name="t1_customfield1")
    private String customField1;

    @Column(name="t1_customfield2")
    private String customField2;

    @Column(name="t1_customfield3")
    private String customField3;

    @Column(name="t1_customfield4")
    private String customField4;
    
    @Column(name = "t1_department")
    private String department;
    
    @Column(name = "t1_jobtitle")
    private String jobTitle;
    
    @Override
    @JsonIgnore
    public List<String> getSearchFields() {
        List<String> searchColumns = new ArrayList<String>();
        searchColumns.add("userName");
        searchColumns.add("jobTitle");
        searchColumns.add("searchHelp");
        searchColumns.add("brokerName");

        searchColumns.add("phone");
        searchColumns.add("mobile");
        searchColumns.add("lastName");
        //searchColumns.add("firstMailMessage");

        searchColumns.add("firstName");
        //searchColumns.add("fax");
        searchColumns.add("email");
        searchColumns.add("companyName");
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
        this.setBlockedAccount(null);
        this.setIsActive(null);
    }
    
}