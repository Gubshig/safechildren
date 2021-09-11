package com.kjh.safechildren.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kjh.safechildren.API.FCM_PUSH;
import com.kjh.safechildren.Global;
import com.kjh.safechildren.R;
import com.kjh.safechildren.data.ChildrenDetailsListAdapter;
import com.kjh.safechildren.data.User_Firebase;
import com.kjh.safechildren.data.User_Safechildren;
import com.kjh.safechildren.data.UsersListAdapter;

import java.util.ArrayList;

public class Two_StatusFragment extends Fragment {
    ChildrenDetailsListAdapter childrenListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return (ViewGroup) inflater.inflate(
                R.layout.statusactivity_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ListView childrenList = (ListView)view.findViewById(R.id.childrendetaillist);
        if (Global.login){
            childrenListAdapter = new ChildrenDetailsListAdapter(getContext(), Global.arrayOfChildrenUsers);//Context context, ArrayList<User_Safechildren> usersList
            childrenList.setAdapter(childrenListAdapter);
            try {
                new FCM_PUSH(getString(R.string.AUTH_KEY_FCM),Global.user.getUid(),Global.user.getName(),"승차").execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getContext(),"로그인이 필요한 서비스입니다.",Toast.LENGTH_LONG).show();
        }

    }

}
