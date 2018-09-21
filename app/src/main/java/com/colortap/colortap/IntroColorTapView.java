package com.colortap.colortap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class IntroColorTapView extends ImageView{
	private static int NUMBER_OF_BALLS;
	private static double redShift, blueShift, greenShift;
	private float height=100, width=50;
	private static List<Boolean> decreaseSize = new ArrayList<Boolean>();
	private static List<Integer> xs =  new ArrayList<Integer>();
	private static List<Integer> ys = new ArrayList<Integer>();
	private static List<Integer> xVelocities = new ArrayList<Integer>();
	private static List<Integer> yVelocities = new ArrayList<Integer>();
	private static List<Double> colorTime = new ArrayList<Double>();
	private static List<Boolean> decreaseColor = new ArrayList<Boolean>();
	private static List<Double> radii = new ArrayList<Double>();
	private static int colorR, colorB, colorG;
	private static Paint colorPaint = new Paint();
	private Handler handler;
	private final int FRAME_RATE;

	
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public IntroColorTapView(Context context, AttributeSet attrs){
		super(context, attrs);
		colorR = (int)Math.floor(Math.random()*10+30);
		colorG = (int)Math.floor(Math.random()*10+30);
		colorB = (int)Math.floor(Math.random()*10+30);
		NUMBER_OF_BALLS =(int)(Math.floor(Math.random()*10+13));
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;

		
		redShift = Math.random()*2;
		blueShift = Math.random()*2+3;
		greenShift = Math.random()*2+7;
		handler = new Handler();
		for(int i=0;i< NUMBER_OF_BALLS;i++){
			decreaseSize.add(false);
			colorTime.add(Math.random()*Util.MAX_COLOR_TIME);
			xs.add((int)Math.round(Math.random()*(width-10)+5));
			ys.add((int)Math.round(Math.random()*(height-10)+5));
			xVelocities.add((int)(Math.pow(-1,(int)Math.round(Math.random()))*Math.round(Math.random()*(Util.MAX_SPEED-1)+1)));
			yVelocities.add((int)(Math.pow(-1,(int)Math.round(Math.random()))*Math.round(Math.random()*(Util.MAX_SPEED-1)+1)));
			
			radii.add(0.0625*Math.random()*Math.sqrt(height*height+width*width)+0.03125*Math.sqrt(height*height+width*width));
			decreaseColor.add(false);
		}
		FRAME_RATE = 50;
	}
	
	private Runnable r = new Runnable() {
		@Override
		public void run() {
			invalidate(); 
		}
	};
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.rgb(colorR, colorG, colorB));

		for(int i=0;i< NUMBER_OF_BALLS;i++){
			
			int x=xs.get(i);
			int y=ys.get(i);
			int xVelocity=xVelocities.get(i);
			int yVelocity=yVelocities.get(i);

	    	x += xVelocity;
	    	y += yVelocity;
	    	
	    	if ((x > width)  || (x < 0)) {
	    		if(x>width){
	    			x=(int)(width-1);
	    		}else{
	    			x=1;
	    		}
	    		xVelocity = -xVelocity;
	    	}
	    	if ((y > height) || (y < 0)) {
	    		if(y>height){
	    			y=(int)(height-1);
	    		}else{
	    			y=1;
	    		}
	    		yVelocity = -yVelocity;
	    	}
	    	
	    	xs.set(i, x);
	    	ys.set(i, y);
	    	xVelocities.set(i, xVelocity);
	    	yVelocities.set(i, yVelocity);
			
		}
		for(int i=0;i< NUMBER_OF_BALLS;i++){
			double[] coefColors = Util.getCoefColors(colorTime.get(i), redShift, greenShift, blueShift);
			colorPaint.setColor(Color.argb(Util.ALPHA_VISIBLE, (int)coefColors[0], (int)coefColors[1], (int)coefColors[2]));
			
			if(radii.get(i)*radii.get(i)*Util.PI > 0.40*height*width){
				decreaseSize.set(i, true);
			}else if(radii.get(i)*radii.get(i)*Util.PI < 0.005*height*width ){
				decreaseSize.set(i, false);
			}
			
			if(decreaseSize.get(i)){
				radii.set(i,radii.get(i)*(1.0-Util.RATE_GROWTH));
			}else{
				radii.set(i,radii.get(i)*(1.0+Util.RATE_GROWTH));
			}
			
			canvas.drawCircle(xs.get(i), ys.get(i), (int)Math.round(radii.get(i)), colorPaint);

			if(colorTime.get(i) > Util.MAX_COLOR_TIME){
				decreaseColor.set(i, true);
			}else if(colorTime.get(i) < 1){
				decreaseColor.set(i, false);
			}
			if(decreaseColor.get(i)){
				colorTime.set(i, colorTime.get(i)-.2);	
			}else{
				colorTime.set(i, colorTime.get(i)+.2);
			}
			
		    handler.postDelayed(r, FRAME_RATE);		
		}
	}
}
