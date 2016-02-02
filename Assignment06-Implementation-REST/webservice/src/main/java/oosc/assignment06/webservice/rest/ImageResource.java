package oosc.assignment06.webservice.rest;

import oosc.assignment06.webservice.domain.Blueprint;
import oosc.assignment06.webservice.domain.Comment;
import oosc.assignment06.webservice.domain.Image;
import oosc.assignment06.webservice.domain.ModerationStatus;
import oosc.assignment06.webservice.domain.SpaceMetrics;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import oosc.assignment06.webservice.persistence.SqliteStore;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Assignment 06.2 - REST resource.
 */
@Path("/")
public class ImageResource {
    private final List<Image> data = new ArrayList<Image>();
    private final AtomicInteger blueprintIds = new AtomicInteger(1);
    private final AtomicInteger commentIds = new AtomicInteger(1);
    private SqliteStore store;

    @PostConstruct
    public void setup() throws Exception {
        // Seed images similar to the reference jar
        data.add(new Image("Image", new URL("https://picsum.photos/400/200"), 0));
        data.add(new Image("Image", new URL("https://picsum.photos/640/480"), 0));
        store = new SqliteStore("data/swcarchitect.db");
        store.init();
    }

    @PreDestroy
    public void shutdown() {
        // nothing to close for SQLite (connections are per call)
    }

    @GET
    @Path("info")
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public String info() {
        return "Everything fine";
    }

    @GET
    @Path("images")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<Image> getAllImages() {
        return data;
    }

    @GET
    @Path("blueprints")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<Blueprint> getBlueprints() {
        try {
            return store.listBlueprints();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Blueprint>();
        }
    }

    @POST
    @Path("blueprints")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Blueprint publishBlueprint(Blueprint blueprint) {
        if (blueprint == null) {
            return null;
        }
        try {
            if (blueprint.getId() == 0) {
                blueprint.setId(blueprintIds.getAndIncrement());
            }
            SpaceMetrics metrics = calculateSpace(blueprint.getContent());
            blueprint.setUsableArea(metrics.getUsableArea());
            blueprint.setUsedArea(metrics.getUsedArea());
            blueprint.setFreeArea(metrics.getFreeArea());
            Blueprint saved = store.insertBlueprint(blueprint);
            store.upsertModeration(new ModerationStatus(saved.getId(), "pending"));
            return saved;
        } catch (Exception e) {
            e.printStackTrace();
            return blueprint;
        }
    }

    @GET
    @Path("comments")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<Comment> getComments() {
        try {
            return store.listComments();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Comment>();
        }
    }

    @POST
    @Path("comments")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public Comment addComment(Comment comment) {
        if (comment == null) {
            return null;
        }
        try {
            if (comment.getId() == 0) {
                comment.setId(commentIds.getAndIncrement());
            }
            return store.insertComment(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return comment;
        }
    }

    @GET
    @Path("moderation")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<ModerationStatus> getModeration() {
        try {
            return store.listModeration();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<ModerationStatus>();
        }
    }

    @POST
    @Path("moderation/{blueprintId}/approve")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ModerationStatus approveBlueprint(@PathParam("blueprintId") int blueprintId) {
        return updateModeration(blueprintId, "approved");
    }

    @POST
    @Path("moderation/{blueprintId}/reject")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ModerationStatus rejectBlueprint(@PathParam("blueprintId") int blueprintId) {
        return updateModeration(blueprintId, "rejected");
    }

    @GET
    @Path("blueprints/{id}/space")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SpaceMetrics> getBlueprintSpace(@PathParam("id") int blueprintId) {
        try {
            List<Blueprint> all = store.listBlueprints();
            for (Blueprint bp : all) {
                if (bp.getId() == blueprintId) {
                    SpaceMetrics metrics = new SpaceMetrics(bp.getId(), bp.getUsableArea(), bp.getUsedArea(), bp.getFreeArea());
                    return ResponseEntity.ok(metrics);
                }
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GET
    @Path("blueprints/space")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<SpaceMetrics> getAllBlueprintSpaces() {
        List<SpaceMetrics> result = new ArrayList<SpaceMetrics>();
        try {
            List<Blueprint> all = store.listBlueprints();
            for (Blueprint bp : all) {
                result.add(new SpaceMetrics(bp.getId(), bp.getUsableArea(), bp.getUsedArea(), bp.getFreeArea()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @GET
    @Path("images/{index}")
    public ResponseEntity<InputStreamResource> showImage(@PathParam("index") int index) {
        if (index < 0 || index >= data.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Image img = data.get(index);
        try {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(new InputStreamResource(img.getLocation().openStream()));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @POST
    @Path("images")
    @Consumes(MediaType.TEXT_PLAIN_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<Image> addImageFromBody(String url) {
        try {
            addImage(new URL(url));
        } catch (Exception e) {
            // ignore invalid URLs in this minimal example
            e.printStackTrace();
        }
        return data;
    }

    @POST
    @Path("images/add")
    @Consumes("*/*")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public List<Image> addImageByQuery(@QueryParam("url") String url) {
        if (url == null) {
            return data;
        }
        try {
            addImage(new URL(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public void addImage(URL url) {
        data.add(new Image("New Image", url, 1));
    }

    public void checkImage(Image image) throws IOException {
        double max = 100000.0d;
        URLConnection con = image.getLocation().openConnection();
        double size = (double) con.getContentLength();
        if (size > max) {
            image.rejectImage();
        }
    }

    private ModerationStatus updateModeration(int blueprintId, String status) {
        try {
            return store.upsertModeration(new ModerationStatus(blueprintId, status));
        } catch (Exception e) {
            e.printStackTrace();
            return new ModerationStatus(blueprintId, status);
        }
    }

    private SpaceMetrics calculateSpace(String content) {
        if (content == null || content.trim().isEmpty()) {
            return new SpaceMetrics(0, 0, 0, 0);
        }
        try {
            JSONObject root = new JSONObject(content);
            JSONObject room = root.getJSONObject("room");
            double width = room.getDouble("width");
            double height = room.getDouble("height");
            double usable = width * height;
            double used = 0.0;
            if (root.has("furniture")) {
                JSONArray furniture = root.getJSONArray("furniture");
                for (int i = 0; i < furniture.length(); i++) {
                    JSONObject item = furniture.getJSONObject(i);
                    used += item.getDouble("width") * item.getDouble("height");
                }
            }
            double free = Math.max(0.0, usable - used);
            return new SpaceMetrics(0, usable, used, free);
        } catch (Exception e) {
            e.printStackTrace();
            return new SpaceMetrics(0, 0, 0, 0);
        }
    }
}
