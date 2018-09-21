package com.colortap.colortap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class Splash extends Activity {
	private final int TOAST_DISPLAY_LENGTH = 2000;
	private static final String FONT_QUOTE = "assets/AYearWithoutRain.ttf";
	private static final String FONT_AUTHOR = "assets/AYearWithoutRain.ttf";
	
	private static TextView quote;
	private static TextView author;
	
	private static final String[][] QUOTES = {
		{"To the mind that stands still\nThe whole universe surrenders.","Lao Tzu"},
		{"The laws of nature are but\nThe mathematical thoughts of God.","Euclid"},
		{"Imagination\nIs more important than knowledge.","Einstein"},
		{"Geometry\nExisted before creation.","Plato"},
		{"It is during our darkest moments\nThat we must focus to see the light.","Aristotle"},
		{"After climbing a great hill,\nOne only finds that\nThere are many more hills to climb.", "Mandela"},
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		quote = (TextView)findViewById(R.id.splash_quote);
        author = (TextView)findViewById(R.id.splash_author);
        
        quote.setTextSize(19);
        author.setTextSize(17);
        
//        quote.setTypeface(Typeface.createFromAsset(getAssets(), FONT_QUOTE));
//        author.setTypeface(Typeface.createFromAsset(getAssets(), FONT_AUTHOR));
        
        int indexRandomQuote = (int)Math.floor(Math.random()*QUOTES.length);
        String randomQuote = QUOTES[indexRandomQuote][0];
        String randomAuthor = "-"+QUOTES[indexRandomQuote][1];
        
        quote.setText(randomQuote);
        author.setText(randomAuthor);
        
        new Handler().postDelayed(new Runnable(){
        	@Override
        	public void run() {
        		Intent mainIntent = new Intent(Splash.this,IntroPageActivity.class);
        		Splash.this.startActivity(mainIntent);
        		Splash.this.finish();
        	}
        }, TOAST_DISPLAY_LENGTH);
	}
}
