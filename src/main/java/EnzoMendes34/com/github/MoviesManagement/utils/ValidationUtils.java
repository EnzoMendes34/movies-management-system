package EnzoMendes34.com.github.MoviesManagement.utils;

import EnzoMendes34.com.github.MoviesManagement.exceptions.NullObjectException;

import java.util.Map;

public class ValidationUtils {

    public static void validateRequiredFields(Map<String, Object> fields) {
        if (fields == null || fields.isEmpty()){
            throw new NullObjectException("Fields map must not be null or empty");
        }

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            Object value = entry.getValue();
            if (value == null || (value instanceof String str && str.isBlank())) {
                throw new NullObjectException(entry.getKey() + " must not be null or Empty.");
            }
        }
    }
}