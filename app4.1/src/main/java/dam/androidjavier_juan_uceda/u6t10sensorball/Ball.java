package dam.androidjavier_juan_uceda.u6t10sensorball;

public class Ball {

    public final int DIAMETER = 20;
    public final double GRAVITY = 0.5;
    public double ax, vx, vy, x, y;

    public Ball() {
        vx = x = y = ax = 0;
        vy = 1;
    }
}
