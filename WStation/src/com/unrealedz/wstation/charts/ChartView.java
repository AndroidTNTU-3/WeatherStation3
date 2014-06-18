package com.unrealedz.wstation.charts;

import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class ChartView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    List<Point> nodes;
    private int title;
    
    public ChartView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }
    
    public ChartView(Context context, List<Point> nodes, int title) {
        super(context);
        getHolder().addCallback(this);
        this.nodes = nodes;
        this.title = title;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(getHolder(), nodes, title);
        drawThread.setRunning(true);
        drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

        boolean retry = true;
        // завершаем работу потока
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // если не получилось, то будем пытаться еще и еще
            }
        }

    }
}
