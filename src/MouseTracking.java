import processing.core.PApplet;

import java.util.ArrayList;

public class MouseTracking implements PixelFilter {

    int ppcm;
    int fps;
    int framesPassed = 0;
    int totalTimePassed = 0;
    private ArrayList<Point> centers = new ArrayList<>(); //each frame adds a center; indexes same as frame sequence
    private ArrayList<Double> times = new ArrayList<>();

    private Point centerOfMouse;
    Point centerOfField = new Point(308, 241);

    private short[][] bwpixels;
    private DataSet mouse = new DataSet();

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setPpcm(int ppcm) {
        this.ppcm = ppcm;
    }

    @Override
    public DImage processImage(DImage img) {
        long startTime = System.currentTimeMillis();
        outlineMouse(img);
        framesPassed++;
        long endTime = System.currentTimeMillis();
        totalTimePassed += endTime - startTime;
        return img;
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

    public void outlineMouse(DImage img) {
        long start = System.currentTimeMillis();
        short[][] bwpixels = applyColorThreshholding(img, 102);
        findMouse(bwpixels);
        double radius = 202;
        applyRadiusFilter(bwpixels, centerOfField, radius);
        this.bwpixels = bwpixels;
        centerOfMouse = centerOfMouse(bwpixels);
        centers.add(centerOfMouse);
        img.setPixels(bwpixels);
        mouse.clear();
        long end = System.currentTimeMillis();
        times.add((double)end - start);
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

    private Point locationAtTime(int t) {
        return centers.get(fps * t);
    }

    private double speedAtTime(int t) {
        int framesIndex = fps * t;
        return distanceBetween(framesIndex, framesIndex - 1);
    }

    private double averageSpeed(int tstart, int tend) {
        int startIndex = tstart * fps;
        int endIndex = tend * fps;
        double totalDistance = 0;
        double totalTime = 0;
        for (int i = startIndex + 1; i < endIndex; i++) {
            totalDistance += distanceBetween(i, i - 1);
            totalTime += times.get(i);
        }
        return totalDistance / totalTime;
    }

    private double distanceFromWall(int t) {
        int index = fps * t;
        return centers.get(index).distanceTo(centerOfField);
    }

    private double totalDistanceTraveled() { //make distance a class variable
        double totalDistance = 0;
        for (int i = 1; i < centers.size(); i++) {
            totalDistance += distanceBetween(i, i - 1);
        }
        return totalDistance;
    }

    private double timeSpentAtSpeed(double speed) {
        double totalTime = 0;
        for (int i = 1; i < centers.size(); i++) {
            if (Math.abs(distanceBetween(i, i - 1) / times.get(i) - speed) < 0.5) {
                totalTime += times.get(i);
            }
        }
        return totalTime;
    }

//    private int timeSpentInRegion(DataSet regionOfInterst) {
//        for (int i = 0; i < centers.size(); i++) {
//
//        }
//    }

    private double distanceBetween(int index1, int index2) {
        return centers.get(index1).distanceTo(centers.get(index2));
    }

//    private int ArrayList<TimeCode> timeSpentAtSpeed(double speed) {
//
//    }
}