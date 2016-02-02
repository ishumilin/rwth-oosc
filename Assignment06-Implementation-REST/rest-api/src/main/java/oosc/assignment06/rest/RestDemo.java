package oosc.assignment06.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Minimal REST-like workflow stub without external dependencies.
 */
public class RestDemo {
    public static void main(String[] args) {
        ImageService service = new ImageService();
        service.addImage(new Image("img1", ImageStatus.PENDING));
        service.approve("img1");
        System.out.println("Approved: " + service.getApproved().size());
    }

    public enum ImageStatus { APPROVED, PENDING, REJECTED }

    public static final class Image {
        private final String id;
        private ImageStatus status;

        public Image(String id, ImageStatus status) {
            this.id = id;
            this.status = status;
        }

        public String getId() {
            return id;
        }

        public ImageStatus getStatus() {
            return status;
        }

        public void setStatus(ImageStatus status) {
            this.status = status;
        }
    }

    public static final class ImageService {
        private final List<Image> images = new ArrayList<Image>();

        public void addImage(Image image) {
            images.add(image);
        }

        public void approve(String id) {
            for (Image img : images) {
                if (img.getId().equals(id)) {
                    img.setStatus(ImageStatus.APPROVED);
                }
            }
        }

        public List<Image> getApproved() {
            List<Image> approved = new ArrayList<Image>();
            for (Image img : images) {
                if (img.getStatus() == ImageStatus.APPROVED) {
                    approved.add(img);
                }
            }
            return approved;
        }
    }
}