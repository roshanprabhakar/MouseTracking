import processing.core.PApplet;

import java.util.ArrayList;

public class MouseTracking implements PixelFilter {

    int totalTimePassed = 0; //keeps track of the time

    private ArrayList<Point> centers = new ArrayList<>(); //each frame adds a center; indexes same as frame sequence
    private ArrayList<Double> times = new ArrayList<>();

    private double fieldRadius = 202;
    private Point centerOfField = new Point(308, 241); //center

    private short[][] bwpixels; //black and white image of mouse in field

    private Point centerOfMouse;
    private ArrayList<Point> mouse = new ArrayList<>(); //All points in the mouse

    public DataSet MOUSE_DATA = new DataSet();

    @Override
    public DImage processImage(DImage img) {
        long startTime = System.currentTimeMillis();
        outlineMouse(img);
        long endTime = System.currentTimeMillis();
        totalTimePassed += endTime - startTime;
        return img;
    }

    public void outlineMouse(DImage img) {
        long start = System.currentTimeMillis();
        short[][] bwpixels = applyColorThreshholding(img, 102);
        findMouse(bwpixels);
        applyRadiusFilter(bwpixels, centerOfField, fieldRadius);
        this.bwpixels = bwpixels;
        centerOfMouse = centerOfMouse(bwpixels);
        centers.add(centerOfMouse);
        MOUSE_DATA.addCenter(centerOfMouse.clone());
        img.setPixels(bwpixels); //doesn't have to be included, can be removed in order to see a colored view with field boundaries
        mouse.clear();
        long end = System.currentTimeMillis();
        times.add((double)end - start);
    }

    private void findMouse(short[][] bwpixels) { //run this right after you run color mask
        for (int r = 0; r < bwpixels.length; r++) {
            for (int c = 0; c < bwpixels[r].length; c++) {
                if (bwpixels[r][c] == 255) {
                    mouse.add(new Point(c, r));
                }
            }
        }
    }

    public Point centerOfMouse(short[][] bwpixels) {
        int sumRow = 0;
        int sumCol = 0;
        int count = 0;
        for (int r = 0; r < bwpixels.length; r++) {
            for (int c = 0; c < bwpixels[r].length; c++) {
                if (bwpixels[r][c] == 0) {
                    sumRow += r;
                    sumCol += c;
                    count++;
                }
            }
        }
        sumRow /= count;
        sumCol /= count;
        return new Point(sumCol, sumRow);
    }

    private void applyRadiusFilter(short[][] bwpixels, Point center, double radius) {
        for (int r = 0; r < bwpixels.length; r++) {
            for (int c = 0; c < bwpixels[r].length; c++) {
                Point p = new Point(c, r);
                if (p.distanceTo(center) >= radius) {
                    bwpixels[r][c] = 255;
                }
            }
        }
    }

    private short[][] applyColorThreshholding(DImage img, int threshhold) {
        short[][] bwpixels = img.getBWPixelGrid();
        for (int r = 0; r < img.getHeight(); r++) {
            for (int c = 0; c < img.getWidth(); c++) {
                if (bwpixels[r][c] < threshhold) {
                    bwpixels[r][c] = 0;
                } else {
                    bwpixels[r][c] = 255;
                }
            }
        }
        return bwpixels;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        if (centerOfMouse == null || bwpixels == null) return;
        centerOfMouse = centerOfMouse(bwpixels);

        for (int i = 0; i < centers.size() - 1; i++) {
            window.stroke(255, 0, 0);
            window.line(centers.get(i).getX(), centers.get(i).getY(), centers.get(i + 1).getX(), centers.get(i + 1).getY());
        }

        window.ellipse(centerOfMouse.getX(), centerOfMouse.getY(), 5, 5);
    }
}