import processing.core.PApplet;

public class ImposeGrid implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) { //n by n
        int vertical = 5;
        int horizontal = 5;
        int[][] pixels = original.getColorPixelGrid();
        for (int r = 0; r < original.getHeight(); r += original.getHeight() / vertical) {
            for (int c = 0; c < original.getWidth(); c ++) {
                pixels[r][c] = 0;
            }
        }
        for (int r = 0; r < original.getHeight(); r++) {
            for (int c = 0; c < original.getWidth(); c += original.getWidth() / horizontal) {
                pixels[r][c] = 0;
            }
        }
        filtered.setPixels(pixels);
    }
}