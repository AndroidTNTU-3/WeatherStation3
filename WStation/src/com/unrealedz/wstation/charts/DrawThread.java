package com.unrealedz.wstation.charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.unrealedz.wstation.entity.PolySector;
import com.unrealedz.wstation.utils.ChartDataBuilder;
import com.unrealedz.wstation.utils.Contract;
import com.unrealedz.wstation.utils.Utils;
import com.unrealedz.wstation.utils.ValueSort;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Rect;
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
    private ChartDrawer chartDrawerMin;
    private ChartDrawer chartDrawerMax;
    
    List<Point> nodesMax;
    List<Point> nodesMin;
    
    private boolean drawMax = true; //if min/max mode used
 
    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
    }
    
    public DrawThread(SurfaceHolder surfaceHolder, List<Point> nodesMax, List<Point> nodesMin, int titleId) {
        this.surfaceHolder = surfaceHolder;
        this.nodesMax = nodesMax;
        this.nodesMin = nodesMin;
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

    	List<PolySector> polySectorsMin = null;
    	List<PolySector> polySectorsMax = null;
        while (runFlag) {
       	
            canvas = null;
            try {               
                canvas = surfaceHolder.lockCanvas(null);
                
            	
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
		                    
	                    	/*p.setStyle(Paint.Style.FILL);
		                    p.setColor(Color.WHITE);
		                    p.setTextSize(20);
		                    p.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		                    p.setAntiAlias(true);
		                    canvas.drawText(title,canvas.getHeight()/10,canvas.getHeight()/10,p);*/
		                    
		                    //Draw chart info
		                     
		                    chartDrawerMax = new ChartDrawer();
		                    polySectorsMax = ChartDataBuilder.getChartPoint(canvas.getHeight(), canvas.getWidth(), nodesMax);
		                    chartDrawerMax.drawPoly(canvas, Color.GREEN, polySectorsMax);
		                    chartDrawerMax.drawCircle(canvas, polySectorsMax);		                    		                    
		                    chartDrawerMax.drawParameters(canvas, polySectorsMax, nodesMax);

		                    
		                   /* chartDrawerMin = new ChartDrawer();
	                    	drawMax = false;
	                    	
	                    	polySectorsMin = ChartDataBuilder.getChartPoint(canvas.getHeight(), canvas.getWidth(), nodesMin);
	                    	chartDrawerMin.drawPoly(canvas, Color.GREEN, polySectorsMin);
	                    	chartDrawerMin.drawCircle(canvas, polySectorsMin);		                    		                    
	                    	chartDrawerMin.drawParameters(canvas, polySectorsMin, nodesMin);*/
	                    	
	                    	//draw Axis	    		            
	                    	drawAxis(canvas);
		                    
		                    //draw label on axis X	                    	
	                    	//drawLabelHour(canvas);
	                    	dravTemeratureAxis(canvas, polySectorsMax);	                    
		                    //draw point circle
	                    	


		                    
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
    	int delta = (canvas.getWidth() - Contract.PADDING*2)/(nodesMax.size()-1);
        
    	polyPaint.setStyle(Style.FILL);
    	polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xff727272,0xffffffff, Shader.TileMode.CLAMP));
    	
        for (int i = 0; i < nodesMax.size()-1; i++) {
        	int left = i*delta + offsetX;
        	int right = (i+1)*delta + offsetX ;
        	if ( (i & 1) == 0 ) polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xff727272,0xffffffff, Shader.TileMode.CLAMP));
        	else polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xffffffff,0xff727272, Shader.TileMode.CLAMP));
            canvas.drawRect(left, offsetY, right, canvas.getHeight() - offsetY, polyPaint);
        }
        
    }
    
    private void drawAxis(Canvas canvas){
    	p.reset();
    	p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1);
        canvas.drawLine(offsetX, offsetY, offsetX, canvas.getHeight() - offsetY, p);
        p.setColor(Color.GRAY);
        canvas.drawLine(offsetX, canvas.getHeight() - offsetY, canvas.getWidth() - offsetX,  canvas.getHeight() - offsetY , p);
    }
    
    private void dravTemeratureAxis(Canvas canvas, List<PolySector> polySectorsMax){
    	p.reset();
    	p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1);
    	List<Point> sortedPoints = new ArrayList<Point>(nodesMax);
    	
    	Collections.sort(sortedPoints, new ValueSort());
		
		int MaxValue = sortedPoints.get(sortedPoints.size() - 1).y;
		int MinValue = sortedPoints.get(0).y;	
		
		int heightAxis = canvas.getHeight() - Contract.PADDING*2;
		float k = (float)((float)heightAxis/(MaxValue-MinValue))*Contract.MULT_Y_NARROW;
		
		int offset = (int) (((float)heightAxis - ((float)(MaxValue - MinValue)*k))/2);
		//offset = 0;
    	int minAxisValue = (int) (MinValue - Math.round(offset/k));
        int maxAxisValue = (int) (MaxValue + Math.round(offset/k));

        float deltaValue = (float)(maxAxisValue - minAxisValue)/4;
        float deltaT = (float) heightAxis/4;
    	float currentValue = maxAxisValue;
    	
    	Rect bounds = new Rect();
    	
    	for (int i = 0; i < 5 ; i++){
    		
    		int markerYPos = Math.round(deltaT*i) + offsetY;							
    		canvas.drawLine(offsetX, markerYPos, offsetX*2, markerYPos, p);
    		String labelT = String.valueOf(Math.round(currentValue));
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.WHITE);
            p.setAntiAlias(true);
            p.setTextSize(15);
            
            //get a text height 
            p.getTextBounds(labelT, 0, labelT.length(), bounds);           
            int textHeight = bounds.height();
            int textYPos = Math.round(deltaT*i) + offsetY;
            
            if (i == 0) textYPos = textYPos + textHeight;
            canvas.drawText(labelT, offsetX*2, textYPos, p);
            currentValue = currentValue - deltaValue;
    	}
    	
    }
    
    private void drawLabelHour(Canvas canvas){
    	p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(25);
        p.setShadowLayer(1.0f, 2.0f, 2.0f, Color.BLACK);
        
    	int offsetText = Contract.MARGIN_LEFT;		                	
    	int delta = (canvas.getWidth() - Contract.PADDING_LEFT_RIGHT)/(nodesMax.size()-1);
    	
        for(int k = 0; k < nodesMax.size(); k++){			                    	
        	if(k == (nodesMax.size()-1)) offsetText = - Contract.MARGIN_LEFT*4;
        	canvas.drawText(String.valueOf(nodesMax.get(k).x),delta*k + offsetText, canvas.getHeight() - Contract.MARGIN_LEFT*2,p);
        }
    }
    
    private class ChartDrawer{
    	
        public void drawCircle(Canvas canvas, List<PolySector> polySectors){
        	
        	p.reset();
        	p.setStyle(Paint.Style.FILL);
        	p.setAntiAlias(true);
        	//p.setShader(new RadialGradient(0, 0, 5.0f, 0xffff6e02, 0xffffff00, Shader.TileMode.CLAMP));
        	//p.setShader(new RadialGradient(1, 1, 5.0f, 0xFFFFFFFF, 0xFF000000, Shader.TileMode.MIRROR));
        	
        	for(int k = 0; k < polySectors.size() + 1; k++){

            	int x, y;

        		if(k == (polySectors.size())) {
        			x = polySectors.get(k-1).getPoint(2).x + offsetX;
        			y = polySectors.get(k-1).getPoint(2).y + offsetY;
        		} else{
        			x = polySectors.get(k).getPoint(1).x + offsetX;
        			y = polySectors.get(k).getPoint(1).y + offsetY;
        		}
        		p.setShader(new RadialGradient(x-2, y-2, 4.0f, 0xffffff00, 0xffff6e02, Shader.TileMode.CLAMP));
    	        canvas.drawCircle(x, y, 5, p);
            }
        }
        
        public void drawParameters(Canvas canvas, List<PolySector> polySectors, List<Point> nodes){
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
        
        public void drawPoly(Canvas canvas, int color, List<PolySector> polySectors) {
            // line at minimum...
        	List<Point> points;
        	Path polyPath = new Path();
        	

        	for( int j = 0; j < polySectors.size(); j++){
    		
        		points = polySectors.get(j).getArray();
        		
    	        // path draw
        		p.setStyle(Style.FILL);
        		p.setAntiAlias(true);
        		if (drawMax){
        			if ( (j & 1) == 0 ) p.setShader(new LinearGradient(0,points.get(0).y,0,points.get(1).y,0xfffed64d,0xfffff3b4, Shader.TileMode.CLAMP));       
        			else p.setShader(new LinearGradient(0,points.get(0).y,0,points.get(1).y,0xffb2cde4,0xffe0ebee, Shader.TileMode.CLAMP));
        		}else{
        			if ( (j & 1) == 0 ) p.setShader(new LinearGradient(0,points.get(0).y,0,points.get(1).y,0xffe0ebee,0xffb2cde4, Shader.TileMode.CLAMP));       
        			else p.setShader(new LinearGradient(0,points.get(0).y,0,points.get(1).y,0xffb2cde4,0xffe0ebee, Shader.TileMode.CLAMP));
        		}

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
    

    
}

