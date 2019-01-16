import processing.core.PApplet;

import java.util.ArrayList;

public class MouseTracking implements PixelFilter {

    private Point centerOfMouse;
    private ArrayList<Point> centers = new ArrayList<>();
    private short[][] bwpixels;

    @Override
    public DImage processImage(DImage img) {
        short[][] bwpixels = applyColorThreshholding(img, 102);
        Point center = new Point(308, 241);
        double radius = 202;
        applyRadiusFilter(bwpixels, center, radius);
        this.bwpixels = bwpixels;
        centerOfMouse = centerOfMouse(bwpixels);
        centers.add(centerOfMouse);
        img.setPixels(bwpixels);
        return img;
    }

    private Point centerOfMouse(short[][] bwpixels) {
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