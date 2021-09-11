package com.kjh.safechildren.API;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.kjh.safechildren.data.SchoolListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Academy_API extends AsyncTask<String, Void, String> {
    private String key;
    private String code;
    private String academy_name;
    private StringBuffer stringbuffer = new StringBuffer();
    private String result;
    private SchoolListAdapter adapter;
    private Context c;
    private String TAG = "Json Parsing Error";

    public Academy_API(String key, String code, String academy_name,SchoolListAdapter adapter, Context c){
        this.key = key;
        this.code = code;
        this.academy_name = academy_name;
        this.adapter = adapter;
        this.c = c;
    }

    @Override
    protected String doInBackground(String... strings) {
        URL url = null;
        try{
            url = new URL("https://open.neis.go.kr/hub/acaInsTiInfo?Key="+key
                    +"&Type=json&pIndex=1&pSize=100&ATPT_OFCDC_SC_CODE="+code
                    + "&ACA_NM="+academy_name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection != null){
                connection.setConnectTimeout(10000);
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
            JSONArray array =  new JSONObject(result).getJSONArray("acaInsTiInfo");
            for (int i = 1; i < array.length(); i++) {
                JSONObject temp = array.getJSONObject(i);
                JSONArray temp_arr = temp.getJSONArray("row");
                for (int j = 0 ; j <temp_arr.length();j++){
                    JSONObject row = temp_arr.getJSONObject(j);
                    String name = row.getString("ACA_NM");
                    String addr = row.getString("FA_RDNMA");
                    adapter.addItem(name,addr);
                }

            }
            adapter.notifyDataSetChanged();

        }catch (Exception e){
            Toast.makeText(c,"검색된 결과가 없습니다.",Toast.LENGTH_LONG).show();
            Log.e(TAG,e.getMessage());
        }


    }
}