package com.colortap.colortap;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private static final int[] SONG_IDs = {
            R.raw.t01_12ad_earth,
            R.raw.t02_12ad_interplanetarywar,
            R.raw.t06_12ad_neptune,
            R.raw.t01_12ad_earth,
            R.raw.t05_12ad_mercury,
            R.raw.t01_12ad_earth,
            R.raw.t06_12ad_neptune,
            R.raw.t02_12ad_interplanetarywar,
            R.raw.t06_12ad_neptune,
            R.raw.t02_12ad_interplanetarywar,
            R.raw.t04_12ad_lostplanet,
            R.raw.t05_12ad_mercury,
            R.raw.t06_12ad_neptune,
    };
    private static int difficulty;
    private ColorTapView ballView;
    private static MediaPlayer player = new MediaPlayer();
    //	private static Launcher m_launcher;

    BackgroundSound mBackgroundSound = new BackgroundSound();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    setContentView(R.layout.activity_main);





    Bundle getData = getIntent().getExtras();
    difficulty = getData.getInt(Util.DIFFICULTY_KEY);
    ballView = (ColorTapView)findViewById(R.id.bouncingball_view);
    ballView.setDifficulty(difficulty);
    ballView.setOnTouchListener(new View.OnTouchListener() {
        @Override
        public boolean onTouch(View arg0, MotionEvent arg1) {
            int count = ballView.howManyInBounds(arg1.getX(), arg1.getY());
            if(ballView.didYouLose()){
                ballView.destroyDrawingCache();
                mBackgroundSound.cancel(true);
                player.stop();
                finish();
            }
            if(count > 0){
                int dead = ballView.makeBallSmallerAndReturnTheDead(arg1.getX(),arg1.getY());
                ballView.setAddedPoints(dead*(dead-1)+1);
            }
            return false;
        }
    });

}

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mBackgroundSound.cancel(true);
            player.stop();
            finish();
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            mBackgroundSound.cancel(true);
            player.stop();
            finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_newgame:
                ballView.destroyDrawingCache();
                finish();
                startActivity(getIntent());
                return true;
            case R.id.menu_settings:
                Intent settingIntent;
                settingIntent = new Intent(getApplicationContext(), Preferences.class);
                startActivity(settingIntent);
            case R.id.menu_quit:
                ballView.destroyDrawingCache();
                mBackgroundSound.cancel(true);
                player.stop();
                finish();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

public class BackgroundSound extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        playSong();
        return null;
    }

}

    private void playSong() {
        if(Preferences.getMusic(getApplicationContext())){
            try {
                //        		player.reset();
                player = MediaPlayer.create(MainActivity.this, SONG_IDs[(int) (Math.floor(Math.random() * SONG_IDs.length))]);
                player.setVolume(100,100);
                player.start();

                // Setup listener so next song starts automatically
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    public void onCompletion(MediaPlayer arg0) {
                        player.reset();
                        player.release();
                        playSong();
                    }

                });

            } catch (Exception e) {}
        }
    }


    public void onResume() {
        super.onResume();
        if(!player.isPlaying()){
            mBackgroundSound.execute();
        }
    }

    public void onPause() {
        super.onPause();
        mBackgroundSound.cancel(true);
    }

    public void onStop() {
        super.onStop();
        mBackgroundSound.cancel(true);
    }

    @Override
    public void onDestroy() {
        mBackgroundSound.cancel(true);
        super.onDestroy();
    }
}
