package org.leo.jsm.api.entity;

import org.leo.jsm.core.BaseEntity;

public class ProfileEntity extends BaseEntity {

	private Integer id;

	private String name; // 昵称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
