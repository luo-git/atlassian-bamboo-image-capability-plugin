# To test this plugin
1. Install atlassian SDK and set up the environment paths (java 8 update 181, atlassian maven).
2. Clone this repository.
2. Navigate to the root directory and use `atlas-run`.


## REST API Usage

### GET rest/imagecapabilities/1.0/imageCapabilities
- Lists all image configurations with associated capabilities

### GET rest/imagecapabilities/1.0/imageCapabilities/{id}
- Get image capability of a specific configuration

### POST rest/imagecapabilities/1.0/imageCapabilities/{id}
- Add new image capabilities to a specific configuration
- Example body: `{ "capabilities": [{"key": "system.jdk.JDK 1.5", "value": "/opt/jdk-5"}] }`

### PUT rest/imagecapabilities/1.0/imageCapabilities/{id}
- Update image capabilities of a specific configuration
- Example body: `{ "capabilities": [{"key": "system.jdk.JDK 1.5", "value": "/opt/jdk-5"}] }`

### DELETE rest/imagecapabilities/1.0/imageCapabilities/{id}
- Remove image capabilities from a specific configuration
- Example body: `{ "capabilities": ["system.jdk.JDK 1.5", "system.jdk.JDK 1.6"] }`

