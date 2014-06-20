package com.unrealedz.wstation.charts;

import java.util.List;

import com.unrealedz.wstation.entity.PolySector;
import com.unrealedz.wstation.utils.ChartDataBuilder;
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
    int offsetX = Contract.PADDING;
    int offsetY = Contract.PADDING;
    
    List<Point> nodes;

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }
    
    public DrawThread(SurfaceHolder surfaceHolder, List<Point> nodes, int titleId) {
        this.surfaceHolder = surfaceHolder;
        this.nodes = nodes;
        this.titleId = titleId;
        //Log.i("DEBUG:", "nodes size: " + nodes.size());
    }

    public void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        p = new Paint();

    	List<PolySector> polySectors = null;
        while (runFlag) {
       	
            canvas = null;
            try {               
                canvas = surfaceHolder.lockCanvas(null);
                polySectors = ChartDataBuilder.getChartPoint(canvas.getHeight(), canvas.getWidth(), nodes);
            	
                synchronized (surfaceHolder) {
                	if (canvas != null){	
                		//this moment is not understand, if not check (canvas != null) apps crashes 
		                   
		                    switch (titleId){
			                    case Contract.TEMPERATURE:
			                    	title = "temperature, C°";
			                    break;
			                    case Contract.PRESSURE:
			                    	title = "pressure, mB";
			                    break;
			                    case Contract.HUMIDITY:
			                    	title = "humidity, %";
			                    break;
		                    }
		                    
		                    //draw background
		                     drawBack(canvas);
		                    
	                    	p.setStyle(Paint.Style.FILL);
		                    p.setColor(Color.WHITE);
		                    p.setTextSize(20);
		                    p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		                    p.setAntiAlias(true);
		                    canvas.drawText(title,canvas.getHeight()/10,canvas.getHeight()/10,p);
		                    
		                    //Draw chart info
	                    	drawPoly(canvas, Color.GREEN, polySectors);
	                    	                   	
	                    	//draw Axis	    		            
	                    	drawAxis(canvas);
		                    
		                    //draw label on axis X	                    	
	                    	drawLabelHour(canvas);
		        	        		                    
		                    //draw point circle
	                    	
	                    	drawCircle(canvas, polySectors);
		                    
		                    
		                    drawParameters(canvas, polySectors);

		                    
                	} else break;
                    //runFlag = false;
                }
            }
            finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            runFlag = false;
            
        }
    }
    
    private void drawBack(Canvas canvas){
    	
    	Paint polyPaint = new Paint();
    	int delta = (canvas.getWidth() - Contract.PADDING*2)/(nodes.size()-1);
        
    	polyPaint.setStyle(Style.FILL);
    	polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xff727272,0xffffffff, Shader.TileMode.CLAMP));
    	
        for (int i = 0; i < nodes.size()-1; i++) {
        	int left = i*delta + offsetX;
        	int right = (i+1)*delta + offsetX ;
        	if ( (i & 1) == 0 ) polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xff727272,0xffffffff, Shader.TileMode.CLAMP));
        	else polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xffffffff,0xff727272, Shader.TileMode.CLAMP));
            canvas.drawRect(left, offsetY, right, canvas.getHeight() - offsetY, polyPaint);
        }
        
    }
    
    private void drawAxis(Canvas canvas){
    	p.setColor(Color.GRAY);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2);
        canvas.drawLine(offsetX, offsetY, offsetX, canvas.getHeight() - offsetY, p);
        canvas.drawLine(offsetX, canvas.getHeight() - offsetY, canvas.getWidth() - offsetX,  canvas.getHeight() - offsetY , p);
    }
    
    private void drawLabelHour(Canvas canvas){
    	p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(25);
        p.setShadowLayer(1.0f, 2.0f, 2.0f, Color.BLACK);
        
    	int offsetText = Contract.MARGIN_LEFT;		                	
    	int delta = (canvas.getWidth() - Contract.PADDING_LEFT_RIGHT)/(nodes.size()-1);
    	
        for(int k = 0; k < nodes.size(); k++){			                    	
        	if(k == (nodes.size()-1)) offsetText = - Contract.MARGIN_LEFT*3;
        	canvas.drawText(String.valueOf(nodes.get(k).x),delta*k + offsetText, canvas.getHeight() - Contract.MARGIN_LEFT,p);
        }
    }
    
    private void drawCircle(Canvas canvas, List<PolySector> polySectors){
    	
    	p.reset();
    	p.setStyle(Paint.Style.FILL);
    	p.setShader(new RadialGradient(0, 0, 5, 0xffffff00,  0xffff6e02, Shader.TileMode.CLAMP));
    	
    	for(int k = 0; k < polySectors.size() + 1; k++){
       	
        	int x, y;

    		if(k == (polySectors.size())) {
    			x = polySectors.get(k-1).getPoint(2).x + offsetX;
    			y = polySectors.get(k-1).getPoint(2).y + offsetY;
    		} else{
    			x = polySectors.get(k).getPoint(1).x + offsetX;
    			y = polySectors.get(k).getPoint(1).y + offsetY;
    		}
        	
	        canvas.drawCircle(x, y, 5, p);
        }
    }
    
    private void drawParameters(Canvas canvas, List<PolySector> polySectors){
    	p.reset();
    	p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(15);
        p.setShadowLayer(1.0f, 2.0f, 2.0f, Color.BLACK);
        
        int offsetText = Contract.MARGIN_LEFT;		                	
    	int delta = (canvas.getWidth() - Contract.PADDING_LEFT_RIGHT)/(nodes.size()-1);
    	
    	for(int k = 0; k < nodes.size(); k++){
    		
    		int x, y;

    		if(k == (nodes.size()-1)) {
      			offsetText = - Contract.MARGIN_LEFT*5;
    			x = polySectors.get(k-1).getPoint(2).x + offsetX + offsetText;
    			y = polySectors.get(k-1).getPoint(2).y - offsetY ;
    		}else{
    		x = polySectors.get(k).getPoint(1).x + offsetX + offsetText;
    		y = polySectors.get(k).getPoint(1).y - offsetY;
    		}
        	canvas.drawText(String.valueOf(nodes.get(k).y),x, y, p);
	     
        }
    }
    
    private void drawPoly(Canvas canvas, int color, List<PolySector> polySectors) {
        // line at minimum...
    	List<Point> points;
    	Path polyPath = new Path();
    	

    	for( int j = 0; j < polySectors.size(); j++){
		
    		points = polySectors.get(j).getArray();
    		
	        // path draw
    		p.setStyle(Style.FILL);
    		p.setAntiAlias(true);
	        if ( (j & 1) == 0 ) p.setShader(new LinearGradient(0,points.get(0).y,0,points.get(1).y,0xfffed64d,0xfffff3b4, Shader.TileMode.CLAMP));       
	        else p.setShader(new LinearGradient(0,points.get(0).y,0,points.get(1).y,0xffb2cde4,0xffe0ebee, Shader.TileMode.CLAMP));

	        polyPath.moveTo(points.get(0).x + offsetX, points.get(0).y + offsetY);
	        
	        for (int i = 0; i < points.size(); i++) {
	            polyPath.lineTo(points.get(i).x + offsetX, points.get(i).y + offsetY);
	            
	        }
	        polyPath.lineTo(points.get(0).x + offsetX, points.get(0).y + offsetY);
	
	        // draw
	        canvas.drawPath(polyPath, p);
	        polyPath.reset();
                                
	        p.reset();

	    }
    }
    
}

