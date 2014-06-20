package com.unrealedz.wstation.charts;

import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class ChartView extends SurfaceView implements SurfaceHolder.Callback {

    private DrawThread drawThread;
    List<Point> nodes;
    private int titleId;
    
    public ChartView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }
    
    public ChartView(Context context, List<Point> nodes, int titleId) {
        super(context);
        getHolder().addCallback(this);
        this.nodes = nodes;
        this.titleId = titleId;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {    
        drawThread = new DrawThread(getHolder(), nodes, titleId);
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
        drawThread.setRunning(false);
        while (retry) {
            try {
                drawThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }

    }
}
