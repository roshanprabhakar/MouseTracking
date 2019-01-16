import processing.core.PApplet;

public class FrameDifference implements PixelFilter {

    short[][] previousFrame;

    @Override
    public DImage processImage(DImage img) {
        int threshhold = 50;
        if (previousFrame == null) {
            previousFrame = img.getBWPixelGrid();
        }
        short[][] thisFrame = img.getBWPixelGrid();
        short[][] writeThis = new short[img.getHeight()][img.getWidth()];
        for (int r = 0; r < img.getHeight(); r++) {
            for (int c = 0; c < img.getWidth(); c++) {
                if (Math.abs(thisFrame[r][c] - previousFrame[r][c]) > threshhold) {
                    writeThis[r][c] = 255;
                } else {
                    writeThis[r][c] = 0;
                }
            }
        }
        previousFrame = new DImage(img).getBWPixelGrid();
        img.setPixels(writeThis);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
    }
}