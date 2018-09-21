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

public class ColorTapView extends ImageView{
	private static final int MAX_LEVEL_TO_WIN = Util.BALLZ.length-1;
	private static int life = Util.MAX_LIFE;
	private static int lostLifeThisLevel = 0;
	private int level=0; 
	private static int THRESHOLD_DISAPPEAR = 50;
	private static int NUMBER_OF_BALLS, ballsAlive, difficultyCoef;
	private static double redShift, blueShift, greenShift;
	private float height=100, width=50;
	private static int points = 0;
	private static List<Integer> xs =  new ArrayList<Integer>();
	private static List<Integer> ys = new ArrayList<Integer>();
	private static List<Integer> xVelocities = new ArrayList<Integer>();
	private static List<Integer> yVelocities = new ArrayList<Integer>();
	private static List<Double> colorTime = new ArrayList<Double>();
	private static List<Boolean> decreaseColor = new ArrayList<Boolean>();
	private static List<Boolean> isDead = new ArrayList<Boolean>();
	private static List<Double> radii = new ArrayList<Double>();
	private static int propIndex1, propIndex2, propIndex3, propIndex4;
	private static Paint lifePaint = new Paint();
	private static Paint outerLifePaint = new Paint();
	private static Paint linePaint = new Paint();
	private static Paint levelPaint = new Paint();
	private static Paint bubblePaint = new Paint();
	private static Paint winPaint = new Paint();
	private static Paint colorPaint = new Paint();
	private static int xPosition = 0;
	private static int backgroundRed, backgroundGreen, backgroundBlue;
	private static boolean isWin = false, isLose = false;
	private Handler handler;
	
	private void reset(){
//		linePaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Util.FONT));
//		winPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Util.FONT));
//		bubblePaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Util.FONT));
//		levelPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), Util.FONT));
		lostLifeThisLevel = 0;
		backgroundRed = (int)Math.floor(215.0 + 40.0*Math.random());
		backgroundGreen = (int)Math.floor(215.0 + 40.0*Math.random());
		backgroundBlue =  (int)Math.floor(215.0 + 40.0*Math.random());
		propIndex1 = (int)Math.floor(Math.random()*Util.PROPS1.length);
		propIndex2 = (int)Math.floor(Math.random()*Util.PROPS2.length);
		propIndex3 = (int)Math.floor(Math.random()*Util.PROPS3.length);
		propIndex4 = (int)Math.floor(Math.random()*Util.PROPS4.length);
		isWin = false;
		isLose = false;
		ballsAlive=Util.BALLZ[level];
		NUMBER_OF_BALLS=Util.BALLZ[level];
		
		redShift = Math.random()*2;
		blueShift = Math.random()*2+3;
		greenShift = Math.random()*2+7;
		
		handler = new Handler();
		
		xs.clear();
		ys.clear();
		xVelocities.clear();
		yVelocities.clear();
		isDead.clear();
		radii.clear();
		decreaseColor.clear();
		
		for(int i=0;i<NUMBER_OF_BALLS;i++){
			colorTime.add(Math.random()*Util.MAX_COLOR_TIME);
			xs.add((int)Math.round(Math.random()*width));
			ys.add((int)Math.round(Math.random()*height*0.8));
			double theta = Math.random()*Math.PI*2.0;
			double r = (1.0 + 1.0*Math.random())*Math.sqrt(width*width + height*height)*0.002*(1+Math.sqrt(level/10.0+level*difficultyCoef/10.0));
			xVelocities.add((int)(Math.round(r*Math.cos(theta))));
			yVelocities.add((int)(Math.round(r*Math.sin(theta))));
			
			radii.add(0.0625*Math.random()*Math.sqrt(height*height+width*width)+0.03125*Math.sqrt(height*height+width*width));
			decreaseColor.add(false);
			isDead.add(false);
		}
		linePaint.setColor(Color.argb(Util.ALPHA_PARTIALLY_VISIBLE, 255, 255, 255));
		linePaint.setStrokeWidth(50);
		linePaint.setTextSize(30);
		bubblePaint.setColor(Color.BLACK);
		levelPaint.setColor(Color.BLACK);
		bubblePaint.setStrokeWidth(50);
		bubblePaint.setTextSize(100);
		levelPaint.setStrokeWidth(50);
		levelPaint.setTextSize(100);
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public ColorTapView(Context context, AttributeSet attrs){
		super(context, attrs);
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		lifePaint.setColor(Color.rgb(0, 255, 0));
		outerLifePaint.setColor(Color.rgb(0, 0, 0));
		handler = new Handler();
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		Display display = wm.getDefaultDisplay();
		life=Util.MAX_LIFE;
		if(currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB_MR2){
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;	
		}else{
			width = display.getWidth();  // deprecated
			height = display.getHeight();  // deprecated
		}
		reset();
	}
	
	public int howManyInBounds(float x, float y){
		int count = 0;
		double maxR = -1;
		int maxX=-1, maxY=-1;
		for(int i=0;i<NUMBER_OF_BALLS;i++){
			if( (xs.get(i)-radii.get(i) < x) && 
				(xs.get(i)+radii.get(i) > x) &&
				(ys.get(i)-radii.get(i) < y) &&
				(ys.get(i)+radii.get(i) > y) &&
				(maxR < radii.get(i))){
				maxR = radii.get(i);
				maxX = xs.get(i);
				maxY = ys.get(i);
			}
		}
		for(int i=0;i<NUMBER_OF_BALLS;i++){
			if( (maxX-maxR < xs.get(i)) && 
				(maxX+maxR > xs.get(i)) &&
				(maxY-maxR < ys.get(i)) &&
				(maxY+maxR > ys.get(i))){
				if(!isDead.get(i)){
					count++;
				}
			}
		}
		return count;
	}

	public int makeBallSmallerAndReturnTheDead(float x, float y){
		//Find Largest Radii
		int dead = 0;
		double maxR = -1;
		int maxX=-1, maxY=-1;
		for(int i=0;i<NUMBER_OF_BALLS;i++){
			if( (xs.get(i)-radii.get(i) < x) && 
				(xs.get(i)+radii.get(i) > x) &&
				(ys.get(i)-radii.get(i) < y) &&
				(ys.get(i)+radii.get(i) > y) &&
				(maxR < radii.get(i))){
				maxR = radii.get(i);
				maxX = xs.get(i);
				maxY = ys.get(i);
			}
		}
		for(int i=0;i<NUMBER_OF_BALLS;i++){
			if( (maxX-maxR < xs.get(i)) && 
				(maxX+maxR > xs.get(i)) &&
				(maxY-maxR < ys.get(i)) &&
				(maxY+maxR > ys.get(i))){
				radii.set(i, radii.get(i)*(.4+difficultyCoef/20.0));
				if(!isDead.get(i) && radii.get(i)<THRESHOLD_DISAPPEAR){
					radii.set(i, 0.0);	
					isDead.set(i, true);
					dead++;
				}
			}
		}
		return dead;
	}
	
	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			invalidate(); 
		}
	};

