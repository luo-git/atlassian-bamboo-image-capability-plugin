package com.atlassian.bamboo.plugins.imagecapability.rest;

import javax.xml.bind.annotation.*;
import java.util.HashMap;

@XmlRootElement(name = "imageConfiguration")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "amiId", "name", "capabilities"})
public class ImageCapabilityModel {

    @XmlElementWrapper(name = "capabilities")
    @XmlElement(name = "capability")
    private HashMap<String, String> capabilities;

    @XmlElement(name = "amiId")
    private String amiId;

    @XmlElement(name = "id")
    private long id;

    @XmlElement(name = "name")
    private String name;

    public ImageCapabilityModel() {
    }

    public ImageCapabilityModel(long id, String amiId, String name, HashMap<String, String> capabilities) {
        this.id = id;
        this.amiId = amiId;
        this.name = name;
        this.capabilities = capabilities;
    }

    public HashMap<String, String> getCapability() {
        return capabilities;
    }

    public void setCapability(HashMap<String, String> capabilities) {
        this.capabilities = capabilities;
    }

    public String getAmiId(String amiId) {
        return this.amiId;
    }

    public void setAmiId(String amiId) {
        this.amiId = amiId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}