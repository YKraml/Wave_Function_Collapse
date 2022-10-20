import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Cell {

    private Piece piece;
    private boolean collapsed;
    private final int xCord;
    private final int yCord;

    private List<Piece> possibilities;


    public Cell(int xCord, int yCord) throws IOException {
        piece = new Piece(new BufferedImage(16,16,BufferedImage.TYPE_INT_RGB));
        this.xCord = xCord;
        this.yCord = yCord;
        collapsed = false;
        possibilities = new LinkedList<>();
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public Piece getPiece() {
        return piece;
    }

    public void collapse() {
        if (collapsed) {
            throw new RuntimeException("Cell already collapsed.");
        }
        collapsed = true;
        Collections.shuffle(possibilities);
        piece = possibilities.iterator().next();
    }

    public int getXCord() {
        return xCord;
    }

    public int getYCord() {
        return yCord;
    }

    public void setPossibilities(List<Piece> possibilities) {
        this.possibilities = possibilities;
    }

    public Collection<Piece> getPossibilities() {
        return possibilities;
    }
}
