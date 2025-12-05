package pl.blokaj.dbms.model.error;


/**
 * Generic error object
 */
public class Error {

    /** Error message */
    public String message;

    // Default constructor
    public Error() {}

    // Convenience constructor
    public Error(String message) {
        this.message = message;
    }

    // Optional getters and setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

