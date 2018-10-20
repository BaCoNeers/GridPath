package com.baconeers;


import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;


class Grid {

    class Pair {
        final int row;
        final int col;

        Pair(int row, int col) {
            // Restrict Rows and Cols to the size of the Grid
            this.row = row < 0 ? 0 : (row >= Grid.Rows) ? Grid.Rows : row;
            this.col = col;
        }

        boolean equals(Pair other) {
            return other != null && (other.row == this.row && other.col == this.col);
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof Pair) && equals((Pair) o);
        }

        @Override
        public int hashCode() {
            return (row << 16) & col;
        }
    }

    private static final int DEFAULT_ROWS = 20;
    private static final int DEFAULT_COLS = 20;
    private static final int PIXELS_PER_UNIT = 20;

    static int Rows = DEFAULT_ROWS;
    static int Cols = DEFAULT_COLS;
    static int PPu = PIXELS_PER_UNIT;

    final Vector<Grid.Pair> obstacles = new Vector<>();
    final Vector<Grid.Pair> path = new Vector<>();

    Grid(int rows, int cols, int pixels_per_unit) {
        Rows = rows;
        Cols = cols;
        PPu = pixels_per_unit;
    }

    void addObstacle(int row, int col) {
        obstacles.add(new Pair(row, col));
    }

    public void clearObstacles() {
        obstacles.clear();
    }

    void addToPath(int row, int col) {
        path.add(new Pair(row, col));
    }

    public void clearPath() {
        path.clear();
    }
}

class Surface extends JPanel {

    private final Grid m_grid;

    Surface(Grid grid) {
        m_grid = grid;

        Dimension gridDimension = new Dimension(Grid.Cols * Grid.PPu + 1, Grid.Rows * Grid.PPu + 1);
        setSize(gridDimension);
        setMinimumSize(gridDimension);
        setPreferredSize(gridDimension);
    }

    private void drawBackgroundGrid(Graphics2D g2d) {
        g2d.setColor(Color.LIGHT_GRAY);
        // Draw the row grid lines
        int width = Grid.Cols * Grid.PPu + 1;
        for (int i = 0; i <= Grid.Rows; i++) {
            g2d.drawLine(0, i * Grid.PPu, width, i * Grid.PPu);
        }

        // Draw the column lines
        int height = Grid.Rows * Grid.PPu + 1;
        for (int i = 0; i <= Grid.Cols; i++) {
            g2d.drawLine(i * Grid.PPu, 0, i * Grid.PPu, height);
        }
    }

    private void drawObstacle(Graphics2D g2d, Grid.Pair p) {
        g2d.fill(new RoundRectangle2D.Double((p.row * Grid.PPu) + 2, (p.col * Grid.PPu) + 2,
                Grid.PPu - 4, Grid.PPu - 4, 2, 2));

    }

    private void drawPoint(Graphics2D g2d, Grid.Pair p) {
        int radius = 6;
        g2d.fillOval((p.row * Grid.PPu) + (Grid.PPu / 2) - radius,
                (p.col * Grid.PPu) + (Grid.PPu / 2) - radius,
                radius * 2, radius * 2);
    }

    private void drawLine(Graphics2D g2d, Grid.Pair p1, Grid.Pair p2) {
        g2d.drawLine((p1.row * Grid.PPu) + (Grid.PPu / 2), (p1.col * Grid.PPu) + (Grid.PPu / 2),
                (p2.row * Grid.PPu) + (Grid.PPu / 2), (p2.col * Grid.PPu) + (Grid.PPu / 2));
    }

    private void drawObstacles(Graphics2D g2d) {
        g2d.setColor(Color.PINK);
        g2d.setBackground(Color.PINK);

        for (Grid.Pair pair : m_grid.obstacles) {
            drawObstacle(g2d, pair);
        }

    }

    private void drawPath(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.setBackground(Color.BLUE);


        Grid.Pair pair = m_grid.path.firstElement();
        drawPoint(g2d, pair);
        for (Grid.Pair pair2 : m_grid.path) {
            drawPoint(g2d, pair2);
            drawLine(g2d, pair, pair2);
            pair = pair2;
        }

    }

    private void doDrawing(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        drawBackgroundGrid(g2d);
        drawObstacles(g2d);
        drawPath(g2d);
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }
}

public class Main extends JFrame {

    private Main() {

        Grid grid = new Grid(20, 20, 40);

        grid.addObstacle(2, 2);
        grid.addObstacle(2, 3);
        grid.addObstacle(2, 4);
        grid.addObstacle(3, 2);
        grid.addObstacle(3, 3);
        grid.addObstacle(3, 4);

        grid.addToPath(10, 10);
        grid.addToPath(8, 10);
        grid.addToPath(6, 9);
        grid.addToPath(5, 4);
        grid.addToPath(4, 1);
        grid.addToPath(0, 0);

        initUI(grid);
    }

    private void initUI(Grid grid) {

        setLayout(new BorderLayout());
        getContentPane().add(new Surface(grid), BorderLayout.CENTER);
        setTitle("Grid Path");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            Main ex = new Main();
            ex.setVisible(true);
        });
    }
}