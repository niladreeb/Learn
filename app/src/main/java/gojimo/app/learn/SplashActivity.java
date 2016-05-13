package gojimo.app.learn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class SplashActivity extends AppCompatActivity {

    private static FileInputStream fis=null;
    private static String jsonStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        StartAnimations();

        //Load data
        try {
            File file = new File (this.getFilesDir(), "json_data");
            if(file.exists())
                fis = openFileInput("json_data");

            if( fis==null || fis.read()==-1) {

                new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over

                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        i.putExtra("JSON","");
                        startActivity(i);

                        // close app
                        finishAffinity();
                    }
                }, 3000);
            }
            else{

                int content;
                while ((content = fis.read()) != -1)
                {
                    jsonStr = jsonStr+ ((char)content);
                }

                jsonStr = "["+jsonStr;      //Bug fix

                new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                    @Override
                    public void run() {
                        // This method will be executed once the timer is over

                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        i.putExtra("JSON",jsonStr);
                        startActivity(i);

                        // close app
                        finishAffinity();
                    }
                }, 3000);
                fis.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        FrameLayout fl=(FrameLayout) findViewById(R.id.frameLayout);
        fl.clearAnimation();
        fl.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView tv = (TextView) findViewById(R.id.fullscreen_content);
        tv.clearAnimation();
        tv.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TextView tv = (TextView) findViewById(R.id.fullscreen_content);
                assert tv != null;
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }


}
