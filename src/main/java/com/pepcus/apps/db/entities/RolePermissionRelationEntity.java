package com.pepcus.apps.db.entities;

import javax.persistence.Column;

import lombok.Data;

/**
 * @author sandeep.vishwakarma
 *
 */
@Data
public class RolePermissionRelationEntity extends BaseEntity {

	//Add join
	@Column(name = "roleId")
	private Integer roleId;
	
	//Add join
	@Column(name = "permissionId")
	private Integer permissionId;
}