//	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.rgb(backgroundRed, backgroundGreen, backgroundBlue));
//		canvas.drawText(backgroundRed + " " + backgroundGreen + " " + backgroundBlue, width/2,height/2, outerLifePaint);
		//Drawing life box
		canvas.drawLine(width/2-1, (int)(height*0.03)-1, width, (int)(height*0.03)-1, outerLifePaint);
		canvas.drawLine(width, (int)(height*0.03)-1, width, (int)(height*0.06)+1, outerLifePaint);
		canvas.drawLine(width, (int)(height*0.06)+1, width/2-1, (int)(height*0.06)+1 ,outerLifePaint);
		canvas.drawLine(width/2-1, (int)(height*0.06)+1, width/2-1, (int)(height*0.03)-1, outerLifePaint);
		for(int i=0;i<NUMBER_OF_BALLS;i++){
			
			int x=xs.get(i);
			int y=ys.get(i);
			int xVelocity=xVelocities.get(i);
			int yVelocity=yVelocities.get(i);

	    	x += xVelocity;
	    	y += yVelocity;

	    	if ((x < 0)  || (x > width)) {
	    		if(x>width){
	    			x=(int)(width-1);
	    		}else{
	    			x=1;
	    		}
	    		xVelocity = -xVelocity;
	    	}
	    	if ((y < 0) || (y > height)) {
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
	    	
	    	//TODO: Add collision detection and add option for it in Preferences. 
			
		}
		int dead=0;
		for(int i=0;i<NUMBER_OF_BALLS;i++){
			double[] coefColors = Util.getCoefColors(colorTime.get(i), redShift, greenShift, blueShift);
			 
			colorPaint.setColor(Color.argb(Util.ALPHA_VISIBLE, (int)(coefColors[0]), (int)(coefColors[1]), (int)(coefColors[2])));
			if(!isLose){
//				radii.set(i,radii.get(i)*(1.0+Util.RATE_GROWTH*(Math.sqrt(level)+1)/4.0*(difficultyCoef+1.0)));	
				radii.set(i,radii.get(i) + Math.sqrt(height*height + width*width)*0.001*(1.0 + Util.RATE_GROWTH*Math.sqrt(level * difficultyCoef)));	
			}
			if(!isDead.get(i)){
				canvas.drawCircle(xs.get(i), ys.get(i), (int)Math.round(radii.get(i)), colorPaint);
			}
			if(radii.get(i) > (width)){
				life--;
				lostLifeThisLevel++;
			}
			if(isDead.get(i)){
				dead++;
			}
			lifePaint.setColor(Color.rgb((int)Math.round(255*(1-life/(double)Util.MAX_LIFE)), (int)Math.round(255.0*(life/(double)Util.MAX_LIFE)), 0));
			canvas.drawRect(width - (width*life)/(2*Util.MAX_LIFE), (int)(height*0.032), width, (int)(height*0.06), lifePaint);
			if(life == 0){
				isLose=true;
			}
			if(colorTime.get(i) > Util.MAX_COLOR_TIME){
				decreaseColor.set(i, true);
			}else if(colorTime.get(i) < 1){
				decreaseColor.set(i, false);
			}
			if(decreaseColor.get(i)){
				colorTime.set(i, colorTime.get(i)-.1);	
			}else{
				colorTime.set(i, colorTime.get(i)+.1);
			}
		    handler.postDelayed(runnable, Util.FRAME_RATE);		
		}
		
		ballsAlive = NUMBER_OF_BALLS-dead;
		if(isLose){
			linePaint.setTextSize(70);
			linePaint.setColor(Color.argb(255, (int)(Math.round(Math.random()*255)),(int)(Math.round(Math.random()*255)),(int)(Math.round(Math.random()*255))));
			if(xPosition < width){
				xPosition++;
			}else{
				xPosition = 0;
			}
			canvas.drawText("YOU LOSE", xPosition, height/2, linePaint);
		}else if(ballsAlive >= 1){
			canvas.drawText("SCORE " + points, (int)(width*0.05), (int)(height*0.05), bubblePaint);
			if(ballsAlive > 1){
				canvas.drawText("BUBBLES " + ballsAlive, (int)(width*0.05), (int)(height*0.10), bubblePaint);
			}else{
				canvas.drawText("BUBBLE " + ballsAlive, (int)(width*0.05), (int)(height*0.10), bubblePaint);
			}
		}else{
			winPaint.setTextSize(100);
			winPaint.setColor(Color.argb(255, (int)(Math.round(Math.random()*255)),(int)(Math.round(Math.random()*255)),(int)(Math.round(Math.random()*255))));
			if(xPosition < 50){
				xPosition++;
				if(xPosition<40){
					if(lostLifeThisLevel==0){
						canvas.drawText(Util.PROPS1[propIndex1], xPosition, height/2, winPaint);
					}else if(lostLifeThisLevel < 5){
						canvas.drawText(Util.PROPS2[propIndex2], xPosition, height/2, winPaint);
					}else if(lostLifeThisLevel < 10){
						canvas.drawText(Util.PROPS3[propIndex3], xPosition, height/2, winPaint);
					}else if(lostLifeThisLevel < 20){
						canvas.drawText(Util.PROPS4[propIndex4], xPosition, height/2, winPaint);
					}
				}else{
					canvas.drawText("LEVEL: " + (level + 2), xPosition-10, height/2, levelPaint);
				}
				isWin = true;
			}else{
				level++;
				if(level<MAX_LEVEL_TO_WIN){
					xPosition=0;
					reset();
				}else{
					winPaint.setTextSize(200);
					canvas.drawText("CONGRATS!", (int)(width*0.05), height/2, winPaint);
					canvas.drawText("YOU BEAT", (int)(width*0.05), (int)Math.round(height/2 * 1.3), winPaint);
					canvas.drawText("THE GAME!!", (int)(width*0.05), (int)Math.round(height/2 *1.6), winPaint);
				}
			}
		}
	}

	public boolean didYouLose(){
		return isLose;
	}
	public boolean didYouWin(){
		return isWin;
	}

	public void setDifficulty(int difficulty) {
		difficultyCoef= difficulty; 
	}

	public void setAddedPoints(int newPointsNow) {
		points = points + newPointsNow;
//		newPoints = newPointsNow;
	}
}
