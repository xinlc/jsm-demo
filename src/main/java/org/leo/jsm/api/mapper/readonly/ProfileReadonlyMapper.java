package org.leo.jsm.api.mapper.readonly;

import org.leo.jsm.api.entity.ProfileEntity;
import org.leo.jsm.core.BaseMapper;

public interface ProfileReadonlyMapper extends BaseMapper {

    // 得到个人资料页
    public ProfileEntity getProfileById(String userId);

}