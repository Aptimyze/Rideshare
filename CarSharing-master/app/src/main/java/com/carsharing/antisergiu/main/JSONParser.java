/*package com.carsharing.antisergiu.main;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

*/
package com.carsharing.antisergiu.main;

import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

import javax.net.ssl.HttpsURLConnection;

public class JSONParser {
    static String response = null;
    static JSONObject jObj = null;
    public final static int GETRequest = 1;
    public final static int POSTRequest = 2;

    //Constructor with no parameter
    public JSONParser() {

    }


    public JSONObject makeHttpRequest(String url, int requestMethod) {
        return this.makeHttpRequest(url, requestMethod, null);
    }
    public JSONObject makeHttpRequest(String urlAddress, int requestMethod,
                                     HashMap<String, String> params) {
        URL url;
        String response="" ;

        try {
            url = new URL(urlAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15001);
            conn.setConnectTimeout(15001);
            conn.setDoInput(true);
            conn.setDoOutput(true);


            if (requestMethod == POSTRequest ) {
                conn.setRequestMethod("POST");
            }
            else if (requestMethod == GETRequest ) {
                conn.setRequestMethod("GET");
            }

            if (params != null) {

                OutputStream ostream = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(ostream,"UTF-8"));
                StringBuilder requestResult = new StringBuilder();
                boolean first = true;
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (first)
                        first = false;
                    else
                        requestResult.append("&");
                    requestResult.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                    requestResult.append("=");
                    requestResult.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
                }

          //      Log.d("result" , requestResult.toString() ) ;
                writer.write(requestResult.toString());

                writer.flush();
                writer.close();
                ostream.close();
            }
            int reqresponseCode = conn.getResponseCode() ;
        //    Log.d("code" , String.valueOf(reqresponseCode));

            if (reqresponseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response="got failed";
            }

      //      Log.d("Code should be " , String.valueOf(HttpURLConnection.HTTP_OK)) ;
       //     Log.d("response    ", response ) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            jObj = new JSONObject(response) ;
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
    //         Log.d("final result" , jObj.toString()) ;

        return jObj  ;
    }

    }