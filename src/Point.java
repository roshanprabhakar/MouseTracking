public class Point {

    private int x;
    private int y;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void randomStep() {
        int[] choices = {5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5};
        this.x += choices[(int) (Math.random() * choices.length)];
        this.y += choices[(int) (Math.random() * choices.length)];
    }

    public String toString() {
        return x + " " + y;
    }

    public double distanceTo(Point other) {
        int oneSegment = (this.x - other.x) * (this.x - other.x);
        int otherSegment = (this.y - other.y) * (this.y - other.y);
        return Math.sqrt(oneSegment + otherSegment);
    }
}
