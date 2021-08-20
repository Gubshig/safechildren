package com.kjh.safechildren;

import android.app.Application;

import androidx.fragment.app.Fragment;

import com.kjh.safechildren.activities.LoginAndRegisterFragment;
import com.kjh.safechildren.data.User_Safechildren;

import java.util.ArrayList;

public class Global extends Application {

    public static User_Safechildren user;
    public static boolean login = false;
    private static LoginAndRegisterFragment loginAndRegister;

    public void setUser(User_Safechildren user){
        this.user = user;
    }
    public User_Safechildren getUser() {
        return user;
    }

    public void setFirstActivity(LoginAndRegisterFragment fa){
        loginAndRegister = fa;
    }
    public static LoginAndRegisterFragment getFirstActivity(){return loginAndRegister;}

    public static String TAG = "safechildren";

    public static ArrayList<User_Safechildren> arrayOfChildrenUsers;
}