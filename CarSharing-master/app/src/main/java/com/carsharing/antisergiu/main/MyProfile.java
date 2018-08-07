package com.carsharing.antisergiu.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.SimpleTimeZone;


public class MyProfile extends Activity {
    private static ProgressDialog pDialog ;
    private static  int suc = 0 ;
    private static JSONParser jParser ;
    private  static  JSONObject person ;
    Button logoutBtn ;
    private final static String URL = "http://10.0.2.2/ANDROID/fetchPerson.php" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        jParser = new JSONParser();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext()) ;
        String userName = pref.getString("USERNAME" ,null) ;
        if (userName == null) {
            Intent inp = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(inp);
        }
        else {
            new FetchPerson (getApplicationContext() , getBaseContext() ).execute();
        }
        logoutBtn = (Button)findViewById(R.id.loginBtn) ;
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext()) ;
                SharedPreferences.Editor ed = pref.edit() ;
                ed.putString("USERNAME" , null ) ;
                ed.commit();
                ed.putString("PASSWORD" , null ) ;
                ed.commit();
                ed.clear() ;
            }
        });
    }
    static class FetchPerson extends AsyncTask<Void , Void , Void > {
        private Context context, conBase;
        public FetchPerson ( Context context , Context conBase ){
            this.context = context ;
            this.conBase = conBase ;
            pDialog = new ProgressDialog(context) ;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   Toast.makeText(getApplicationContext() , "registering on the app" , Toast.LENGTH_LONG).show() ;

            pDialog.setTitle("loading profile.....");
            pDialog.setCancelable(false);
          //  pDialog.show();

            //   Toast.makeText(getApplicationContext() ,  String.valueOf(pDialog.getProgress()), Toast.LENGTH_LONG).show() ;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HashMap<String, String> params = new HashMap<> () ;
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(conBase) ;
            params.put ("uName" ,  pref.getString("USERNAME" , null ) ) ;
            try {
                JSONObject json = jParser.makeHttpRequest(URL , 2 , params);
                suc = Integer.parseInt(json.getString("success"));
                 person = json.getJSONObject("result" );
                Log.d("json111" , json.toString()) ;
            }
            catch ( Exception e ){
                e.printStackTrace();
            }

            return null ;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing())
                pDialog.cancel();

        }

        public void finishAll ( ){
            //   Log.d("IN THE LOOP","YO");
            ((Activity) context).onBackPressed();

            //((Activity)contextParent).finish();
            // fragmentTransaction.remove(yourfragment).commit()
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
