import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MyDrawPanel extends JPanel {

    private final List<Cell> cells;
    private final int gridSize;

    public MyDrawPanel(List<Cell> cells, int gridSize) {
        this.gridSize = gridSize;
        int size = 16 * 64;

        int correct = size % gridSize;
        setPreferredSize(new Dimension(size, size));
        requestFocus();
        this.cells = cells;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cellSizeX = getWidth() / gridSize + 1;
        int cellSizeY = getHeight() / gridSize + 1;

        cells.forEach(cell -> {
            g.drawImage(cell.getPiece().getBufferedImage(), cell.getXCord() * cellSizeX, cell.getYCord() * cellSizeY, cellSizeX, cellSizeY, null);
        });


    }
}
