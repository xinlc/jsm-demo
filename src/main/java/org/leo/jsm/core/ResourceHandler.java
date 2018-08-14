package org.leo.jsm.core;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;

import org.leo.jsm.core.filter.AuthorityFilter;
import org.leo.jsm.core.filter.DecodeFilter;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class ResourceHandler extends ResourceConfig {

    private static final Logger logger = Logger.getLogger(ResourceHandler.class);

    public ResourceHandler() {
        logger.info("init service...");

        // for url decoder
        register(DecodeFilter.class);
        // security check
        register(AuthorityFilter.class);

        // register(MoxyXmlFeature.class);
        // register(MoxyJsonFeature.class);
        // register(MOXyJsonProvider.class);
        register(JacksonJsonProvider.class);

        logger.info("init done.");
    }
}
