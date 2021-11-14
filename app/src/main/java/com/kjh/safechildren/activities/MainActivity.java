package com.kjh.safechildren.activities;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kjh.safechildren.Global;
import com.kjh.safechildren.R;
import com.kjh.safechildren.API.FCM_PUSH;
import com.kjh.safechildren.data.GPS;
import com.kjh.safechildren.data.User_Firebase;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private BottomNavigationView navView;
    private GPS gps;
   // private String TAG = "FirebaseMessaging";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //FCM push 수신을 위한 Token받아오기
        /*FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d(TAG, token);

                    }
                });*/



        gps = new GPS(this,getApplicationContext());


        //nfc 사용 불가 시 null return
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null){
            Toast.makeText(getApplicationContext(),"NFC 미지원 단말기입니다. ",Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Add bottom navigation menu listener
        navView = (BottomNavigationView)findViewById(R.id.bottommenu);
        navView.setOnItemSelectedListener(new Navigation(fragmentManager));
        navView.setSelectedItemId(R.id.main_menu_button);


    }

    //뒤로 가기 시 dialog
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("종료")
                .setMessage("앱을 종료합니다.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("취소",null)
                .show();
    }

    @Override
    //앱이 켜져 있을 때만 인식
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null ) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null ){
            Toast.makeText(getApplicationContext(),"tag 인식에 실패했습니다.",Toast.LENGTH_LONG).show();
        }
        else{
            String state_text = "";
            if (new String(tag.getId()).equals("@=?!")) {
                if (Global.user.getType().equals("child")) { // 학생 계정일 경우
                    if (Global.user.getStatus()) {//true(승차) 상태에서 nfc tag 인식 시
                        Global.user.setStatus(false); //false(하차)상태로 변환
                        Toast.makeText(getApplicationContext(), "하차하였습니다.", Toast.LENGTH_SHORT).show();
                        state_text += "하차";
                        gps.removeGPS();
                        User_Firebase.updateUser();
                    } else { //false(하차) 상태에서 nfc tag 인식 시
                        Global.user.setStatus(true); //true(승차)상태로 변환
                        Toast.makeText(getApplicationContext(), "승차하였습니다.", Toast.LENGTH_SHORT).show();
                        state_text += "승차";
                        gps.getLocation(); //GPS 등록
                        User_Firebase.updateUser();

                    }

                    // 숭, 하차 상태 변경 후 fcm push
                    try {
                        new FCM_PUSH(getString(R.string.AUTH_KEY_FCM), Global.user.getUid(), Global.user.getName(), state_text).execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }
}
