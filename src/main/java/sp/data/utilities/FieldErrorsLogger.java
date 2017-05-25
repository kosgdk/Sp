package sp.data.utilities;

import org.slf4j.Logger;
import org.springframework.validation.Errors;

public class FieldErrorsLogger {

    public static void logErrors(Logger logger, Errors errors){
        logger.debug(errors.getObjectName() + " has errors:");
        if (errors.hasGlobalErrors()) {
            errors.getGlobalErrors().forEach(e -> logger.debug("   * Global error: " + e.getCode()));
        }
        if (errors.hasFieldErrors()){
            errors.getFieldErrors().forEach(e -> logger.debug("   * Field: " + e.getField() + "; Code: " + e.getCode()));
        }
    }
}
