package ut.com.atlassian.bamboo.plugins.imagecapability;

import com.atlassian.bamboo.plugins.imagecapability.api.ImageCapabilityComponent;
import com.atlassian.bamboo.plugins.imagecapability.impl.ImageCapabilityComponentImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ImageCapabilityTest {
    @Test
    public void testName() {
        ImageCapabilityComponent component = new ImageCapabilityComponentImpl(null);
        assertEquals("names do not match!", "ImageCapabilityComponent", component.getName());
    }
}