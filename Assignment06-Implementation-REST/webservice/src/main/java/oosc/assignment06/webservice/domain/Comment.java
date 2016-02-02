package oosc.assignment06.webservice.domain;

/**
 * Minimal comment domain object.
 */
public class Comment {
    private int id;
    private int blueprintId;
    private String author;
    private String text;
    private boolean flagged;

    public Comment() {
        // default constructor for JSON
    }

    public Comment(int id, int blueprintId, String author, String text, boolean flagged) {
        this.id = id;
        this.blueprintId = blueprintId;
        this.author = author;
        this.text = text;
        this.flagged = flagged;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlueprintId() {
        return blueprintId;
    }

    public void setBlueprintId(int blueprintId) {
        this.blueprintId = blueprintId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}