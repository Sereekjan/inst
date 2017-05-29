package kz.ikar.almatyinstitutes.classes;


/**
 * Created by User on 24.05.2017.
 */

public class Point {
    private int id;
    private double latitude;
    private double longitude;

    public Point(){};

    public Point(int id,double latitude, double longitude) {
        this.id=id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Point(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
