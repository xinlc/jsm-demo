package org.leo.jsm.api.model;

import javax.ws.rs.core.EntityTag;

import org.leo.jsm.core.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProfileModel extends BaseModel {

    private String userId;
    private String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JsonIgnore
    @Override
    public EntityTag getETag() {
        eTag = new EntityTag(String.valueOf(hashCode()));
        return eTag;
    }

}
