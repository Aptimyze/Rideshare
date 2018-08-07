package com.carsharing.antisergiu.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.String.valueOf;

public class MatchingPoolsActivity extends Activity {
    private  static JSONParser jParser ;
    private  static  ProgressDialog pDialog ;
    private static final String URL = "http://10.0.2.2/ANDROID/searchPool.php" ;
    private  static  int suc = 0 ;
    private static  ArrayList < JSONObject > list;
    private static Bundle bundle ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_pools);
      //  list = new ArrayList<JSONObject>() ;
        bundle = getIntent().getExtras() ;
       // pDialog  = new ProgressDialog(getApplicationContext()) ;
        jParser = new JSONParser() ;
        RoutesAdapter adapter = new RoutesAdapter(getApplicationContext());
        ListView listView = (ListView) findViewById(R.id.matching_pools_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showRoute(position) ;
            }
        });
    }
    public void showRoute(int index ) {

        ShowRouteDialog showRouteDialog = new ShowRouteDialog();
        Bundle args = new Bundle();
        try {
            String startLat, startLong, endLat, endLong, poolId;
            JSONObject tmp = list.get(index) ;
            startLat = tmp.getString("startLat") ;
            startLong = tmp.getString("startLong") ;
            endLat = tmp.getString("endLat") ;
            endLong = tmp.getString("endLong") ;
            //poolId = tmp.get("poolId").toString() ;
            args.putString("startLat", startLat);
            args.putString("startLong", startLong);
            args.putString("endLat", endLat);
            args.putString("endLong", endLong);
           // args.putString("poolId" , poolId);
        }
        catch ( Exception e ){
            e.printStackTrace();
        }
        showRouteDialog.setArguments(args);
        showRouteDialog.show(this.getFragmentManager(), "fragment_show_route");
    }
    static class FetchPool extends AsyncTask <Void , Void , Void > {

        HashMap<String, JSONObject > res ;
        Context context ;
        public  FetchPool (  Context context ){
            pDialog = new ProgressDialog(context.getApplicationContext()) ;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   Toast.makeText( context , "registering on the app " , Toast.LENGTH_LONG).show() ;

          /*  pDialog.setTitle("Searching for the matching pool.....");
            pDialog.setCancelable(false);
             pDialog.show();   */

            //   Toast.makeText(getApplicationContext() ,  String.valueOf(pDialog.getProgress()), Toast.LENGTH_LONG).show() ;
        }


        @Override
        protected Void doInBackground(Void... arg0) {
            HashMap<String, String> params = new HashMap<>();
            params.put("startLat", bundle.getString("startLat"));
            params.put("startLong", bundle.getString("startLong"));
            params.put("endLat", bundle.getString("endLat"));
            params.put("endLong", bundle.getString("endLong"));
            params.put("poolDate" , bundle.getString("poolDate"));
            params.put("poolTime",  bundle.getString("poolTime"));
            params.put("timeWindow", bundle.getString("timeWindow")) ;
            params.put("walkingDistance" , bundle.getString("walkingDistance")) ;
            try {
                 JSONObject json = jParser.makeHttpRequest(URL, 2, params);
                 Log.d("json", json.toString()) ;

                  JSONArray jArray = json.getJSONArray("result") ;
                for ( int i = 0 ; i < jArray.length() ; i++ ){
                    list.add((JSONObject)jArray.get(i)) ;
                }
                suc = Integer.parseInt(json.getString("success"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
           /* if (pDialog.isShowing())
              pDialog.cancel();  */
            //  Toast.makeText(RegisterActivity.this , String.valueOf(suc) , Toast.LENGTH_LONG).show();
     //       Log.d("success", valueOf(suc));
   //         Toast.makeText( context , "Done " , Toast.LENGTH_LONG).show() ;

        }


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

    /**
     * A placeholder fragment containing a simple view.
     */

/*    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_matching_pools, container, false);

            RoutesAdapter adapter = new RoutesAdapter();
            ListView listView = (ListView) rootView.findViewById(R.id.matching_pools_listview);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showRoute(view);

                }
            });


          return rootView;
        }


        public void showRoute(View view) {

                ShowRouteDialog showRouteDialog = new ShowRouteDialog();
            Bundle args = new Bundle();
            //ContentValues args = new ContentValues() ;
            args.putDouble("SOURCE_LAT", 50);
            args.putDouble("SOURCE_LONG", 51);
            args.putDouble("DEST_LAT", 50.05);
            args.putDouble("DEST_LONG", 51.07);
            showRouteDialog.setArguments(args);
                showRouteDialog.show(this.getFragmentManager(), "fragment_show_route");
            }
        private class RoutesAdapter extends BaseAdapter {

            private ArrayList<Route> routes;

            public RoutesAdapter() {
                routes = new ArrayList<Route>();
                Time t = new Time();
                t.setToNow();
                routes.add(new Route("Brit",3,20, 1, 3, 3, 12, 23, new LatLng(1,1), new LatLng(2,2)));
                t.set(0,1,2,3,4,5);
                routes.add(new Route("Hanna",2,20, 3, 2, 1, 23, 12, new LatLng(1,1), new LatLng(2,2)));
            }

            public int getCount() {
                return routes.size();
            }



            public Route getItem(int position) {
                return routes.get(position);
            }

            public long getItemId(int position) {
                return routes.get(position).getId();
            }

            public View getView(int position, View convertView, ViewGroup parent) {

                LinearLayout layout;
                Route currRoute = routes.get(position);

                if (convertView == null) {
                    layout = new LinearLayout(parent.getContext());
                    layout.setOrientation(LinearLayout.VERTICAL);

                    TextView timeView = new TextView(parent.getContext());
                    timeView.setText(String.format("%02d", currRoute.getDepatureDate()) + "/" + String.format("%02d", currRoute.getDepatureMonth()) + "/" + currRoute.getDepatureYear() + " " + String.format("%02d", currRoute.getDepatureHour()) + ":" + String.format("%02d", currRoute.getDepatureMinute()));
                    timeView.setTextSize(18);
                    timeView.setTypeface(null, Typeface.BOLD);
                    timeView.setTextColor(Color.DKGRAY);
                    layout.addView(timeView);

                    TextView driverView = new TextView(parent.getContext());
                    driverView.setText("Driver: " + currRoute.getDriver());
                    driverView.setTextSize(18);
                    driverView.setTextColor(Color.DKGRAY);
                    layout.addView(driverView);

                    TextView priceView = new TextView(parent.getContext());
                    priceView.setText("Price: " + currRoute.getPrice() + "$");
                    priceView.setTextSize(18);
                    priceView.setTextColor(Color.DKGRAY);
                    layout.addView(priceView);

                }
                else {
                    layout = (LinearLayout) convertView;
                }

                return layout;
            }

        }


       } */
    private static  class RoutesAdapter extends BaseAdapter {

        public RoutesAdapter(Context context) {
            list = new ArrayList<JSONObject>();
            new FetchPool(context).execute();
        }

        public int getCount() {
            return list.size();
        }



        public JSONObject getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            long itmId = 0 ;
            try {
                 itmId = Integer.parseInt(list.get(position).getString("poolId"));
             }
              catch ( Exception e ){
                e.printStackTrace();
            }
            return  itmId ;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LinearLayout layout;
            JSONObject currPool = list.get(position);

            if (convertView == null) {
                layout = new LinearLayout(parent.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);

                try {
                    String poolDate, poolTime, userName;
                    poolDate = currPool.getString("poolDate") ;
                    poolTime = currPool.getString("poolTime") ;
                    userName = currPool.getString("USERNAME") ;
                    TextView timeView = new TextView(parent.getContext());
                    timeView.setText("POOL TIME : " + poolDate );
                    timeView.setTextSize(18);
                   // timeView.setTypeface(null, Typeface.BOLD);
                    timeView.setTextColor(Color.DKGRAY);
                    layout.addView(timeView);

                    TextView tmeView = new TextView(parent.getContext());
                    tmeView.setText("POOL TIME : " + poolTime );
                    tmeView.setTextSize(18);
                    //tmeView.setTypeface(null, Typeface.BOLD);
                    tmeView.setTextColor(Color.DKGRAY);
                    layout.addView(tmeView);

                    TextView driverView = new TextView(parent.getContext());
                    driverView.setText("DRIVER: " +  userName +"\n");
                    driverView.setTextSize(18);
                    driverView.setTextColor(Color.DKGRAY);
                    layout.addView(driverView);

                }
                catch (Exception e ){
                    e.printStackTrace();
                }




            }
            else {
                layout = (LinearLayout) convertView;
            }

            return layout;
        }

    }

}
