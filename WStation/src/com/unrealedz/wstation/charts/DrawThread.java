package com.unrealedz.wstation.charts;

import java.util.List;

import com.unrealedz.wstation.entity.PolySector;
import com.unrealedz.wstation.utils.Utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by Alex on 16.06.2014.
 */
public class DrawThread extends Thread{

    private boolean runFlag = false;
    private SurfaceHolder surfaceHolder;
    private Paint p;
    StringBuilder sb;
    
    List<Point> nodes;

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }
    
    public DrawThread(SurfaceHolder surfaceHolder, List<Point> nodes) {
        this.surfaceHolder = surfaceHolder;
        this.nodes = nodes;
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        p = new Paint();
        sb = new StringBuilder();
        while (runFlag) {
        	List<PolySector> polySectors = null;

        	
            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                polySectors = Utils.getChartPoint(canvas.getHeight(), canvas.getWidth(), nodes);
            	
                synchronized (surfaceHolder) {
                	if (canvas != null){	
                		//this moment is not understand, if not check (canvas != null) apps crashes 
                	if (polySectors.size() == 0){
	                    canvas.drawColor(Color.WHITE);
	                    p.setColor(Color.GREEN);
	                    p.setStyle(Paint.Style.STROKE);
	                    p.setStrokeWidth(2);
	
	                    p.setAntiAlias(true);
	                    canvas.drawRect(10, 10, 30, 30, p);
	
	                    sb.setLength(0);
	                    sb.append("width = ").append(canvas.getWidth())
	                            .append(", height = ").append(canvas.getHeight());
	                    p.setStyle(Paint.Style.FILL);
	                    p.setColor(Color.BLACK);
	                    p.setTextSize(15);
	                    canvas.drawText(sb.toString(),10,50,p);
	                    }else{ 
	                    	
	                    	drawPoly(canvas, Color.GREEN, polySectors);
	                    	Log.i("DEBUG", "h:" + String.valueOf(canvas.getHeight()) + " w:" + String.valueOf(canvas.getWidth()));
	                    	//for(Point p: points) Log.i("Debug", "X: " + p.x + "Y: " + p.y);
	                    }
                	} else break;

                }
            }
            finally {
                if (canvas != null) {
                    // отрисовка выполнена. выводим результат на экран
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
    
    private void drawPoly(Canvas canvas, int color, List<PolySector> polySectors) {
        // line at minimum...
    	List<Point> points;
    	for( int j = 0; j < polySectors.size(); j++){
    	
	        /*if (points.size() < 2) {
	            return;
	        }*/
    		if ((j == 0) || (j == 2)) color = Color.BLUE;
    		else color = Color.WHITE;
    		points = polySectors.get(j).getArray();
	        // paint
	        Paint polyPaint = new Paint();
	        polyPaint.setColor(color);
	        polyPaint.setStyle(Style.FILL);
	
	        // path
	        Path polyPath = new Path();
	        polyPath.moveTo(points.get(0).x, points.get(0).y);
	        int i, len;
	        len = points.size();
	        for (i = 0; i < len; i++) {
	            polyPath.lineTo(points.get(i).x, points.get(i).y);
	        }
	        polyPath.lineTo(points.get(0).x, points.get(0).y);
	
	        // draw
	        canvas.drawPath(polyPath, polyPaint);
	        runFlag = false;
	    }
    	
    }
    
}

