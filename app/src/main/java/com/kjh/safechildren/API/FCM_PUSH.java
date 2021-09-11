package com.kjh.safechildren.API;

import android.os.AsyncTask;

import com.google.gson.JsonObject;
import com.kjh.safechildren.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class FCM_PUSH extends AsyncTask {
    //fcm request url
    private final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";

    //구글 인증 서버키, 주제
    private String AUTH_KEY_FCM, topic, title, state_text;

    //device app token
    //private String userDeviceIdKey = "dMkucqlJTF-92U2SxBgsrm:APA91bHrLvxI6bnw-g2pWG7g10uV2wm3h-eW7H0tlH4FzHaYJnc_7AJW9x2awEPuRZkmNopiccfJ6stc7KQM8CBM6fz_zVO9gk7mjAgEJqz0WGt66x0Rr0q8T5h1V7ab5AXlUlRg_iDh";

    private HttpURLConnection conn;
    private OutputStreamWriter wr;
    private BufferedReader br;
    private URL url;

    public FCM_PUSH(String AUTH_KEY_FCM, String topic,String title, String state_text){
        this.AUTH_KEY_FCM = AUTH_KEY_FCM;
        this.topic = topic;
        this.title = title;
        this.state_text = state_text;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            pushFCMNotification(topic);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void pushFCMNotification(String topic) throws Exception {
        /*
             페이로드 형태
            { "data": {
                "key1": "value1",
                "key2": "value2"
              },
              "to" : "userToken..." or topics
            }

        */

        url = new URL(API_URL_FCM);
        conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
        conn.setRequestProperty("Content-Type", "application/json");
        //일반 텍스트 전달시 Content-Type , application/x-www-form-urlencoded;charset=UTF-8

        //알림 + 데이터 메세지 형태의 전달
        JsonObject json = new JsonObject();
        JsonObject info = new JsonObject();
        JsonObject dataJson = new JsonObject();

        //앱 백그라운드 발송시 기본 noti는 이 내용을 참조한다
        info.addProperty("title", title);
        info.addProperty("body", "자녀가 "+state_text+"하였습니다."); // Notification body
        info.addProperty("sound", "default");

        //noti일 경우 백그라운드 상태에서 활성화
        json.add("notification", info);

        //디바이스전송 (앱단에서 생성된 토큰키)
        //json.addProperty("to", userDeviceIdKey); // deviceID

        //주제별 전송
        json.addProperty("to", "/topics/" + topic);

        //여러주제 전송
        //"condition": "'dogs' in topics || 'cats' in topics",


        dataJson.addProperty("type", title);
        dataJson.addProperty("message", "자녀가 "+state_text+"하였습니다.");



        json.add("data", dataJson);

        try{
            wr = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            wr.write(json.toString());
            wr.flush();

        }catch(Exception e){
            connFinish();
            throw new Exception("OutputStreamException : " + e);
        }

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            connFinish();
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }else{
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
            }

        }
    }

    /**
     * 네트웍크관련 finalize
     * connFinish
     */
    private void connFinish(){
        if(br != null){
            try {
                br.close();
                br = null;
            } catch (IOException e) {
            }
        }
        if(wr != null){
            try {
                wr.close();
                wr = null;
            } catch (IOException e) {
            }

        }
        if(conn != null){
            conn.disconnect();
            conn = null;
        }
    }

}
