package org.leo.jsm.api.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.leo.jsm.api.entity.ProfileEntity;
import org.leo.jsm.api.utils.ProfileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.leo.jsm.api.dao.RedisCacheDao;
import org.leo.jsm.api.exception.BusinessException;
import org.leo.jsm.api.exception.RequestException;
import org.leo.jsm.api.model.ProfileModel;
import org.leo.jsm.api.service.ProfileService;
import org.leo.jsm.core.BaseResource;

@Api(value = "/profile")
@ApiResponses(value = {@ApiResponse(code = 401, message = "未授权的访问！"), @ApiResponse(code = 500, message = "API内部错误！")})
@Path("/")
public class ProfileResource extends BaseResource {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private RedisCacheDao redisCacheDao;

    @ApiOperation(value = "得到我的个人资料页", notes = "得到我的个人资料页。", response = ProfileModel.class, httpMethod = "GET")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "Token", dataType = "string", paramType = "header", required = true)})
    @Path("{userId}/profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@PathParam("userId") String userId) {

        // 验证输入参数
        try {
            this.verifyGetProfile(userId);
        } catch (RequestException re) {
            return message(Response.Status.BAD_REQUEST, re.getCode(), null);
        }

        // /执行业务操作：得到个人详细信息
        ProfileEntity profile = null;
        try {
            profile = this.profileService.getProfileById(userId);
        } catch (BusinessException be) {
            return message(Response.Status.BAD_REQUEST, be.getCode(), null);
        }

        ProfileModel profileDetail = this.convertToProfileDetail(profile, userId);

        return Response.ok(profileDetail).build();
    }


    @ApiOperation(value = "更新用户姓名", notes = "更新用户姓名。", response = Response.class, httpMethod = "PUT")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "Token", dataType = "string", paramType = "header", required = true)})
    @Path("{userId}/name")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserName(@HeaderParam("token") String token, ProfileModel profile) {

        String userId = this.profileService.getUserIdByToken(token);

        if (null == userId) {
            // 未经授权访问
            return message(Response.Status.BAD_REQUEST, 40100, null);
        }

        // 验证传入参数
        try {
            this.verifyUpdateUserName(userId, profile);
        } catch (RequestException re) {
            return message(Response.Status.BAD_REQUEST, re.getCode(), null);
        }

        Map<String, Boolean> result = new HashMap<String, Boolean>();
        Boolean hasFullAuth = false;
        // 执行业务操作：更新用户名
        String userName = profile.getUserName();
        try {
            hasFullAuth = this.profileService.updateUserName(userId, userName);
        } catch (BusinessException be) {
            return message(Response.Status.BAD_REQUEST, be.getCode(), null);
        }
        result.put("hasFullAuth", hasFullAuth);
        return Response.ok(result).build();
    }

    /**
     * 验证更新用户名的输入参数
     *
     * @param params
     * @throws RequestException
     */
    private void verifyUpdateUserName(String userId, ProfileModel profile) throws RequestException {

        //验证用户id
        ProfileUtils.verifyUserId(userId);

        //验证用户名
        if (null == profile) {
            throw new RequestException(910030);
        } else {
            String userName = profile.getUserName();
            if (null == userName || "".equals(userName)) {
                // 验证用户新改的名字是否为空
                throw new RequestException(910030);
            }
        }
    }

    /**
     * 验证得到个人资料页输入参数
     *
     * @param params
     * @throws RequestException
     */
    private void verifyGetProfile(String userId) throws RequestException {
        //验证用户id
        ProfileUtils.verifyUserId(userId);
    }

    private ProfileModel convertToProfileDetail(ProfileEntity profileEntity, String userId) {

        ProfileModel profileModel = new ProfileModel();

        if (profileEntity != null) {

            String name = profileEntity.getName();
            if (name == null) {
                name = "";
            }
            profileModel.setUserName(name);
        }

        return profileModel;
    }

}
		
	

	
