package it.com.atlassian.bamboo.plugins.imagecapability;

import com.atlassian.bamboo.plugins.imagecapability.api.ImageCapabilityComponent;
import com.atlassian.plugins.osgi.test.AtlassianPluginsTestRunner;
import com.atlassian.sal.api.ApplicationProperties;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AtlassianPluginsTestRunner.class)
public class MyComponentWiredTest
{
     private final ApplicationProperties applicationProperties;
     private final ImageCapabilityComponent myPluginComponent;

     public MyComponentWiredTest(ApplicationProperties applicationProperties, ImageCapabilityComponent myPluginComponent)
     {
         this.applicationProperties = applicationProperties;
         this.myPluginComponent = myPluginComponent;
     }

     @Test
     public void testMyName()
     {
         assertEquals("names do not match!", "ImageCapabilityComponent:"
                 + applicationProperties.getDisplayName(),myPluginComponent.getName());
     }
}