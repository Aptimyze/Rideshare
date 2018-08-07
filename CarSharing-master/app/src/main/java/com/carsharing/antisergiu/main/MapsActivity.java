package com.carsharing.antisergiu.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.tts.Voice;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.String.valueOf;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapController mapController ;
    boolean flag = false ;
    private  static  int source = 0 ;
   /* private int mInterval = 10000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if ( mapController.mIsOriginSet && mapController.mIsDestinationSet ){
                    Intent ip = new Intent(getApplicationContext() , CreatePoolActivity.class) ;
                    stopRepeatingTask();
                    startActivity(ip);
                }
            } finally {
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle bundle =  getIntent().getExtras() ;
        source = Integer.parseInt(bundle.getString("source")) ;
        //new CheckParams().execute() ;
     //   mHandler = new Handler();
       // startRepeatingTask();
        new CheckParams(this).execute() ;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng jaipur = new LatLng(26.9597, 75.7387);
        mMap.addMarker(new MarkerOptions().position(jaipur).title("Marker in jaipur"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jaipur, 12));
        mapController = new MapController(mMap);
        // pass the current Context to  the AsyncTask class implementation

    }
    class CheckParams extends AsyncTask <Void, Void, Void> {
        public Context context ;
        Timer timer ;
        public CheckParams(Context context) {
            this.context = context ;
        }

        @Override
        protected  Void  doInBackground(Void... params) {
            return  null ;
        }
       // @Override
      //  protected Void doInBackground(Void... params) {
          //  Toast.makeText(getApplicationContext() , "i am here " , Toast.LENGTH_LONG).show();

           /* if ( mapController.mIsOriginSet && mapController.mIsDestinationSet ){
                Intent ip = new Intent(getApplicationContext() , CreatePoolActivity.class) ;
                startActivity(ip);
            }  */
            /*new Thread(new Runnable() {
               public void run() {
                   if (mapController.mIsOriginSet && mapController.mIsDestinationSet) {
                       flag = true ;
                       Log.d("what " , "i m inside the thread yet") ;
                   }
               }
           }).start();  */
           //
          // onClick(getParent());
         /*    */
           // Log.d("flag" , valueOf(flag)) ;
             //   if ( flag ){
                 //   Intent ip = new Intent( context , CreatePoolActivity.class );
                  //  Log.d ("whatt   " , "i was  there bro what");
                  //  context.startActivity(ip) ;
                   // ((Activity)context).finish();
               // }
         //  return null;
       // }
        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);

           /*  runOnUiThread(new Runnable() {
                public void run() {
                    if (mapController.mIsOriginSet && mapController.mIsDestinationSet) {
                        flag = true ;
                        Log.d("what " , "i m inside the thread yet") ;
                    }
                }
            });  */
            timer = new Timer();
            timer.scheduleAtFixedRate(  new TimerTask() {
                public void run() {

                        if ( mapController.mIsOriginSet && mapController.mIsDestinationSet ) {
                            flag = true  ;
                            timer.cancel();

                            Log.d("start" ,  mapController.mOrigin.toString()) ;
                            Log.d("end" , mapController.mDestination.toString()) ;
                            Intent intent ;
                            if ( source == 1)    intent    = new Intent(context, CreatePoolActivity.class);
                            else                 intent = new Intent(context , SearchPoolActivity.class) ;
                            intent.putExtra("startLat" , mapController.mOrigin.latitude) ;
                            intent.putExtra("startLong" , mapController.mOrigin.longitude) ;
                            intent.putExtra("endLat" , mapController.mDestination.latitude) ;
                            intent.putExtra("endLong" , mapController.mDestination.longitude) ;
                            startActivity(intent);
                            ((Activity) context).finish();
                            // startActivity(ip);
                        }

                }
            }, 0, 1000);
            Log.d("status" , "value is " + valueOf(flag) ) ;

        }

    }

}
