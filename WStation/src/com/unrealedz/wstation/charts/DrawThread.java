package com.unrealedz.wstation.charts;

import java.util.List;

import com.unrealedz.wstation.entity.PolySector;
import com.unrealedz.wstation.utils.Contract;
import com.unrealedz.wstation.utils.Utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
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
    int titleId;
    String title;
    
    List<Point> nodes;

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }
    
    public DrawThread(SurfaceHolder surfaceHolder, List<Point> nodes, int titleId) {
        this.surfaceHolder = surfaceHolder;
        this.nodes = nodes;
        this.titleId = titleId;
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        p = new Paint();
        sb = new StringBuilder();
        int height, width;
    	List<PolySector> polySectors = null;
        while (runFlag) {
       	
            canvas = null;
            try {
                // получаем объект Canvas и выполняем отрисовку
                canvas = surfaceHolder.lockCanvas(null);
                polySectors = Utils.getChartPoint(canvas.getHeight(), canvas.getWidth(), nodes);
            	
                synchronized (surfaceHolder) {
                	if (canvas != null){	
                		//this moment is not understand, if not check (canvas != null) apps crashes 
		                    //canvas.drawColor(Color.WHITE);

		                    switch (titleId){
			                    case Contract.TEMPERATURE:
			                    	title = "temperature";
			                    break;
			                    case Contract.PRESSURE:
			                    	title = "pressure";
			                    break;
			                    case Contract.HUMIDITY:
			                    	title = "humidity";
			                    break;
		                    }
		                    
		                    drawBack(canvas);
		                    
	                    	p.setStyle(Paint.Style.FILL);
		                    p.setColor(Color.WHITE);
		                    p.setTextSize(20);
		                    p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		                    p.setAntiAlias(true);
		                    canvas.drawText(title,canvas.getHeight()/10,canvas.getHeight()/10,p);
	                    	drawPoly(canvas, Color.GREEN, polySectors);
	                    	Log.i("DEBUG", "h:" + String.valueOf(canvas.getHeight()) + " w:" + String.valueOf(canvas.getWidth()));
	                    	
	                    	
	                    	height = canvas.getHeight() - Contract.MARGIN_LEFT;
	    		            
	                    	p.setColor(Color.GRAY);
		                    p.setStyle(Paint.Style.STROKE);
		                    p.setStrokeWidth(2);
		                    canvas.drawLine(Contract.MARGIN_LEFT, Contract.PADDING_LEFT_RIGHT, Contract.MARGIN_LEFT, height, p);
		                    canvas.drawLine(Contract.MARGIN_LEFT, height, canvas.getWidth() - Contract.MARGIN_LEFT,  height , p);
		                    
		        	        p.setStyle(Paint.Style.FILL);
		                    p.setColor(Color.WHITE);
		                    p.setAntiAlias(true);
		                    p.setTextSize(25);
		                    p.setShadowLayer(1.0f, 2.0f, 2.0f, Color.BLACK);
		                    
		                	int offsetText = Contract.MARGIN_LEFT;
		                	
		                	int delta = (canvas.getWidth() - Contract.PADDING_LEFT_RIGHT)/(nodes.size()-1);
		                	
		                    for(int k = 0; k < nodes.size(); k++){
		                    	if(k == (nodes.size()-1)) offsetText = - Contract.MARGIN_LEFT*2;
		                    	canvas.drawText(String.valueOf(nodes.get(k).x),delta*k + offsetText, canvas.getHeight() - Contract.MARGIN_LEFT,p);
		                    }
		                    
		                    for(int k = 0; k < polySectors.size(); k++){
		                    	p.reset();
		                    	p.setStyle(Paint.Style.FILL);
		                    	p.setShader(new RadialGradient(0, 0, 5, 0xffffff00,  0xffff6e02, Shader.TileMode.CLAMP));
		            	        canvas.drawCircle(polySectors.get(k).getPoint(1).x, polySectors.get(k).getPoint(1).y, 5, p);
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
    
    private void drawBack(Canvas canvas){
    	
    	Paint polyPaint = new Paint();
    	Path polyPath = new Path();
    	int delta = (canvas.getWidth() - Contract.PADDING_LEFT_RIGHT)/(nodes.size()-1);
    	//polyPath.moveTo(Contract.MARGIN_LEFT, Contract.MARGIN_LEFT);
        
    	polyPaint.setStyle(Style.FILL);
    	polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xff727272,0xffffffff, Shader.TileMode.CLAMP));
    	//p.setColor(Color.YELLOW);
    	
        for (int i = 0; i < 4; i++) {
            //polyPath.lineTo(i*delta, canvas.getHeight() - Contract.MARGIN_LEFT);
        	int left = i*delta;
        	int right = (i+1)*delta;
        	if(i==0) left = left + Contract.MARGIN_LEFT;
        	if(i==3) right = (i+1)*delta - Contract.MARGIN_LEFT;
            canvas.drawRect(left, Contract.PADDING_LEFT_RIGHT, right, canvas.getHeight() - Contract.MARGIN_LEFT, polyPaint);
        }
        //polyPath.lineTo(points.get(0).x, points.get(0).y);

        // draw
        //canvas.drawPath(polyPath, p);
    }
    
    private void drawPoly(Canvas canvas, int color, List<PolySector> polySectors) {
        // line at minimum...
    	List<Point> points;
    	Paint polyPaint = new Paint();
    	Path polyPath = new Path();
    	

    	for( int j = 0; j < polySectors.size(); j++){
    	
	        /*if (points.size() < 2) {
	            return;
	        }*/
    		
    		
    		
    		points = polySectors.get(j).getArray();
	        // paint
    		//circle draw
	       // p.setShader(new RadialGradient(0, 0, 5, 0xffffff00,  0xffff6e02, Shader.TileMode.CLAMP));
	        //canvas.drawCircle(points.get(1).x, points.get(1).y, 5, p);
	            

	        // path draw
    		p.setStyle(Style.FILL);
    		p.setAntiAlias(true);
	        if ( (j & 1) == 0 ) p.setShader(new LinearGradient(0,points.get(0).y,0,points.get(1).y,0xfffed64d,0xfffff3b4, Shader.TileMode.CLAMP));       
	        else p.setShader(new LinearGradient(0,points.get(0).y,0,points.get(1).y,0xffb2cde4,0xffe0ebee, Shader.TileMode.CLAMP));

	        polyPath.moveTo(points.get(0).x, points.get(0).y);
	        
	        for (int i = 0; i < points.size(); i++) {
	            polyPath.lineTo(points.get(i).x, points.get(i).y);
	            
	        }
	        polyPath.lineTo(points.get(0).x, points.get(0).y);
	
	        // draw
	        canvas.drawPath(polyPath, p);
	        polyPath.reset();
	        

                                       
	        p.reset();

	    }
    	runFlag = false;
    }
    
}

