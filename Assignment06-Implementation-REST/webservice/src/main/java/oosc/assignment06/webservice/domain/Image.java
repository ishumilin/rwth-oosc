package oosc.assignment06.webservice.domain;

import java.net.URL;

/**
 * Minimal domain object similar to the reference.
 */
public class Image {
    private String name;
    private URL location;
    private int status; // 0 = pending, 1 = approved, 2 = rejected

    public Image() {
        // default constructor for JSON
    }

    public Image(String name, URL location, int status) {
        this.name = name;
        this.location = location;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getLocation() {
        return location;
    }

    public void setLocation(URL location) {
        this.location = location;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void approveImage() {
        this.status = 1;
    }

    public void rejectImage() {
        this.status = 2;
    }
}
