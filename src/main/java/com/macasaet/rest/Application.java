package com.macasaet.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.bazaarvoice.auth.hmac.server.Authenticator;
import com.bazaarvoice.auth.hmac.server.HmacAuthFeature;

@ApplicationPath("api")
public class Application<P> extends ResourceConfig {

    public Application() {
        // Security
        register(new HmacAuthFeature<String>());
        register(new AbstractBinder() {
            protected void configure() {
                // The P parameter is to trick HK2 into injecting the Authenticator where it is needed.
                bind(MyAuthenticator.class).to(new TypeLiteral<Authenticator<P>>() {});
            }
        });

        // Serialisation
        packages("org.glassfish.jersey.examples.jackson");
        register(JacksonFeature.class);

        // Endpoints
        register(Resource.class);
    }

}