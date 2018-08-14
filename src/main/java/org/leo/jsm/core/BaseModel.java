package org.leo.jsm.core;

import javax.ws.rs.core.EntityTag;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BaseModel {

	@JsonIgnore
	protected EntityTag eTag = null;

	public abstract EntityTag getETag();
/*
	public void setETag(EntityTag eTag) {
		this.eTag = eTag;
	}*/
	
}
