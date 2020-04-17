package com.pepcus.apps.api.db.entities;

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

import lombok.Data;

@Entity
@Table(name = "app_throne_roles")
@Data
public class ThroneRole {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "isAdministrator")
    private Integer administrator;
    
    @Column(name = "isImportDefault")
    private Integer importDefault;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "brokerId")
    private Integer brokerId;
    
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "app_throne_permissions_roles", 
            joinColumns = { @JoinColumn(name = "roleId") }, 
            inverseJoinColumns = { @JoinColumn(name = "permissionId") }
    )
    private Set<ThronePermission> permissions;
}
