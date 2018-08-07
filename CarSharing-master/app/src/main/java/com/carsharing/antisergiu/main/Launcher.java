package com.carsharing.antisergiu.main;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.*;;

public class Launcher extends Activity {

    TextView tv1, tv2;
    ImageView iv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        // tv1 = (TextView)findViewById(R.id.textView1);
        // tv2 = (TextView)findViewById(R.id.textView2);
        // iv1 = (ImageView)findViewById(R.id.imageView1);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                finally{
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }
            }
        };

        timer.start();
    }

}
