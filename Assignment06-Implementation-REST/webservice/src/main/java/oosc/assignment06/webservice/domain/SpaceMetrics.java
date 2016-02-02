package oosc.assignment06.webservice.domain;

/**
 * Calculated space metrics for a blueprint.
 */
public class SpaceMetrics {
    private int blueprintId;
    private double usableArea;
    private double usedArea;
    private double freeArea;

    public SpaceMetrics() {
        // default constructor for JSON
    }

    public SpaceMetrics(int blueprintId, double usableArea, double usedArea, double freeArea) {
        this.blueprintId = blueprintId;
        this.usableArea = usableArea;
        this.usedArea = usedArea;
        this.freeArea = freeArea;
    }

    public int getBlueprintId() {
        return blueprintId;
    }

    public void setBlueprintId(int blueprintId) {
        this.blueprintId = blueprintId;
    }

    public double getUsableArea() {
        return usableArea;
    }

    public void setUsableArea(double usableArea) {
        this.usableArea = usableArea;
    }

    public double getUsedArea() {
        return usedArea;
    }

    public void setUsedArea(double usedArea) {
        this.usedArea = usedArea;
    }

    public double getFreeArea() {
        return freeArea;
    }

    public void setFreeArea(double freeArea) {
        this.freeArea = freeArea;
    }
}