package com.kjh.safechildren.data;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kjh.safechildren.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;

public class School_API extends AsyncTask<String, Void, String> {
    private String key;
    private String type;
    private String school_name;
    private StringBuffer stringbuffer = new StringBuffer();
    private String result;
    SchoolListAdapter adapter;
    private String TAG = "Json Parsing Error";

    //조건 조회 시
    public School_API(String key, String type, String school_name,SchoolListAdapter adapter){
        this.key = key;
        this.type = type;
        this.school_name = school_name;
        this.adapter = adapter;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        try{
            url = new URL("http://www.career.go.kr/cnet/openapi/getOpenApi?apiKey="+key
                    +"&svcType=api&svcCode=SCHOOL&contentType=json&gubun="+type
                    + "&searchSchulNm="+school_name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection != null){
                connection.setConnectTimeout(30000);
                connection.setUseCaches(false);
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStreamReader input = new InputStreamReader(connection.getInputStream());
                    BufferedReader buffer = new BufferedReader(input);
                    while(true){
                        String line = buffer.readLine();
                        if (line == null) break;
                        stringbuffer.append(line);
                    }
                    buffer.close();
                    connection.disconnect();
                }
            }
            result = stringbuffer.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        try{
            JSONObject object = new JSONObject(result).getJSONObject("dataSearch");
            JSONArray array = object.getJSONArray("content");
            for (int i = 0; i < array.length(); i++) {
                JSONObject temp = array.getJSONObject(i);
                String name = temp.getString("schoolName");
                String addr = temp.getString("adres");
                adapter.addItem(name,addr);

            }

            adapter.notifyDataSetChanged();



        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }


    }
}
