package com.kjh.safechildren.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kjh.safechildren.Global;
import com.kjh.safechildren.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class Navigation implements BottomNavigationView.OnItemSelectedListener{
    private FragmentManager fm;
    public Navigation(FragmentManager fragmentManager){
        fm = fragmentManager;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.main_menu_button:
                if (Global.login){
                    if (fm.findFragmentByTag("user") != null) {
                        fm.beginTransaction().show(fm.findFragmentByTag("user")).commit();
                    }
                    else {
                        fm.beginTransaction().add(R.id.container, new One_UserPageFragment(),"user").commit();
                    }

                    if  (fm.findFragmentByTag("login") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("login")).commit();
                    if  (fm.findFragmentByTag("status") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("status")).commit();
                    if  (fm.findFragmentByTag("location") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("location")).commit();

                }
                else {
                    if (fm.findFragmentByTag("login") != null) {
                        fm.beginTransaction().show(fm.findFragmentByTag("login")).commit();
                    }
                    else {
                        fm.beginTransaction().add(R.id.container, new LoginAndRegisterFragment(),"login").commit();
                    }

                    if  (fm.findFragmentByTag("user") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("user")).commit();
                    if  (fm.findFragmentByTag("status") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("status")).commit();
                    if  (fm.findFragmentByTag("location") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("location")).commit();

                }
                return true;

            case R.id.info_menu_button:
                if (fm.findFragmentByTag("status") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("status")).commit();
                }
                else {
                    fm.beginTransaction().add(R.id.container, new Two_StatusFragment(),"status").commit();
                }

                if  (fm.findFragmentByTag("login") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("login")).commit();
                if  (fm.findFragmentByTag("user") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("user")).commit();
                if  (fm.findFragmentByTag("location") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("location")).commit();

                return true;

            case R.id.location_menu_button:

                if (fm.findFragmentByTag("location") != null) {
                    fm.beginTransaction().show(fm.findFragmentByTag("location")).commit();
                }
                else {
                    fm.beginTransaction().add(R.id.container, new Three_LocationFragment(),"location").commit();
                }

                if  (fm.findFragmentByTag("login") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("login")).commit();
                if  (fm.findFragmentByTag("user") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("user")).commit();
                if  (fm.findFragmentByTag("status") != null)  fm.beginTransaction().hide(fm.findFragmentByTag("status")).commit();

                return true;
        }
        return false;


    }


}

