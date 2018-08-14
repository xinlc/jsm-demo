package org.leo.jsm.api.mapper.writable;

import org.apache.ibatis.annotations.Param;
import org.leo.jsm.core.BaseMapper;

public interface ProfileMapper extends BaseMapper {

    // 个人资料页-更新用户姓名
    public void updateUserName(@Param("userId") String userId, @Param("userName") String userName);

}