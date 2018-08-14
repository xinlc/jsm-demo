package org.leo.jsm.core;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseResource {

    private static final Logger logger = Logger.getLogger(BaseResource.class);

    @Autowired
    protected Constants constants = null;

    @Context
    UriInfo uriInfo;

    protected Response ifNotModified(BaseModel result, Request request) {
        if (null == result)
            return message(Response.Status.NOT_FOUND, 40400, null);
        ResponseBuilder builder = request.evaluatePreconditions(result.getETag());
        if (null != builder)
            return builder.build();
        return Response.ok(result).tag(result.getETag()).build();
    }

    protected Response message(Status status, int msgCode, Throwable t) {
        Message msg = new Message(msgCode, constants.messageMap.get(String.valueOf(msgCode)));
        if (null != t) {
            logger.error("Request URI:" + uriInfo.getRequestUri() + " Message:" + msg.getMsg(), t);
        } else {
            logger.info("Request URI:" + uriInfo.getRequestUri() + " Message:" + msg.getMsg());
        }
        return Response.status(status).entity(msg).build();
    }

}
