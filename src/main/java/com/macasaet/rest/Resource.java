package com.macasaet.rest;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.bazaarvoice.auth.hmac.server.HmacAuth;
import com.macasaet.rest.domain.Entry;

@Path("/")
public class Resource {

    @GET
    @Path("health")
    @Produces(APPLICATION_JSON)
    public String ruok() {
        return "{\"status\": \"healthy\"}";
    }

    @GET
    @Path("entries")
    @Produces(APPLICATION_JSON)
    public List<? extends Entry> getEntries(@HmacAuth final String principal) {
        final Entry x = new Entry();
        x.setTitle("an entry");
        x.setDescription("a description");
        final Entry y = new Entry();
        y.setTitle("another entry");
        y.setDescription("another description");
        return unmodifiableList(asList(x, y));
    }

    @GET
    @Path("entries/{entryId}")
    @Produces(APPLICATION_JSON)
    public Entry getEntry(@HmacAuth final String principal, @PathParam("entryId") final String entryId) {
        final Entry retval = new Entry();
        retval.setId(entryId);
        retval.setTitle("title");
        retval.setDescription("description");
        return retval;
    }

    @POST
    @Path("entries")
    @Produces(APPLICATION_JSON)
    public Response addEntry(@HmacAuth final String principal, final Entry entry) throws URISyntaxException {
        final String entryId = UUID.randomUUID().toString();
        entry.setId(entryId);
        entry.setLastUpdatedBy(principal);
        final URI location = new URI("/api/entries/" + entryId);
        return Response.created(location).entity(entry).build();
    }

    @PUT
    @Path("entries/{entryId}")
    @Produces(APPLICATION_JSON)
    public Response updateEntry(@HmacAuth final String principal, @PathParam("entryId") final String entryId, final Entry entry)
        throws URISyntaxException {
        entry.setId(entryId);
        entry.setLastUpdatedBy(principal);
        final URI location = new URI("/api/entries/" + entryId);
        return Response.created(location).entity(entry).build();
    }

}