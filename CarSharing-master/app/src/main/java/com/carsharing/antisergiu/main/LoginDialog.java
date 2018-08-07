package com.carsharing.antisergiu.main;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;

// LOGIN LOGIC
public class LoginDialog extends DialogFragment {
    public static SharedPreferences pref ;
    private static int suc = 0 ;
    EditText userName, password, telephone;
    private static  JSONParser jParser  ;
    private static final String URL= "http://10.0.2.2/ANDROID/insertPool.php";
    private  static Bundle bundle ;
    //String poolDate, poolTime, weeklyPool, totalSeat, StartLat, startLong, endLat, endLong ;
    private EditText mEditText;
    private LoginDialog mdialog = this;
    public static int flag = 0;
    public String startLat;
    private static  ProgressDialog pDialog ;
    public LoginDialog() {

    }
    public  void getDefaults (Context con ){
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()) ;
        if ( flag == 1 ){
            SharedPreferences.Editor ed = pref.edit() ;
            ed.putString("USERNAME" , null ) ;
            ed.commit();
            ed.putString("PASSWORD" , null ) ;
            ed.commit();
            ed.clear() ;
            flag = 0 ;
            mdialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        getDefaults(getActivity().getBaseContext()) ;
        bundle = getArguments() ;
        jParser = new JSONParser() ;
        final String password = pref.getString("PASSWORD" , null) ;
        view = inflater.inflate(R.layout.fragment_login, container);
        getDialog().setTitle("Please confirm password!");
        mEditText = (EditText) view.findViewById(R.id.login_et_password);
        ((Button) view.findViewById(R.id.loginConfirmBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mEditText.getText().toString().compareTo(password) == 0 ){
                   // Toast.makeText(getActivity().getApplicationContext(), "Car Pool Saved!" , Toast.LENGTH_LONG).show();

                    pDialog = new ProgressDialog(getView().getContext()) ;
                    new InsertNewPool(getActivity().getApplicationContext()).execute() ;
                    mdialog.dismiss();
                }
                else {
                    mEditText.setText("");
                    Toast.makeText(getActivity().getApplicationContext(), "Enter the right password" , Toast.LENGTH_LONG).show();
                }
            }
        });
       ((Button)view.findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1 ;
                getDefaults(getActivity().getBaseContext());
               // mdialog.dismiss();
            }
        });
        return view;
    }


    // AsyncTask Method to store the  car pool details

    static class InsertNewPool extends AsyncTask <Void , Void , Void > {

        Context context ;
        public InsertNewPool ( Context con ){
            this.context = con ;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   Toast.makeText(getApplicationContext() , "registering on the app" , Toast.LENGTH_LONG).show() ;

            pDialog.setTitle("Saving  Car pool!!");
            pDialog.setCancelable(false);
            pDialog.show();

            //   Toast.makeText(getApplicationContext() ,  String.valueOf(pDialog.getProgress()), Toast.LENGTH_LONG).show() ;
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HashMap<String, String> params = new HashMap<>();
            params.put("poolDate" , bundle.getString("poolDate") );
            params.put("poolTime" , bundle.getString("poolTime")) ;
          //  params.put("weekly" , bundle.getString("weeklyPool") );
            params.put("weekly" , "false") ;
            params.put("totalSeat" , bundle.getString("seats")) ;
            params.put("startLat" , bundle.getString("startLat"));
            params.put("startLong" , bundle.getString("startLong" )) ;
            params.put("endLat" ,  bundle.getString("endLat"));
            params.put("endLong" , bundle.getString("endLong")) ;
            params.put("userName" , bundle.getString("userName")) ;
         //   params.put("userName" , bundle.getString("userName")) ;
          //  Log.d("params"  , params.toString()) ;
            try {
                JSONObject json = jParser.makeHttpRequest(URL, 2, params);
                Log.d("json", json.toString()) ;
                suc = Integer.parseInt(json.getString("success"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.cancel();
            //Log.d("success", valueOf(suc));
               if ( suc == 1 ) {
                    suc = 0;
                    Toast.makeText(context, "Car pool Saved!!!", Toast.LENGTH_LONG).show();
                }
                else   Toast.makeText(context, "Error while saving car pool!!!", Toast.LENGTH_LONG).show();
            }

        }


    }
