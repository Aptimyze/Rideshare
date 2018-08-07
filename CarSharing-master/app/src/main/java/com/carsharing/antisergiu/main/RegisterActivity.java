package com.carsharing.antisergiu.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.String.valueOf;

public class RegisterActivity extends Activity {
    static int suc =0 ;
    static EditText email,phone,age,address,uname, pwd;
    private static ProgressDialog pDialog;
    static String name, contact, ag, mail, add, password ;
    static Button sgnup,loginBtn, regisBtn, loginUserBtn ;
    //public static Activity getActivity;
    static JSONParser jParser ;
    static int flag = 2;
    private static final String URL= "http://10.0.2.2/ANDROID/insertPerson.php";
    private static final String URL2 = "http://10.0.2.2/ANDROID/checkPerson.php" ;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registeruser);
     //   pDialog = new ProgressDialog(getApplicationContext());
        jParser = new JSONParser();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new RegisterActivity.PlaceholderFragment())
                    .commit();
        }

        regisBtn = (Button)findViewById(R.id.regisBtn) ;
        loginBtn = (Button)findViewById(R.id.loginBtn) ;

        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
               flag ^= 3 ;
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                flag ^= 3;
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //   getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }


     public static class PlaceholderFragment extends Fragment implements View.OnClickListener {

         public PlaceholderFragment() {

         }
         private  View createReferences( LayoutInflater inflater, ViewGroup container  ) {

             View view = inflater.inflate(R.layout.fragment_register, container, false) ;
             uname = (EditText) view.findViewById(R.id.uname);
             email = (EditText) view.findViewById(R.id.email);
             phone = (EditText) view.findViewById(R.id.phone);
             age = (EditText) view.findViewById(R.id.age);
             address = (EditText) view.findViewById(R.id.address);
             pwd = (EditText) view.findViewById(R.id.pwd);
             sgnup = (Button) view.findViewById(R.id.sgnup);
             sgnup.setOnClickListener(this);
             return view ;
         }
         @Override
         public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState) {
            View view ;
             if ( flag  ==  1){
                 //create References
                 view = createReferences (  inflater, container ) ;
             }
            else {
                 view = inflater.inflate(R.layout.fragment_register_user , container , false ) ;
                 uname = (EditText) view.findViewById(R.id.uname);
                 pwd = (EditText) view.findViewById(R.id.pwd);
                 loginUserBtn = (Button)view.findViewById(R.id.loginUserBtn) ;
                 loginUserBtn.setOnClickListener(this);
             }
             return view ;
         }

         @Override
         public void onClick (View v ){
                if ( flag == 1 ){

                    name = uname.getText().toString();
                    contact = phone.getText().toString();
                    ag = age.getText().toString();
                    add = address.getText().toString();
                    mail = email.getText().toString();
                    password = pwd.getText().toString();
                    int atpos = mail.indexOf("@");
                    int  dotpos = mail.lastIndexOf(".");

                    if ( name.compareTo("") == 0 ) Toast.makeText(getActivity().getApplicationContext(), "Please Enter the USER NAME" , Toast.LENGTH_LONG).show();
                    else if ( contact.compareTo("") == 0 || contact.length() != 10 || Pattern.matches("[0-9]+", contact) == false )  {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Enter the valid contact Number" , Toast.LENGTH_LONG).show();
                    }
                    else if (add.compareTo("") == 0 ){
                        Toast.makeText(getActivity().getApplicationContext(), "Please Enter the valid address" , Toast.LENGTH_LONG).show();
                    }
                    else if (atpos<1 || dotpos<atpos+2 || dotpos+2>= mail.length()) {
                        Toast.makeText(getActivity().getApplicationContext(), "Please Enter the valid Email-id" , Toast.LENGTH_LONG).show();
                    }
                    else {
                        new InsertPerson(getActivity() , getView().getContext() ).execute();
                    }
                        //Toast.makeText(getActivity().getApplicationContext() , String.valueOf(suc),Toast.LENGTH_LONG ).show();

                }
                else {
                    name = uname.getText().toString();
                    password = pwd.getText().toString();
                    new CheckUser( getActivity() , getView().getContext() ).execute();
                   // Toast.makeText(getActivity().getApplicationContext() , String.valueOf(suc),Toast.LENGTH_LONG ).show();
                   /* if ( suc == 1 ) {
                        setDefaults(getActivity().getBaseContext());
                        suc = 0;
                        getActivity().finish();
                    }   */
                }
         }

     }


     // TODO:  To insert a user into the person firebase database : IMP


    static class InsertPerson extends AsyncTask <Void , Void , Void > {

        private Context context, baseCon, contextParent  ;

        public InsertPerson ( Activity contextParent, Context context ){
            this.contextParent = contextParent.getApplicationContext() ;
            this.context = context ;
            this.baseCon = contextParent.getBaseContext();
            pDialog = new ProgressDialog(context) ;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   Toast.makeText(getApplicationContext() , "registering on the app" , Toast.LENGTH_LONG).show() ;
            pDialog.setTitle("Registering on the app.....");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HashMap<String, String> params = new HashMap<>();
            params.put("name", name);
            params.put("password", password);
            params.put("mail", mail);
            params.put("age", ag);
            params.put("address", add);
            params.put("contact", contact);
            try {
                JSONObject json = jParser.makeHttpRequest(URL , 2 , params);
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


            Log.d("success", valueOf(suc));
            if ( suc == 1 ) {
                setDefaults(baseCon);
                suc = 0;
                finishAll();
            }
        }
        public void finishAll ( ){

            ((Activity) context).onBackPressed();
        }

      }



       // TODO : ADD the api calls in the server
        static class CheckUser extends AsyncTask <Void , Void , Void >{
            private Context context, baseCon, contextParent  ;
            public CheckUser ( Activity contextParent, Context context ){
                this.contextParent = contextParent.getApplicationContext() ;
                this.context = context ;
                this.baseCon = contextParent.getBaseContext() ;
                pDialog = new ProgressDialog(context) ;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //   Toast.makeText(getApplicationContext() , "registering on the app" , Toast.LENGTH_LONG).show() ;

                pDialog.setTitle("Checking the user info.....");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HashMap<String, String> params = new HashMap<> () ;
                 params.put ("name" , name) ;
                params.put ("password" , password ) ;
                try {
                    JSONObject json = jParser.makeHttpRequest(URL2, 2 , params);
                    suc = Integer.parseInt(json.getString("success"));
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

                if ( suc == 1 ) {

                    setDefaults(baseCon);
                    suc = 0;
                    //finishAll();
                }

            }

            public void finishAll ( ){
                //   Log.d("IN THE LOOP","YO");
                ((Activity) context).onBackPressed();
                //((Activity)contextParent).finish();
                // fragmentTransaction.remove(yourfragment).commit()
            }

        }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    public static void setDefaults (Context con ) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con)  ;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("USERNAME" ,name) ;
        editor.commit();
        editor.putString("PASSWORD" , password);
        editor.commit();
        editor.clear() ;
           /*  editor.putString("EMAIL" , mail) ;
             editor.commit();
             editor.putString("CONTACT" ,contact) ;
             editor.commit();  */
        //Toast.makeText(getActivity().getApplicationContext(), prefs.getString("USERNAME" , null ) , Toast.LENGTH_LONG).show() ;
    }
}
