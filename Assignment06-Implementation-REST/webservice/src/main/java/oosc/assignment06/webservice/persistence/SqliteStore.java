package oosc.assignment06.webservice.persistence;

import oosc.assignment06.webservice.domain.Blueprint;
import oosc.assignment06.webservice.domain.Comment;
import oosc.assignment06.webservice.domain.ModerationStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple SQLite persistence (single-file DB).
 *
 * This keeps storage lightweight: no ORM, just JDBC.
 */
public class SqliteStore {
    private final String url;

    public SqliteStore(String filePath) {
        this.url = "jdbc:sqlite:" + filePath;
    }

    public void init() throws SQLException {
        Connection conn = getConnection();
        Statement st = conn.createStatement();
        st.executeUpdate("CREATE TABLE IF NOT EXISTS blueprints (id INTEGER PRIMARY KEY, name TEXT, content TEXT, usable_area REAL, used_area REAL, free_area REAL)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS comments (id INTEGER PRIMARY KEY, blueprint_id INTEGER, author TEXT, text TEXT, flagged INTEGER)");
        st.executeUpdate("CREATE TABLE IF NOT EXISTS moderation (blueprint_id INTEGER PRIMARY KEY, status TEXT)");
        st.close();
        conn.close();
    }

    public Blueprint insertBlueprint(Blueprint blueprint) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO blueprints(name, content, usable_area, used_area, free_area) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, blueprint.getName());
        ps.setString(2, blueprint.getContent());
        ps.setDouble(3, blueprint.getUsableArea());
        ps.setDouble(4, blueprint.getUsedArea());
        ps.setDouble(5, blueprint.getFreeArea());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            blueprint.setId(rs.getInt(1));
        }
        rs.close();
        ps.close();
        conn.close();
        return blueprint;
    }

    public List<Blueprint> listBlueprints() throws SQLException {
        Connection conn = getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT id, name, content, usable_area, used_area, free_area FROM blueprints ORDER BY id");
        List<Blueprint> result = new ArrayList<Blueprint>();
        while (rs.next()) {
            Blueprint bp = new Blueprint(rs.getInt("id"), rs.getString("name"), rs.getString("content"));
            bp.setUsableArea(rs.getDouble("usable_area"));
            bp.setUsedArea(rs.getDouble("used_area"));
            bp.setFreeArea(rs.getDouble("free_area"));
            result.add(bp);
        }
        rs.close();
        st.close();
        conn.close();
        return result;
    }

    public Comment insertComment(Comment comment) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO comments(blueprint_id, author, text, flagged) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, comment.getBlueprintId());
        ps.setString(2, comment.getAuthor());
        ps.setString(3, comment.getText());
        ps.setInt(4, comment.isFlagged() ? 1 : 0);
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            comment.setId(rs.getInt(1));
        }
        rs.close();
        ps.close();
        conn.close();
        return comment;
    }

    public List<Comment> listComments() throws SQLException {
        Connection conn = getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT id, blueprint_id, author, text, flagged FROM comments ORDER BY id");
        List<Comment> result = new ArrayList<Comment>();
        while (rs.next()) {
            result.add(new Comment(
                    rs.getInt("id"),
                    rs.getInt("blueprint_id"),
                    rs.getString("author"),
                    rs.getString("text"),
                    rs.getInt("flagged") == 1));
        }
        rs.close();
        st.close();
        conn.close();
        return result;
    }

    public ModerationStatus upsertModeration(ModerationStatus status) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement ps = conn.prepareStatement(
                "INSERT OR REPLACE INTO moderation(blueprint_id, status) VALUES (?, ?)");
        ps.setInt(1, status.getBlueprintId());
        ps.setString(2, status.getStatus());
        ps.executeUpdate();
        ps.close();
        conn.close();
        return status;
    }

    public List<ModerationStatus> listModeration() throws SQLException {
        Connection conn = getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT blueprint_id, status FROM moderation ORDER BY blueprint_id");
        List<ModerationStatus> result = new ArrayList<ModerationStatus>();
        while (rs.next()) {
            result.add(new ModerationStatus(rs.getInt("blueprint_id"), rs.getString("status")));
        }
        rs.close();
        st.close();
        conn.close();
        return result;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}