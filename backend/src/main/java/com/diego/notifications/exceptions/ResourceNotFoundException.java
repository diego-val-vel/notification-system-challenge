package com.diego.notifications.exceptions;

/**
 * Exception thrown when a required domain resource cannot be found.
 *
 * <p>This exception is used by the service layer to fail fast when catalog
 * values or persisted entities required for the notification flow do not
 * exist in the database.</p>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Creates a new resource not found exception.
     *
     * @param message detailed error message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
