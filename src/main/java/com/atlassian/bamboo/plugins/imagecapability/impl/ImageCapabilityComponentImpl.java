package com.atlassian.bamboo.plugins.imagecapability.impl;

import com.atlassian.bamboo.plugins.imagecapability.api.ImageCapabilityComponent;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.sal.api.ApplicationProperties;

import javax.inject.Inject;
import javax.inject.Named;

@ExportAsService ({ImageCapabilityComponent.class})
@Named ("ImageCapabilityComponent")
public class ImageCapabilityComponentImpl implements ImageCapabilityComponent
{
        @ComponentImport
        private final ApplicationProperties applicationProperties;

        @Inject
        public ImageCapabilityComponentImpl(final ApplicationProperties applicationProperties)
    {
        this.applicationProperties = applicationProperties;
    }

    public String getName()
    {
        if(null != applicationProperties)
        {
            return "ImageCapabilityComponent:" + applicationProperties.getDisplayName();
        }
        
        return "ImageCapabilityComponent";
    }
}