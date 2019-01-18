import java.util.ArrayList;

public class DataSet {
    private ArrayList<Point> points;
    private ArrayList<Point> convexHull;
    private Point center;

    public DataSet() {
        points = new ArrayList<>();
        convexHull = new ArrayList<>();
    }

    public void add(Point p) {
        points.add(p);
    }

    public void calculateCenter() {
        int sumX = 0, sumY = 0;
        for (Point p : points) {
            sumX += p.getX();
            sumY += p.getY();
        }
        center = new Point(sumX / points.size(), sumY / points.size());
    }
}
