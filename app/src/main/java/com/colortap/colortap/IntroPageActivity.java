package com.colortap.colortap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class IntroPageActivity extends Activity {
	private static final int PARTIALLY_VISIBLE = 200;
	private static Button start, setting, quit;
	private MediaPlayer introPlayer = new MediaPlayer();	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.activity_intro_page);
		
		loadUiComponents();
		playSong();

		quit.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				introPlayer.stop(); 
				introPlayer.reset();
				finish();
			}
		});

		setting.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				introPlayer.stop(); 
				introPlayer.reset();
				Intent settingIntent;
				settingIntent = new Intent(getApplicationContext(), Preferences.class);
				startActivity(settingIntent);
			}
		});
		
		start.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(IntroPageActivity.this);
				builder.setTitle("Difficulty Level:").setItems(R.array.difficulty, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialogInterface, int i){
						Bundle bundle = new Bundle();
						bundle.putInt(Util.DIFFICULTY_KEY, i);
						introPlayer.stop(); 
						introPlayer.reset();
						Intent startIntent;
						startIntent = new Intent(getApplicationContext(), MainActivity.class);
						startIntent.putExtras(bundle);
						startActivity(startIntent);
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});
		
	}

	private void loadUiComponents() {
		
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		int width, height;
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;

		
		start = (Button)findViewById(R.id.start);
		setting = (Button)findViewById(R.id.setting);
		quit = (Button)findViewById(R.id.quit);
		
		start.setHeight(height/10);
		setting.setHeight(height / 10);
		quit.setHeight(height / 10);

		start.setWidth(width/3);
		setting.setWidth(width/3);
		quit.setWidth(width/3);
		
//		start.setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT));
//		setting.setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT));
//		rate.setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT));
//		email.setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT));
//		quit.setTypeface(Typeface.createFromAsset(getAssets(), Util.FONT));
		
		
		start.getBackground().setAlpha(PARTIALLY_VISIBLE);
		setting.getBackground().setAlpha(PARTIALLY_VISIBLE);
		quit.getBackground().setAlpha(PARTIALLY_VISIBLE);
	}
	
	private void playSong() {
		if(Preferences.getMusic(getApplicationContext())){
			try {
                introPlayer = MediaPlayer.create(IntroPageActivity.this, R.raw.intro);
                introPlayer.setVolume(100,100);
                introPlayer.setLooping(true);
                introPlayer.start();
 
			} catch (Exception e) {
				Log.v("Exception", e.getLocalizedMessage());}
		}
	}

	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			introPlayer.stop();
			introPlayer.reset();
			finish();
			return true;
		}
		return false;
	}
	
	@Override
	protected void onResume() {
		if(introPlayer != null && !introPlayer.isPlaying()){
			playSong();
		}
		super.onResume();
	}


	@Override
	protected void onPause() {
		if(introPlayer.isPlaying()){
			introPlayer.stop();
			introPlayer.reset();
		}
		super.onPause();
	}
}