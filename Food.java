
public class Food {
    private int x;
    private int y;

    public Food(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Food(Food food) {
        this.x = food.getX();
        this.y = food.getY();
    }

    public void moveTo(Direction direction, int v) {
        switch (direction) {
            case UP:
                this.y -= v;
                break;
            case DOWN:
                this.y += v;
                break;
            case LEFT:
                this.x -= v;
                break;
            case RIGHT:
                this.x += v;
                break;
            default:
                break;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean intersects(Food food, int tolerance) {
        int diffX = Math.abs(this.x - food.getX());
        int diffY = Math.abs(this.y - food.getY());

        return this.equals(food) || (diffX <= tolerance && diffY <= tolerance);
    }

    public boolean equals(Food food) {
        return this.x == food.getX() && this.y == food.getY();
    }
}
