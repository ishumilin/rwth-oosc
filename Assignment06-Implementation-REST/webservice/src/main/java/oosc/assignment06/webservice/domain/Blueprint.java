package oosc.assignment06.webservice.domain;

/**
 * Minimal blueprint payload for publish-to-web placeholder.
 */
public class Blueprint {
    private int id;
    private String name;
    private String content;
    private double usableArea;
    private double usedArea;
    private double freeArea;

    public Blueprint() {
        // default constructor for JSON
    }

    public Blueprint(int id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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