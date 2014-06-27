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
    int bottomEdge;
    int topEdge;
    private ChartDrawer chartDrawerMin;
    private ChartDrawer chartDrawerMax;
    
    List<Point> nodesMax;
    List<Point> nodesMin;
    
    ChartDataBuilder chartDataBuilder;
    
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
        chartDataBuilder = new ChartDataBuilder();
        
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
		                    
		                    bottomEdge = canvas.getHeight() - Contract.PADDING_BOTTOM;
		                    topEdge =  Contract.PADDING_TOP;
		                    
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
		                    polySectorsMax = chartDataBuilder.getChartPoint(canvas.getHeight(), canvas.getWidth(), nodesMax);
		                    chartDrawerMax.drawPoly(canvas, polySectorsMax);
		                    chartDrawerMax.drawCircle(canvas);		                    		                    
		                    chartDrawerMax.drawParameters(canvas, nodesMax);
		                    drawMidGrad(canvas);
		                    drawMidGrad1(canvas);
		                    
		                    
		                   /* chartDrawerMin = new ChartDrawer();
	                    	drawMax = false;
	                    	
	                    	polySectorsMin = ChartDataBuilder.getChartPoint(canvas.getHeight(), canvas.getWidth(), nodesMin);
	                    	chartDrawerMin.drawPoly(canvas, Color.GREEN, polySectorsMin);
	                    	chartDrawerMin.drawCircle(canvas, polySectorsMin);		                    		                    
	                    	chartDrawerMin.drawParameters(canvas, polySectorsMin, nodesMin);*/
	                    	
	                    	//draw Axis	    		            
	                    	drawAxis(canvas);
		                    
		                    //draw label on axis X	                    	
	                    	drawLabelHour(canvas);
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
    	int delta = (canvas.getWidth() - Contract.PADDING_LEFT_RIGHT)/(nodesMax.size()-1);
        
    	polyPaint.setStyle(Style.FILL);
    	polyPaint.setShader(new LinearGradient(0,0,0, bottomEdge ,0xff727272,0xffffffff, Shader.TileMode.CLAMP));
    	
        for (int i = 0; i < nodesMax.size()-1; i++) {
        	int left = i*delta + Contract.PADDING_LEFT;
        	int right = (i+1)*delta + Contract.PADDING_RIGHT ;
        	if ( (i & 1) == 0 ) polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xff727272,0xffffffff, Shader.TileMode.CLAMP));
        	else polyPaint.setShader(new LinearGradient(0,0,0,canvas.getHeight(),0xffffffff,0xff727272, Shader.TileMode.CLAMP));
            canvas.drawRect(left, topEdge, right, bottomEdge, polyPaint);
        }
        
    }
    
    private void drawAxis(Canvas canvas){
    	p.reset();
    	p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(1);
        canvas.drawLine(Contract.PADDING_LEFT, topEdge, Contract.PADDING_LEFT, bottomEdge, p);
        
        p.setColor(Color.WHITE);
        canvas.drawLine(Contract.PADDING_LEFT, bottomEdge, canvas.getWidth() - Contract.PADDING_RIGHT,  bottomEdge, p);
    }
    
    private void dravTemeratureAxis(Canvas canvas, List<PolySector> polySectors){
    	p.reset();
        
        int amountYParameter = 4;           	
    	
    	List<Integer> divisionY = chartDataBuilder.getDivision(canvas.getHeight(), canvas.getWidth(), nodesMax, amountYParameter);
    	List<Float> divisionValue = chartDataBuilder.getDivisionValue(amountYParameter);
    	
    	for (int i = 0; i < amountYParameter + 1 ; i++){
    		
        	p.setColor(Color.WHITE);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(1);
    									
    		canvas.drawLine(Contract.PADDING_LEFT, divisionY.get(i), Contract.PADDING_LEFT*2, divisionY.get(i), p);
    		 		
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.WHITE);
            p.setAntiAlias(true);
            p.setTextSize(15);
                        
            String labelParam = String.valueOf(Math.round(divisionValue.get(i)))  + "°";
   
            int textHeight = getTextHeight(p, labelParam);
            int textYPos = divisionY.get(i) + topEdge;
            
            if (i == 0) textYPos = textYPos + textHeight;
            if (i == amountYParameter) textYPos = textYPos - textHeight;
            canvas.drawText(labelParam, Contract.PADDING_LEFT*2, textYPos, p);           
    	}
    	
    }
    
    private void drawLabelHour(Canvas canvas){
    	p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(15);
        
    	int offsetText = Contract.PADDING_LEFT;		
    	
    	List<Point> points = chartDataBuilder.getPoints();
    	
    	float delta = (float)(canvas.getWidth() - Contract.PADDING_LEFT_RIGHT)/(nodesMax.size()-1);
    	    	   	
        for(int k = 0; k < nodesMax.size(); k++){	
        	int textWidth = getTextWidth(p, String.valueOf(nodesMax.get(k).x));
        	int textHeight = getTextHeight(p, String.valueOf(nodesMax.get(k).x));
        	
        	if(k == (nodesMax.size()-1)) offsetText = -textWidth/2;
        	canvas.drawText(String.valueOf(nodesMax.get(k).x),points.get(k).x + offsetText, bottomEdge +  textHeight + 2,p);
        	offsetText = Contract.MARGIN_LEFT - textWidth/2;
        }
    }
    
    private class ChartDrawer{
    	
        public void drawCircle(Canvas canvas){
        	
        	p.reset();
        	p.setStyle(Paint.Style.FILL);
        	p.setAntiAlias(true);
        	//p.setShader(new RadialGradient(0, 0, 5.0f, 0xffff6e02, 0xffffff00, Shader.TileMode.CLAMP));
        	//p.setShader(new RadialGradient(1, 1, 5.0f, 0xFFFFFFFF, 0xFF000000, Shader.TileMode.MIRROR));
        	List<Point> points = chartDataBuilder.getPoints();
        	
        	for(int k = 0; k < points.size(); k++){
       		
        		int y = bottomEdge - points.get(k).y;
	        	int x = Contract.PADDING_LEFT + points.get(k).x;	            		
         		
        		p.setShader(new RadialGradient(x-2, y-2, 4.0f, 0xffffff00, 0xffff6e02, Shader.TileMode.CLAMP));
    	        canvas.drawCircle(x, y, 5, p);
            }
        }
        
        public void drawParameters(Canvas canvas, List<Point> nodes){
        	p.reset();
        	p.setStyle(Paint.Style.FILL);
            p.setColor(Color.WHITE);
            p.setAntiAlias(true);
            p.setTextSize(15);
            p.setShadowLayer(1.0f, 2.0f, 2.0f, Color.BLACK);
            
            List<Point> points = chartDataBuilder.getPoints();
            
            int offsetTextX = Contract.PADDING_TEXT;		
 		
        	for(int k = 0; k < points.size(); k++){
	    		
        		String textParams = String.valueOf(nodes.get(k).y) + "°";
        		
	            int textWidth = getTextWidth(p, textParams);
	        	int textHeight = getTextHeight(p, textParams);
	        	
	    		if(k == (points.size() - 1)) {
	    			offsetTextX = -(textWidth + Contract.PADDING_TEXT);
	    		}
	        	
	        	int y = bottomEdge - points.get(k).y + textHeight/2;
	        	int x = Contract.PADDING_LEFT + points.get(k).x + offsetTextX;
	            		
	            canvas.drawText(textParams, x, y, p);            	

            }
        }
        
        public void drawPoly(Canvas canvas, List<PolySector> polySectors) {
            // line at minimum...
        	List<Point> points;
        	Path polyPath = new Path();
        	

        	for( int j = 0; j < polySectors.size(); j++){
    		
        		points = polySectors.get(j).getArray();
        		
    	        // path draw
        		p.setStyle(Style.FILL);
        		p.setAntiAlias(true);
        		
        		/*
        		 * get max value for gradient max value ;
        		 */
        		int maxY = 0;
        		if (points.get(1).y < points.get(2).y) maxY = points.get(1).y; 
        		else maxY = points.get(2).y;
        			       		
        		if (drawMax){
        			if ( (j & 1) == 0 ) p.setShader(new LinearGradient(0,points.get(0).y,0,maxY,0xfffbc948,0xfffff880, Shader.TileMode.CLAMP));       
        			else p.setShader(new LinearGradient(0,points.get(0).y,0,maxY,0xffa8c1d7,0xffdae1ee, Shader.TileMode.CLAMP));
        		}else{
        			if ( (j & 1) == 0 ) p.setShader(new LinearGradient(0,points.get(0).y,0,maxY,0xffe0ebee,0xffb2cde4, Shader.TileMode.CLAMP));       
        			else p.setShader(new LinearGradient(0,points.get(0).y,0,maxY,0xffb2cde4,0xffe0ebee, Shader.TileMode.CLAMP));
        		}

    	        polyPath.moveTo(points.get(0).x + Contract.PADDING_LEFT, points.get(0).y + offsetY);
    	        
    	        for (int i = 0; i < points.size(); i++) {
    	            polyPath.lineTo(points.get(i).x + Contract.PADDING_LEFT, points.get(i).y + offsetY);
    	            
    	        }
    	        polyPath.lineTo(points.get(0).x + Contract.PADDING_LEFT, points.get(0).y + offsetY);
    	
    	        // draw
    	        canvas.drawPath(polyPath, p);
    	        polyPath.reset();
                                    
    	        p.reset();

    	    }
        }
    }
    
    /*
     * get a text bounds size
     */
    
    private void drawMidGrad(Canvas canvas){
    	
    	int h = canvas.getHeight() - Contract.PADDING_TOP - Contract.PADDING_BOTTOM;   	
    	Path polyPath = new Path();
    	List<Point> points = chartDataBuilder.getPoints();
    	
    	float deltaX = (float)(points.get(1).x - points.get(0).x)/3;
    	float deltaY = (float)(points.get(1).y - points.get(0).y)/3;
    	Log.i("DEBUG","deltaY: " + String.valueOf(deltaY));
    	List<Point> points1 = new ArrayList<Point>();
    	points1.add(new Point(Math.round(deltaX),  h));
    	points1.add(new Point(Math.round(deltaX), h - points.get(0).y - Math.round(deltaY)));
    	points1.add(new Point(points.get(1).x - Math.round(deltaX), h - points.get(1).y + Math.round(deltaY)));
    	points1.add(new Point(points.get(1).x  - Math.round(deltaX) ,  h));
    	p.reset();
    	p.setStyle(Style.FILL);
		p.setAntiAlias(true);
				
		for( int j = 0; j < points.size(); j++){
			
			int maxY = 0;
    		if (points.get(1).y < points.get(2).y) maxY = points.get(1).y; 
    		else maxY = points.get(2).y;
    		p.setShader(new LinearGradient(0,points.get(0).y,0,maxY,0xfffff880,0xfffbc948, Shader.TileMode.CLAMP)); 
    		
    		polyPath.moveTo(points1.get(0).x + Contract.PADDING_LEFT, points1.get(0).y + offsetY);
	        
	        for (int i = 0; i < points1.size(); i++) {
	            polyPath.lineTo(points1.get(i).x + Contract.PADDING_LEFT, points1.get(i).y + offsetY);
	            
	        }
	        polyPath.lineTo(points1.get(0).x + Contract.PADDING_LEFT, points1.get(0).y + offsetY);
	
	        // draw
	        canvas.drawPath(polyPath, p);
	        polyPath.reset();
    		
		}
    	
    }
    
