import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public boolean isX() {
        return this == LEFT || this == RIGHT;
    }

    public boolean isY() {
        return this == UP || this == DOWN;
    }
}

public class Snake {
    private Direction direction;
    private Food head;
    private ArrayList<Food> body;

    public Snake(int x, int y) {
        this.head = new Food(x, y);
        this.direction = Direction.RIGHT;
        this.body = new ArrayList<Food>();
        for (int i = 0; i < 3; i++) {
            this.body.add(new Food(0, 0));
        }
    }

    public void move() {
        ArrayList<Food> newBody = new ArrayList<Food>();

        newBody.add(new Food(this.head));
        newBody.addAll(body.subList(0, body.size() - 1));
        body = newBody;
        head.moveTo(direction, 10);
    }

    public void addTail() {
        body.add(new Food(-10, -10));
    }

    public void turn(Direction d) {
        if ((d.isX() && direction.isY()) || (d.isY() && direction.isX())) {
            direction = d;
        }
    }

    public boolean collidesWithBoundary(int width, int height) {
        return head.getX() <= 20 || head.getY() >= width + 10 || head.getY() <= 40 || head.getY() >= height + 30;
    }

    public boolean collidesWithItself() {
        return body.stream().anyMatch(b -> head.equals(b));
    }

    public ArrayList<Food> getBody() {
        return body;
    }

    public Food getHead() {
        return head;
    }

    public void render(Graphics2D g2d) {
        g2d.setColor(new Color(33, 70, 199));
        g2d.fillRect(head.getX(), head.getY(), 10, 10);

        body.forEach(b -> g2d.fillRect(b.getX(), b.getY(), 10, 10));

        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawRect(20, 40, Game.WIDTH, Game.HEIGHT);
    }
}
