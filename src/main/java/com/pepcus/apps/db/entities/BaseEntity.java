package com.pepcus.apps.db.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity implements SearchableEntity {

	//We can put all the common attributes that is needed for all entities like created_on, modified_on, created_by and modified_by.
	@NotNull
	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "created_on")
	private Long createdOn = System.currentTimeMillis();

	@Column(name = "modified_on")
	private Long modifiedOn;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Override
	public List<String> getSearchFields() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNodeName() {
		return "entity";
	}

	@Override
	public String getMultiDataNodeName() {
		return "entities";
	}
}
