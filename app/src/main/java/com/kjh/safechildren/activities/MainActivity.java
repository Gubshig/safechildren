package com.kjh.safechildren.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kjh.safechildren.Global;
import com.kjh.safechildren.R;

public class MainActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //nfc 사용 불가 시 null return
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Add bottom navigation menu listener
        navView = (BottomNavigationView)findViewById(R.id.bottommenu);
        navView.setOnItemSelectedListener(new Navigation(fragmentManager));
        navView.setSelectedItemId(R.id.main_menu_button);


    }

    @Override
    //앱이 켜져 있을 때만 인식
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
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
        if (tag != null) {
            byte[] tagId = tag.getId();
            Toast.makeText(this,toHexString(tagId),Toast.LENGTH_LONG).show();
            Log.e("TAG",toHexString(tagId));
        }

    }

    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < data.length; ++i) {

            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F))

                    .append(CHARS.charAt(data[i] & 0x0F));

        }

        return sb.toString();

    }


}
