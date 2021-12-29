package com.example.sunrin_app;

import android.app.Application;

import com.example.sunrin_app.activities.LoginAndRegisterFragment;
import com.example.sunrin_app.data.User_Ingoout;

import java.util.ArrayList;

public class Global extends Application {

    public static User_Ingoout user;
    public static boolean login = false;
    private static LoginAndRegisterFragment loginAndRegister;

    public void setUser(User_Ingoout user){
        this.user = user;
    }
    public User_Ingoout getUser() {
        return user;
    }

    public void setFirstActivity(LoginAndRegisterFragment fa){
        loginAndRegister = fa;
    }
    public static LoginAndRegisterFragment getFirstActivity(){return loginAndRegister;}

}