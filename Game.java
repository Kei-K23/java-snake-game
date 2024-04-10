import java.util.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.image.*;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.*;

enum GameStatus {

}

public class Game extends JPanel {
    private Timer timer;
    private Snake snake;
    private Food food;
    private int score = 0;
    private int bestScore = 0;
    private BufferedImage foodImage;
    private GameStatus status;

    // constants variables
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final int SPEED = 50;
    private static final Font FONT_M = new Font("MV Boli", Font.PLAIN, 24);
    private static final Font FONT_M_ITALIC = new Font("MV Boli", Font.ITALIC, 24);
    private static final Font FONT_L = new Font("MV Boli", Font.PLAIN, 85);
    private static final Font FONT_XL = new Font("MV Boli", Font.PLAIN, 150);

    // constructor
    public Game() {
        loadFoodImage();
        initializeGame();
    }

    // load food image
    private void loadFoodImage() {
        try {
            foodImage = ImageIO.read(new File("apple.png"));
        } catch (IOException e) {
            foodImage = null;
            System.out.println("Error loading food image: " + e.getMessage());
        }
    }

    // initialize game
    private void initializeGame() {
        addKeyListener(new GameKeyListener());
        setFocusable(true);
        setBackground(new Color(130, 205, 71));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);
        startNewGame();
    }

    private void startNewGame() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
    }

    // inner class for Game key event
    private class GameKeyListener extends KeyAdapter {

    }
}