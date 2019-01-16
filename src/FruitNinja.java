import processing.core.PApplet;

public class FruitNinja implements PixelFilter {

    short[][] previousFrame;
    short[][] writeThis;
    Point fruit = new Point(0, 0);

    @Override
    public DImage processImage(DImage img) {
        int threshhold = 40;
        if (previousFrame == null) {
            previousFrame = img.getBWPixelGrid();
        }
        short[][] thisFrame = img.getBWPixelGrid();
        writeThis = new short[img.getHeight()][img.getWidth()];
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
        //writePixels reflects the changes
        return img;
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        window.ellipse(fruit.getY(), fruit.getX(), 30, 30);
        if (writeThis != null) {
            System.out.println(fruit);
            if (writeThis[fruit.getY()][fruit.getX()] == 0 || fruit.getY() >= original.getWidth()) {
                fruit.setX(0);
                fruit.setY(0);
            } else {
                fruit.setY(fruit.getY() + 2);
            }
        }
    }
}