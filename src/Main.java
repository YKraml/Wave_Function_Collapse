import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class Main {

    private final static int GRID_SIZE = 64;

    private static final String[] FILE_NAMES = new String[]{
            "tiles/lines/1.png",
            "tiles/lines/2.png",
            "tiles/lines/3.png",
            "tiles/lines/4.png",
            "tiles/lines/5.png"
    };

    /*
    private static final String[] FILE_NAMES = new String[]{
            "tiles/circuit/1.png",
            "tiles/circuit/2.png",
            "tiles/circuit/3.png",
            "tiles/circuit/4.png",
            "tiles/circuit/5.png",
            "tiles/circuit/6.png",
            "tiles/circuit/7.png",
            "tiles/circuit/8.png",
            "tiles/circuit/9.png",
            "tiles/circuit/10.png",
            "tiles/circuit/11.png",
            "tiles/circuit/12.png",
            "tiles/circuit/13.png",
            "tiles/circuit/14.png",
            "tiles/circuit/15.png",
            //"tiles/circuit/16.png",
            "tiles/circuit/17.png",
            "tiles/circuit/18.png",
            //"tiles/circuit/19.png",
            "tiles/circuit/20.png",
            "tiles/circuit/21.png",
    };
     */


    public static void main(String[] args) throws IOException, InterruptedException {

        Collection<BufferedImage> images = new ImageLoader(FILE_NAMES).loadImages();
        int pieceSize = images.iterator().next().getHeight();

        Collection<Piece> pieces = new ArrayList<>();
        images.forEach(bufferedImage -> pieces.add(new Piece(bufferedImage)));
        pieces.forEach(piece -> piece.initPossibleNeighbors(pieces));


        Map<Point, Cell> cells = getPointCellMap(pieces);

        MyDrawPanel myDrawPanel = new MyDrawPanel(new LinkedList<>(cells.values()), GRID_SIZE);
        MyFrame myFrame = new MyFrame(myDrawPanel);
        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1000 / 24);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                myDrawPanel.repaint();
            }
        }).start();

        Set<Cell> cellsToCheck = new HashSet<>();

        for (int i = 0; i < GRID_SIZE * GRID_SIZE; i++) {

            Thread.sleep((long) (1000 / GRID_SIZE / Math.log(GRID_SIZE)));

            Cell cellToCollapse = findCellToCollapse(cells);
            cellToCollapse.collapse();
            cellsToCheck.add(findCell(cells, cellToCollapse.getXCord(), cellToCollapse.getYCord() - 1));
            cellsToCheck.add(findCell(cells, cellToCollapse.getXCord() + 1, cellToCollapse.getYCord()));
            cellsToCheck.add(findCell(cells, cellToCollapse.getXCord(), cellToCollapse.getYCord() + 1));
            cellsToCheck.add(findCell(cells, cellToCollapse.getXCord() - 1, cellToCollapse.getYCord()));

            cellsToCheck.removeIf(Cell::isCollapsed);
            cellsToCheck.parallelStream().forEach(cell -> updatePossibilitiesOfCell(cell, pieces, cells));
        }

        createAndSaveImage(pieceSize, cells);

    }

    private static Map<Point, Cell> getPointCellMap(Collection<Piece> pieces) throws IOException {
        Map<Point, Cell> cells = new HashMap<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Cell cell = new Cell(i, j);
                cell.setPossibilities(new LinkedList<>(pieces));
                cells.put(new Point(cell.getXCord(), cell.getYCord()), cell);
            }
        }
        return cells;
    }

    private static void createAndSaveImage(int pieceSize, Map<Point, Cell> cells) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(GRID_SIZE * pieceSize, GRID_SIZE * pieceSize, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        cells.values().forEach(cell -> g.drawImage(cell.getPiece().getBufferedImage(), cell.getXCord() * pieceSize, cell.getYCord() * pieceSize, null));
        g.dispose();
        ImageIO.write(bufferedImage, "png", new File("created.png"));
    }


    private static Cell findCellToCollapse(Map<Point, Cell> cells) {
        Cell cellToCollapse = cells.values().stream()
                .filter(cell -> !cell.isCollapsed())
                .filter(cell -> cell.getPossibilities().size() != 0)
                .min(Comparator.comparingInt(o -> o.getPossibilities().size()))
                .get();
        return cellToCollapse;
    }

    private static void updatePossibilitiesOfCell(Cell cell, Collection<Piece> pieces, Map<Point, Cell> cells) {
        List<Piece> intersection = new LinkedList<>(pieces);

        Cell northCell = findCell(cells, cell.getXCord(), cell.getYCord() - 1);
        Cell eastCell = findCell(cells, cell.getXCord() + 1, cell.getYCord());
        Cell southCell = findCell(cells, cell.getXCord(), cell.getYCord() + 1);
        Cell westCell = findCell(cells, cell.getXCord() - 1, cell.getYCord());

        if (northCell.isCollapsed()) {
            intersection.retainAll(northCell.getPiece().getSouthPossibilities());
        }
        if (eastCell.isCollapsed()) {
            intersection.retainAll(eastCell.getPiece().getWestPossibilities());
        }
        if (southCell.isCollapsed()) {
            intersection.retainAll(southCell.getPiece().getNorthPossibilities());
        }
        if (westCell.isCollapsed()) {
            intersection.retainAll(westCell.getPiece().getEastPossibilities());
        }
        cell.setPossibilities(intersection);
    }


    private static Cell findCell(Map<Point, Cell> cells, int xCord, int yCord) {
        return cells.get(new Point((xCord + GRID_SIZE) % GRID_SIZE, (yCord + GRID_SIZE) % GRID_SIZE));
    }


}
