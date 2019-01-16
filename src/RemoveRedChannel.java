import processing.core.PApplet;

public class RemoveRedChannel implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        short[][] newRedChannel = new short[img.getHeight()][img.getWidth()];
        for (int r = 0; r < newRedChannel.length; r++) {
            for (int c = 0; c < newRedChannel[r].length; c++) {
                newRedChannel[r][c] = 100;
            }
        }
        img.setRedChannel(newRedChannel);
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
    }
}