import processing.core.PApplet;

import java.util.ArrayList;

public class MouseTracking implements PixelFilter {

    private Point centerOfMouse;
    private ArrayList<Point> centers = new ArrayList<>();
    private short[][] bwpixels;
    private DataSet mouse = new DataSet();

    @Override
    public DImage processImage(DImage img) {
        outlineMouse(img);
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
        short[][] bwpixels = applyColorThreshholding(img, 102);
        findMouse(bwpixels);
        Point center = new Point(308, 241);
        double radius = 202;
        applyRadiusFilter(bwpixels, center, radius);
        this.bwpixels = bwpixels;
        centerOfMouse = centerOfMouse(bwpixels);
        centers.add(centerOfMouse);
        img.setPixels(bwpixels);
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


    //DATA COLLECTOR API
//    private Point locationAtTime(int t) {
//
//    }
//
//    private double speedAtTimeT(int t) {
//
//    }
//
//    private double averageSpeed(int tstart, int tend) {
//
//    }
//
//    private double distanceFromWall(int t) {
//
//    }
//
//    private double totalDistanceTraveled() {
//
//    }
//
//    private int timeSpentAtSpeed(double cmpers) {
//
//    }
//
//    private int timeSpentInRegion(Cluster regionOfInterst) {
//
//    }
//
//    private int ArrayList<TimeCode> timeSpentAtSpeed(double speed) {
//
//    }
}