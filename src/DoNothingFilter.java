import processing.core.PApplet;

public class DoNothingFilter implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        window.line(0,0, 300,300);
    }
}