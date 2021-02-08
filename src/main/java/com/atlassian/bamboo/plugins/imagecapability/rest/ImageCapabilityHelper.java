package com.atlassian.bamboo.plugins.imagecapability.rest;

import com.atlassian.bamboo.agent.elastic.server.ElasticImageConfiguration;
import com.atlassian.bamboo.agent.elastic.server.ElasticImageConfigurationManager;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ImageCapabilityHelper {

    @ComponentImport
    private static ElasticImageConfigurationManager configurationManager;


    @Autowired
    private void setConfigurationManager(@ComponentImport ElasticImageConfigurationManager configurationManager) {
        ImageCapabilityHelper.configurationManager = configurationManager;
    }

    /**
     * Returns all capabilities of all configurations.
     * @return A list of ImageCapabilityModel describing the capabilities
     */
    public static List<ImageCapabilityModel> getImageCapabilities() {
        List<ImageCapabilityModel> models = new ArrayList<>();
        for (ElasticImageConfiguration config : configurationManager.getAllElasticImageConfigurations()) {
            HashMap<String, String> capabilities = new HashMap<>();
            for (Capability c : config.getCapabilitySet().getCapabilities()) {
                capabilities.put(c.getKey(), c.getValue());
            }
            models.add(new ImageCapabilityModel(config.getId(), config.getAmiId(),
                    config.getConfigurationName(), capabilities));
        }
        return models;
    }

    /**
     * Searches and returns all capability of a specific configuration.
     * @param id configuration id
     * @return ImageCapabilityModel describing the capability
     * @throws IllegalArgumentException if id is invalid
     */
    public static ImageCapabilityModel getImageCapability (int id) throws IllegalArgumentException {
        ElasticImageConfiguration configuration = configurationManager.getElasticImageConfigurationById(id);
        if (configuration == null) {
            throw new IllegalArgumentException("Id not found.");
        }
        HashMap<String, String> capabilities = new HashMap<>();
        for (Capability c : configuration.getCapabilitySet().getCapabilities()) {
            capabilities.put(c.getKey(), c.getValue());
        }
        return new ImageCapabilityModel(configuration.getId(),
                configuration.getAmiId(), configuration.getConfigurationName(), capabilities);
    }

    /**
     * Adds capabilities.
     * Only add if all capabilities to be added are valid.
     * @param id configuration id
     * @param capabilities a list of capabilities key to add
     * @throws IllegalArgumentException when id or key is invalid
     */
    public static void addImageCapability(int id, Map<String, String> capabilities) throws IllegalArgumentException {
        ElasticImageConfiguration configuration = configurationManager.getElasticImageConfigurationById(id);
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration id not found.");
        }
        for (String key : capabilities.keySet()) {
            if (configuration.getCapabilitySet().getCapability(key) != null) {
                throw new IllegalArgumentException("Image already has the capability "
                        + key + ". Please use PUT to modify.");
            }
            configuration.getCapabilitySet().addCapability(new CapabilityImpl(key, capabilities.get(key)));
        }
        // Saves the configuration
        configurationManager.saveElasticImageConfiguration(configuration);
    }

    /**
     * Deletes capabilities.
     * Only delete if all capabilities to be deleted are valid.
     * @param id configuration id
     * @param keys a list of capability keys to delete
     * @throws IllegalArgumentException when id or any key is invalid
     */
    public static void deleteCapability(int id, List<String> keys) throws IllegalArgumentException {
        ElasticImageConfiguration configuration = configurationManager.getElasticImageConfigurationById(id);
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration id not found.");
        }
        for (String key : keys) {
            if (configuration.getCapabilitySet().getCapability(key) == null) {
                throw new IllegalArgumentException("Configuration " + id + " does not have the capability " + key);
            }
            configuration.getCapabilitySet().removeCapability(key);
        }
        // Saves the configuration
        configurationManager.saveElasticImageConfiguration(configuration);
    }


    /**
     * Updates capabilities.
     * Only update if all capabilities to be updated are valid.
     * @param id configuration id
     * @param capabilities map of capabilities
     * @throws IllegalArgumentException when id is invalid
     */
    public static void updateImageCapability(int id, Map<String, String> capabilities) throws IllegalArgumentException {
        ElasticImageConfiguration configuration = configurationManager.getElasticImageConfigurationById(id);
        if (configuration == null) {
            throw new IllegalArgumentException("Configuration id not found.");
        }
        for (String key : capabilities.keySet()) {
            if (configuration.getCapabilitySet().getCapability(key) == null) {
                throw new IllegalArgumentException("Capability " + key + " not found!");
            }
            configuration.getCapabilitySet().removeCapability(key);
            configuration.getCapabilitySet().addCapability(new CapabilityImpl(key, capabilities.get(key)));
        }
        // Saves the configuration
        configurationManager.saveElasticImageConfiguration(configuration);
    }
}
