package com.lukecheskin.classes;

/**
 * Enumeration representing the build status of a LEGO set.
 * Possible values are NOT_STARTED, IN_PROGRESS, and COMPLETED.
 */
public enum Status {
    COMPLETED,
    IN_PROGRESS,
    NOT_STARTED;

    @Override
    public String toString() {
        switch (this) {
            case COMPLETED:
                return "Completed";
            case IN_PROGRESS:
                return "In Progress";
            case NOT_STARTED:
                return "Not Started";
            default:
                return "Unknown";
        }
    }
}
