package oosc.assignment06.webservice.rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Jersey registration to match the starter-jersey setup.
 */
@Component
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(ImageResource.class);
    }
}
