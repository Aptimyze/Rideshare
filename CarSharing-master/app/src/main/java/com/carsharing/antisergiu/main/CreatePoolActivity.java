package com.carsharing.antisergiu.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreatePoolActivity extends Activity {
    EditText create_tv_date , create_tv_time ;
    Switch weeklyPool ;
    String startLat, startLong, endLat , endLong ;
    Spinner sp ;
    String fiDate, time ;

    public static String convertToHour(int hourOfDay, int minute) {

        return ( hourOfDay < 10 ? "0" : "" ) + hourOfDay + ":" +  ( minute < 10 ? "0" : "" ) +  minute ;

    }

    public static String convertToDate(int year, int month, int day) {
        ++month;
        return (  day < 10 ? "0" : "" ) + day + "-" + ( month < 10 ? "0" : "" ) +  "-"  + year ;
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            ((TextView) getActivity().findViewById(R.id.create_tv_time)).setText(convertToHour(hourOfDay, minute));

        }
    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);


            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            ((TextView) getActivity().findViewById(R.id.create_tv_date)).setText(convertToDate(year, month, day));
        }
    }

    public void showLoginDialog(View view) {

      /*  LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(this.getFragmentManager(), "fragment_login");
        EditText regis  = (EditText)findViewById(R.id.regis_name) ;  */


       // pool creation  time
        String dateOfPoolCreation  = create_tv_date.getText().toString();
        String timeOfPoolCreation  = create_tv_time.getText().toString();

        // current datetime
        Date today = Calendar.getInstance().getTime() ;
        SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyy::HH:mm");



        // validate the  created pool dateTime
        try {
            Date poolCreationDateTime = fmt.parse(dateOfPoolCreation + "::" + timeOfPoolCreation );
            System.out.println( poolCreationDateTime  ) ;
            System.out.println( today ) ;
            if ( today.after ( poolCreationDateTime ) ) {
                Toast.makeText(getApplicationContext() , "Choose the dateTime which is later than the current time" , Toast.LENGTH_LONG).show();
                return ;
            }
        }
        catch ( Exception e ){
            Toast.makeText(getApplicationContext() , "Some parsing Exception occured" , Toast.LENGTH_LONG).show();
        }

        /* save the pool details in the firebase db
           If the user is logged through sharedPreferences in otherwise prompt the login dailog box  */


        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String userName = pref.getString("USERNAME", null);

        //  Toast.makeText(getApplicationContext(), userName , Toast.LENGTH_LONG).show();
        if (userName == null) {
            // launch the fragment for the login or signup activity
            Intent inp = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(inp);
        }

        userName = pref.getString("USERNAME", null);

        if (userName != null) {
            Bundle bundle = new Bundle();
            bundle.putString("poolDate", dateOfPoolCreation );
            bundle.putString("poolTime", timeOfPoolCreation );
            bundle.putString("weeklyPool", weeklyPool.getText().toString() );
            bundle.putString("seats", sp.getSelectedItem().toString() );
            bundle.putString("userMail", userName );
            bundle.putString("startLat", startLat);
            bundle.putString("startLong", startLong);
            bundle.putString("endLat", endLat);
            bundle.putString("endLong", endLong);
            bundle.putString("userName", userName);
            //  bundle.putString("curDate" , date);
            //   bundle.putString("curTime" , time) ;
            LoginDialog loginDialog = new LoginDialog();
            loginDialog.setArguments(bundle);
            loginDialog.show(this.getFragmentManager(), "fragment_login");
        }

    }

    private void createReferences () {
        create_tv_date = (EditText)findViewById(R.id.create_tv_date)  ;
        create_tv_time = (EditText)findViewById(R.id.create_tv_time) ;
        weeklyPool = (Switch)findViewById(R.id.weeklyPool) ;
        sp = (Spinner)findViewById(R.id.totalSeats) ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pool);


        // get the location data
        Bundle inp = getIntent().getExtras();
        startLat = inp.get("startLat").toString() ;
        startLong = inp.get("startLong").toString() ;
        endLat = inp.get("endLat").toString() ;
        endLong = inp.get("endLong").toString() ;


        // set  current time
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);


        createReferences() ;
        create_tv_time.setText(time);

        // set current date
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        String date = convertToDate(year,month,day) ;
        // code to reverse the date
        create_tv_date.setText(date);

        fiDate = date.split("-" )[1]  ;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.create_pool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showTimePickerDialog(View v) {
       // Log.d("TIME", "shows the time");
        new TimePickerFragment().show(this.getFragmentManager(), "pick up time");
    }

    public void showDatePickerDialog(View view) {
        Log.d("DATE" , "shows the date") ;
        new DatePickerFragment().show(  this.getFragmentManager(), "pick up date" );
    }
}