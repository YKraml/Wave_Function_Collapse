import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ImageLoader {


    private final String[] fileNames;

    public ImageLoader(String[] fileNames) {
        this.fileNames = fileNames;

    }

    public Collection<BufferedImage> loadImages() throws IOException {

        Collection<BufferedImage> images = new ArrayList<>();

        for (String fileName : fileNames) {
            BufferedImage image1 = ImageIO.read(new File(fileName));
            BufferedImage image2 = rotateImageByDegrees(image1, 90);
            BufferedImage image3 = rotateImageByDegrees(image1, 180);
            BufferedImage image4 = rotateImageByDegrees(image1, 270);

            images.add(image1);
            images.add(image2);
            images.add(image3);
            images.add(image4);
        }

        deleteDuplicates(images);

        return images;

    }

    public BufferedImage rotateImageByDegrees(BufferedImage img, double angle) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, img.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w / 2d, h / 2d);
        graphic.drawImage(img, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

    private void deleteDuplicates(Collection<BufferedImage> images) {

        Iterator<BufferedImage> iterator = images.iterator();
        while (iterator.hasNext()) {
            BufferedImage next = iterator.next();
            boolean toRemoved = false;
            for (BufferedImage image : images) {
                if (next != image && compareImages(next, image)) {
                    toRemoved = true;
                }
            }
            if (toRemoved) {
                iterator.remove();
            }
        }


    }

    public boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }
}
