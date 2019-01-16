import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class RandomWalk implements PixelFilter {

    ArrayList<Point> points;

    public RandomWalk() {
        points = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            points.add(new Point(200,200));
        }
    }

    @Override
    public DImage processImage(DImage img) {
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        for (Point p : points) {
            window.ellipse(p.getY(), p.getX(), 10, 10);
            p.randomStep();
        }
    }
}