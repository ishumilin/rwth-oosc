package oosc.assignment06.webservice.domain;

/**
 * Minimal moderation status for a blueprint.
 */
public class ModerationStatus {
    private int blueprintId;
    private String status; // pending, approved, rejected

    public ModerationStatus() {
        // default constructor for JSON
    }

    public ModerationStatus(int blueprintId, String status) {
        this.blueprintId = blueprintId;
        this.status = status;
    }

    public int getBlueprintId() {
        return blueprintId;
    }

    public void setBlueprintId(int blueprintId) {
        this.blueprintId = blueprintId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}