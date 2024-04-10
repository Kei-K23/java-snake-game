import java.util.*;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.*;

enum GameStatus {
    NOT_STARTED,
    RUNNING,
    PAUSED,
    GAME_OVER
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
    public static final int WIDTH = 760;
    public static final int HEIGHT = 520;
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
        repaint();

        requestFocusInWindow();
    }

    private void startNewGame() {
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        status = GameStatus.NOT_STARTED;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
        Toolkit.getDefaultToolkit().sync();
    }

    private void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setFont(FONT_M);

        if (status == GameStatus.NOT_STARTED) {
            drawCenteredString(g2d, "SNAKE", FONT_XL, 200);
            drawCenteredString(g2d, "GAME", FONT_XL, 300);
            drawCenteredString(g2d, "Press any key to begin", FONT_M_ITALIC, 330);
        } else {
            renderGame(g2d);
        }
    }

    private void renderGame(Graphics2D g2d) {
        snake.render(g2d);

        g2d.drawString("SCORES: " + String.format("%02d", score), 20, 30);

        g2d.drawString("BEST: " + String.format("%02d", bestScore), 630, 30);

        if (food != null) {
            if (foodImage != null) {
                g2d.drawImage(foodImage, food.getX(), food.getY(), 60, 60, null);
            } else {
                g2d.setColor(Color.RED);
                g2d.fillRect(food.getX(), food.getY(), 60, 60);
            }
        }

        if (status == GameStatus.GAME_OVER) {
            drawCenteredString(g2d, "GAME OVER", FONT_M_ITALIC, 200);
            drawCenteredString(g2d, "Press any key to restart", FONT_L, 330);
        }

        if (status == GameStatus.PAUSED) {
            drawCenteredString(g2d, "PAUSED", FONT_L, 200);
        }
    }

    private void drawCenteredString(Graphics g, String text, Font font, int y) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (WIDTH - metrics.stringWidth(text)) / 2;
        g.setFont(font);
        g.drawString(text, x, y);
    }

    private void togglePause() {
        setStatus(status == GameStatus.PAUSED ? GameStatus.RUNNING : GameStatus.PAUSED);
    }

    private void setStatus(GameStatus newStatus) {
        switch (newStatus) {
            case RUNNING:
                startGameLoop();
                break;

            case PAUSED:
            case GAME_OVER:
                stopGameLoop();
                if (newStatus == GameStatus.GAME_OVER) {
                    bestScore = Math.max(score, bestScore);
                }
                break;

            default:
                break;
        }
        status = newStatus;
    }

    private void startGameLoop() {
        timer = new Timer();
        timer.schedule(new GameLoop(), 0, SPEED);
    }

    private void stopGameLoop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void spawnFood() {
        food = new Food((int) (Math.random() * (WIDTH - 60)) + 20, (int) (Math.random() * (HEIGHT - 60)) + 40);
    }

    private void update() {
        snake.move();

        if (food != null && snake.getHead().intersects(food, 20)) {
            snake.addTail();
            food = null;
            score++;
        }

        if (food == null) {
            spawnFood();
        }

        checkForGameOver();
    }

    private void checkForGameOver() {
        if (snake.collidesWithBoundary(WIDTH, HEIGHT) || snake.collidesWithItself()) {
            setStatus(GameStatus.GAME_OVER);
        }
    }

    // inner class for Game key event

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (status == GameStatus.RUNNING) {
                handleRunningKeyPress(key);
            } else if (status == GameStatus.NOT_STARTED) {
                setStatus(GameStatus.RUNNING);
            } else if (status == GameStatus.GAME_OVER) {
                startNewGame();
            }

            if (key == KeyEvent.VK_P) {
                togglePause();
            }
        }
    }

    private void handleRunningKeyPress(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                snake.turn(Direction.LEFT);
                break;

            case KeyEvent.VK_RIGHT:
                snake.turn(Direction.RIGHT);
                break;

            case KeyEvent.VK_UP:
                snake.turn(Direction.UP);
                break;

            case KeyEvent.VK_DOWN:
                snake.turn(Direction.DOWN);
                break;

            case KeyEvent.VK_SPACE:
                if (status == GameStatus.PAUSED) {
                    togglePause();
                }
                break;
            default:
                break;
        }
    }

    private class GameLoop extends TimerTask {
        @Override
        public void run() {
            update();
            repaint();
        }
    }
}