package org.leo.jsm.core.filter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;

import org.apache.log4j.Logger;

@PreMatching
public class DecodeFilter implements ContainerRequestFilter {

    private static final Logger logger = Logger.getLogger(DecodeFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // TODO Auto-generated method stub
        String requestUri = requestContext.getUriInfo().getRequestUri().toString();
        logger.info("decoder filter before :" + requestUri);
        try {
            requestUri = java.net.URLDecoder.decode(requestUri, "utf-8");
            requestContext.setRequestUri(new URI(requestUri));
            logger.info("decoder filter after :" + requestUri);
        } catch (URISyntaxException e) {
            logger.error("Uri setting error:", e);
        }
    }

}
