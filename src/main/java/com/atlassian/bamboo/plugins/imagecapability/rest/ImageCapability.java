package com.atlassian.bamboo.plugins.imagecapability.rest;

import com.atlassian.bamboo.security.BambooPermissionManager;
import com.atlassian.bamboo.user.BambooAuthenticationContext;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.user.User;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A resource of message.
 */
@Component
@Path("/imageCapabilities")
public class ImageCapability {

    @ComponentImport
    private BambooAuthenticationContext userContext;

    @ComponentImport
    private BambooPermissionManager permissionManager;

    @Autowired
    private void setUserContext(@ComponentImport BambooAuthenticationContext userContext) {
        this.userContext = userContext;
    }

    @Autowired
    private void setPermissionManager(@ComponentImport BambooPermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    // Error message for unauthorized access
    private static final Response RESPONSE_ERROR_UNAUTHORIZED;
    static {
        JSONObject json = new JSONObject();
        json.put("error", "You are not authorized to perform this request.");
        RESPONSE_ERROR_UNAUTHORIZED = Response
                .status(Response.Status.UNAUTHORIZED)
                .type(MediaType.APPLICATION_JSON)
                .entity(json)
                .build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{id}")
    public Response getImageCapability(@PathParam("id") String id) {
        if (!isAdmin()) {
            return RESPONSE_ERROR_UNAUTHORIZED;
        }
        try {
            int integerId = Integer.parseInt(id);
            return Response.ok(ImageCapabilityHelper.getImageCapability(integerId)).build();
        } catch (IllegalArgumentException e) {
            JSONObject json = new JSONObject();
            json.put("error", e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(json)
                    .build();
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getImageCapabilities() {
        if (!isAdmin()) {
            return RESPONSE_ERROR_UNAUTHORIZED;
        }
        List<ImageCapabilityModel> models = ImageCapabilityHelper.getImageCapabilities();
        return Response.ok(new GenericEntity<List<ImageCapabilityModel>>(models) {}).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response addImageCapability(@PathParam("id") String id, String body) {
        if (!isAdmin()) {
            return RESPONSE_ERROR_UNAUTHORIZED;
        }
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        try {
            JSONObject json = (JSONObject) parser.parse(body);
            int idInteger = Integer.parseInt(id);
            JSONArray configsToAdd = (JSONArray) json.get("capabilities");
            Map<String, String> capabilities = new HashMap<>();
            for (Object o : configsToAdd) {
                JSONObject config = (JSONObject) o;
                capabilities.put(config.get("key").toString(), config.get("value").toString());
            }
            ImageCapabilityHelper.addImageCapability(idInteger, capabilities);
            return getImageCapability(id);
        } catch (ParseException | NullPointerException | IllegalArgumentException e) {
            JSONObject json = new JSONObject();
            json.put("error", e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(json)
                    .build();
        }
    }

    @DELETE
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response deleteImageCapability(@PathParam("id") String id, String body) {
        if (!isAdmin()) {
            return RESPONSE_ERROR_UNAUTHORIZED;
        }
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        try {
            JSONObject json = (JSONObject) parser.parse(body);
            int idInteger = Integer.parseInt(id);
            JSONArray configsToDelete = (JSONArray) json.get("capabilities");
            List<String> keys = new ArrayList<>();
            for (Object o : configsToDelete) {
                String key = o.toString();
                keys.add(key);
            }
            ImageCapabilityHelper.deleteCapability(idInteger, keys);
            return getImageCapability(id);
        } catch (ParseException | NullPointerException | IllegalArgumentException e) {
            JSONObject json = new JSONObject();
            json.put("error", e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(json)
                    .build();
        }
    }

    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON})
    @Path("/{id}")
    public Response updateImageCapability(@PathParam("id") String id, String body) {
        if (!isAdmin()) {
            return RESPONSE_ERROR_UNAUTHORIZED;
        }
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        try {
            JSONObject json = (JSONObject) parser.parse(body);
            int idInteger = Integer.parseInt(id);
            JSONArray configsToUpdate = (JSONArray) json.get("capabilities");
            Map<String, String> capabilities = new HashMap<>();
            for (Object o : configsToUpdate) {
                JSONObject config = (JSONObject) o;
                capabilities.put(config.get("key").toString(), config.get("value").toString());
            }
            ImageCapabilityHelper.updateImageCapability(idInteger, capabilities);
            return getImageCapability(id);
        } catch (ParseException | NullPointerException | IllegalArgumentException e) {
            JSONObject json = new JSONObject();
            json.put("error", e.getMessage());
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(json)
                    .build();
        }
    }

    private boolean isAdmin() {
        User user = userContext.getUser();
        assert user != null;
        return permissionManager.isAdmin(user.getName());
    }

}