private void drawMidGrad1(Canvas canvas){
    	
    	int h = canvas.getHeight() - Contract.PADDING_TOP - Contract.PADDING_BOTTOM;   	
    	Path polyPath = new Path();
    	List<Point> points = chartDataBuilder.getPoints();
    	
    	float deltaX = (float)(points.get(3).x - points.get(2).x)/3;
    	float deltaY = (float)(points.get(3).y - points.get(2).y)/3;
    	Log.i("DEBUG","deltaY: " + String.valueOf(deltaY));
    	List<Point> points1 = new ArrayList<Point>();
    	points1.add(new Point(points.get(2).x + Math.round(deltaX),  h));
    	points1.add(new Point(points.get(2).x + Math.round(deltaX), h - points.get(2).y - Math.round(deltaY)));
    	points1.add(new Point(points.get(3).x - Math.round(deltaX), h - points.get(3).y + Math.round(deltaY)));
    	points1.add(new Point(points.get(3).x  - Math.round(deltaX) ,  h));
    	p.reset();
    	p.setStyle(Style.FILL);
		p.setAntiAlias(true);
				
		for( int j = 0; j < points.size(); j++){
			
			int maxY = 0;
    		if (points.get(1).y < points.get(2).y) maxY = points.get(1).y; 
    		else maxY = points.get(2).y;
    		p.setShader(new LinearGradient(0,points.get(0).y,0,maxY,0xfffff880,0xfffbc948, Shader.TileMode.CLAMP)); 
    		
    		polyPath.moveTo(points1.get(0).x + Contract.PADDING_LEFT, points1.get(0).y + offsetY);
	        
	        for (int i = 0; i < points1.size(); i++) {
	            polyPath.lineTo(points1.get(i).x + Contract.PADDING_LEFT, points1.get(i).y + offsetY);
	            
	        }
	        polyPath.lineTo(points1.get(0).x + Contract.PADDING_LEFT, points1.get(0).y + offsetY);
	
	        // draw
	        canvas.drawPath(polyPath, p);
	        polyPath.reset();
    		
		}
    	
    }
    
    private int getTextHeight(Paint p, String text){
    	Rect bounds = new Rect();
    	p.getTextBounds(text, 0, text.length(), bounds);
    	return bounds.height();
    }
    
    private int getTextWidth(Paint p, String text){
    	Rect bounds = new Rect();
    	p.getTextBounds(text, 0, text.length(), bounds);
    	return bounds.width();
    }
    
    
}

