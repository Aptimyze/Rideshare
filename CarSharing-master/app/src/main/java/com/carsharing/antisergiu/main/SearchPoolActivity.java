package com.carsharing.antisergiu.main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class SearchPoolActivity extends Activity {
    EditText create_tv_time , create_tv_date ;
    Spinner timeSlide ,walkingDistance ;
    String startLat , startLong, endLat, endLong ;
    private  static Bundle bundle ;
    public static String convertToHour(int hourOfDay, int minute) {

        String hourString;
        if (hourOfDay < 10)
            hourString = "0" + hourOfDay;
        else
            hourString = "" +hourOfDay;

        String minuteSting;
        if (minute < 10)
            minuteSting = "0" + minute;
        else
            minuteSting = "" +minute;

        return hourString + " : " + minuteSting;
    }

    public static String convertToDate(int year, int month, int day) {
        String monthString;
        month++;
        if(month < 10) {
            monthString = "0" + month;
        } else {
            monthString = "" + month;
        }

        String dayString;
        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = "" + day;
        }

        return monthString + "-" + dayString + "-" + year;

    }

    public void showLoginDialog(View view) {

        LoginDialog loginDialog = new LoginDialog();
        loginDialog.show(this.getFragmentManager(), "fragment_login");
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

    public void showTimePickerDialog(View v) {
        Log.d("TIME", "shows the time");
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(this.
                getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(this.getFragmentManager(), "datePicker");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_pool);
        // get Intent data
        bundle = getIntent().getExtras() ;
        /*startLat = bundle.getString("startLat") ;
        startLong = bundle.getString("startLong");
        endLat = bundle.getString("endLat") ;
        endLong = bundle.getString("endLong") ; */
        //  create references
        create_tv_date = (EditText)findViewById(R.id.create_tv_date) ;
        create_tv_time = (EditText)findViewById(R.id.create_tv_time) ;
        walkingDistance = (Spinner)findViewById(R.id.walkingDistance) ;
        timeSlide = (Spinner)findViewById(R.id.timeSlide) ;
        Calendar c = Calendar.getInstance();
        int minute = c.get(Calendar.MINUTE);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        String time = convertToHour(hour, minute);
        ((TextView) findViewById(R.id.create_tv_time)).setText(time);


        // set current date
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        ((TextView) findViewById(R.id.create_tv_date)).setText(convertToDate(year, month, day));

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_pool, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void searchPool(View view) {
        Intent matchingPoolIntent = new Intent(this, MatchingPoolsActivity.class);
        matchingPoolIntent.putExtra("poolDate" , create_tv_date.getText().toString() ) ;
        matchingPoolIntent.putExtra("poolTime" , create_tv_time.getText().toString()) ;
        matchingPoolIntent.putExtra("timeWindow" , timeSlide.getSelectedItem().toString() ) ;
        matchingPoolIntent.putExtra("walkingDistance" , walkingDistance.getSelectedItem().toString()) ;
        matchingPoolIntent.putExtra("startLat", bundle.get("startLat").toString()) ;
        matchingPoolIntent.putExtra("startLong" , bundle.get("startLong").toString()) ;
        matchingPoolIntent.putExtra("endLat" , bundle.get("endLat").toString()) ;
        matchingPoolIntent.putExtra("endLong" , bundle.get("endLong").toString()) ;
        startActivity(matchingPoolIntent);
    }
}
