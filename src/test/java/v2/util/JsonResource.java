package v2.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class JsonResource {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonResource() {
    }

    public static Map<String, Object> readAsMap(String classpathFile) {
        try (InputStream inputStream = JsonResource.class.getClassLoader().getResourceAsStream(classpathFile)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("resource not found: " + classpathFile);
            }
            return OBJECT_MAPPER.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new IllegalStateException("failed to parse resource: " + classpathFile, e);
        }
    }
}
