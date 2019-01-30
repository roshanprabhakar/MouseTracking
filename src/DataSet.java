import java.util.ArrayList;

public class DataSet {

    private ArrayList<Point> centers; //the centers of the mouse for every frame
    private ArrayList<Double> times; //how long it took the mouse to move from previous to current frame
    private Point centerOfField;
    private double fieldRadius;
    private int fps;
    private double cmpp;

    public void addCenter(Point p) {
        centers.add(p);
    }

    public DataSet() {
        centers = new ArrayList<>();
        times = new ArrayList<>();
    }

    public void setFieldRadius(double fieldRadius) {
        this.fieldRadius = fieldRadius;
    }

    public void setCenterOfField(Point p) {
        this.centerOfField = p;
    }

    public void setCmpp(double cmpp) {
        this.cmpp = cmpp;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    //Data collecting methods

    private Point locationAtTime(int t) {
        return centers.get(fps * t);
    }


    private double speedAtTime(int t) {
        int framesIndex = fps * t;
        return distanceBetween(framesIndex, framesIndex - 1);
    }

    private double totalDistanceTraveled() { //make distance a class variable
        double totalDistance = 0;
        for (int i = 1; i < centers.size(); i++) {
            totalDistance += distanceBetween(i, i - 1);
        }
        return totalDistance;
    }

    //tstart = seconds from start, start of data collecting time
    //tend = seconds from start, end of data collecting time
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


    private double timeSpentAtSpeed(double speed) {
        double totalTime = 0;
        for (int i = 1; i < centers.size(); i++) {
            if (Math.abs(distanceBetween(i, i - 1) / times.get(i) - speed) < 0.5) {
                totalTime += times.get(i);
            }
        }
        return totalTime;
    }

    //time t in seconds since the video is running
    private double distanceFromWall(int t) {
        int index = fps * t;
        return fieldRadius - centers.get(index).distanceTo(centerOfField);
    }

    private double distanceBetween(int index1, int index2) {
        return centers.get(index1).distanceTo(centers.get(index2));
    }

}
