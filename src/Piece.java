import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Piece {


    private final BufferedImage bufferedImage;

    private final int[] northColors;
    private final int[] eastColors;
    private final int[] southColors;
    private final int[] westColors;

    private final Collection<Piece> northPossibilities;
    private final Collection<Piece> eastPossibilities;
    private final Collection<Piece> southPossibilities;
    private final Collection<Piece> westPossibilities;


    public Piece(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;

        northColors = new int[bufferedImage.getWidth()];
        eastColors = new int[bufferedImage.getHeight()];
        southColors = new int[bufferedImage.getWidth()];
        westColors = new int[bufferedImage.getHeight()];


        initSides();

        northPossibilities = new ArrayList<>();
        eastPossibilities = new ArrayList<>();
        southPossibilities = new ArrayList<>();
        westPossibilities = new ArrayList<>();
    }

    private void initSides() {

        for (int i = 0; i < northColors.length; i++) {
            northColors[i] = bufferedImage.getRGB(i, 0);
        }

        for (int i = 0; i < eastColors.length; i++) {
            eastColors[i] = bufferedImage.getRGB(bufferedImage.getWidth() - 1, i);
        }

        for (int i = 0; i < southColors.length; i++) {
            southColors[i] = bufferedImage.getRGB(i, bufferedImage.getHeight() - 1);
        }

        for (int i = 0; i < westColors.length; i++) {
            westColors[i] = bufferedImage.getRGB(0, i);
        }

    }

    public void initPossibleNeighbors(Collection<Piece> pieces) {

        for (Piece piece : pieces) {

            if (Arrays.equals(piece.southColors, northColors)) {
                northPossibilities.add(piece);
            }

            if (Arrays.equals(piece.eastColors, westColors)) {
                westPossibilities.add(piece);
            }

            if (Arrays.equals(piece.northColors, southColors)) {
                southPossibilities.add(piece);
            }

            if (Arrays.equals(piece.westColors, eastColors)) {
                eastPossibilities.add(piece);
            }

        }


    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public Collection<Piece> getNorthPossibilities() {
        return northPossibilities;
    }

    public Collection<Piece> getEastPossibilities() {
        return eastPossibilities;
    }

    public Collection<Piece> getSouthPossibilities() {
        return southPossibilities;
    }

    public Collection<Piece> getWestPossibilities() {
        return westPossibilities;
    }
}
