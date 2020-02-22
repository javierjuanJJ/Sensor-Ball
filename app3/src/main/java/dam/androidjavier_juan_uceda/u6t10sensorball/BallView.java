package dam.androidjavier_juan_uceda.u6t10sensorball;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.view.Display;
import android.view.View;

public class BallView extends View implements SensorEventListener {
    SoundManager soundManager;
    Bitmap bitmapBackground;
    Bitmap bitmapBall;
    Rect screen;
    Display display;
    private Ball ball;
    private Paint pen;
    private int width, height;

    public BallView(Context context) {
        super(context);
        ball = new Ball();
        pen = new Paint();
        Point size = new Point();
        display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        display.getSize(size);
        bitmapBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        bitmapBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.balonfutbol);
        soundManager = new SoundManager();
        soundManager.initSoundManager(context);
        soundManager.addSound(1, R.raw.ball_bounce);
        screen = new Rect(0, 0, size.x, size.y);
    }

    private double map(double value, double o1, double o2, double d1, double d2) {
        double result;
        double scale = (d2 - d1) / (o2 - o1);
        result = (value - o1) * scale + d1;
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        ball.x = w / 2;
        ball.y = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmapBackground, null, screen, null);
        pen.setColor(Color.BLUE);
        pen.setStrokeWidth(1);
        canvas.drawBitmap(bitmapBall, (float) ball.x, (float) ball.y, null);
    }

    protected void updatePhysics() {
        boolean collision = false;
        if (ball.x + ball.DIAMETER / 2 > width - 1) {
            ball.vx = -ball.vx / 2.0;
            ball.x = width - 1 - ball.DIAMETER / 2;
            collision = true;
        }

        if (ball.x + ball.DIAMETER / 2 < 0) {
            ball.vx = -ball.vx / 2.0;
            ball.x = width - 1 - ball.DIAMETER / 2;
            collision = true;
        }

        if (ball.y + ball.DIAMETER / 2 > height - 1) {
            ball.vy *= -1;
            ball.y = height - 1 - ball.DIAMETER / 2;
            collision = true;
        }

        if (ball.y + ball.DIAMETER / 2 < 0) {
            ball.vy *= -1;
            ball.y = height - 1 - ball.DIAMETER / 2;
            collision = true;
        }
        if (collision) soundManager.playSound(1);
        ball.vy += ball.GRAVITY;
        ball.vx += ball.ax;

        ball.y += ball.vy;
        ball.x += ball.vx;

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double value = event.values[0];
            if (value > 5) value = 5;
            if (value < -5) value = -5;
            if (value < 0.1 && value > -0.1) ball.ax = 0.0;
            else {
                ball.ax = map(Math.abs(value), 0.1, 5, 0.05, 0.5);
                if (value < 0.0) ball.ax *= -1;
            }
            ball.ax *= -1;
            updatePhysics();
            invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
