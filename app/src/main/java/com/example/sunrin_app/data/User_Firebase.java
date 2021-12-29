package com.example.sunrin_app.data;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.sunrin_app.Global;
import com.example.sunrin_app.R;
import com.example.sunrin_app.IngooutCallback;

import java.util.ArrayList;

public class User_Firebase {
    private static DatabaseReference mDatabase;
    private static View view;
    private static FragmentManager fm;
    private Context c;

    static IngooutCallback ingooutCB;

    public static ArrayList<User_Ingoout> allChildrenList;


    private final static String serverAddress= "https://ingoout-de41a-default-rtdb.asia-southeast1.firebasedatabase.app/";

    public User_Firebase(Context c, FragmentManager fm, View view, IngooutCallback ingooutCB) {
        // [START initialize_database_ref]
        this.ingooutCB = ingooutCB;
        if(mDatabase==null){
            FirebaseDatabase.getInstance(serverAddress).setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance(serverAddress).getReference();
        }

        // [END initialize_database_ref]
        this.view = view;
        this.c = c;
        this.fm = fm;
    }



    public static void getAllUserData(Context c, FragmentManager fm, FirebaseUser user, boolean bLogin){
        User_Firebase.getAllUserDataFromFirebase(c, fm, user, bLogin);
    }

    public static void getAllUserDataFromFirebase(Context c,FragmentManager fm, FirebaseUser firebaseUserData, boolean bLogin) {
        if (mDatabase == null) {
            FirebaseDatabase.getInstance(serverAddress).setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance(serverAddress).getReference();
        }
        if(firebaseUserData==null && bLogin==true){
           // Intent intent2 = new Intent(c, One_UserPageFragment.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
           // c.startActivity(intent2);
        }
        if(firebaseUserData==null){
            return;
        }

        String uid = firebaseUserData.getUid();
        //Task<Void> t = mDatabase.child("users").child(userToUpdate.Uid).setValue(userToUpdate);
        mDatabase.child("users").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                User_Ingoout userDataFromDB = dataSnapshot.getValue(User_Ingoout.class);
                //userDataFromDB가 null이라면 firebase의 realtime database에는 없는것임, 즉 "Authentication" db에만 있는것임
                if (userDataFromDB == null) {
                    Global.user = new User_Ingoout(firebaseUserData.getUid(), firebaseUserData.getEmail());
                } else {
                    Global.user = userDataFromDB;
                    //Uid is missing in the data received from firebase
                    Global.user.setUid(firebaseUserData.getUid());
                }
                /*
                //Global.user.setUid(firebaseUserData.getUid());
                //Global.user.setEmail(firebaseUserData.getEmail());
                //User_Firebase.getAllTutorsAndLessons(c, firebaseUserData, bLogin);

                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                    fm.beginTransaction().replace(R.id.container, new One_UserPageFragment()).commit();
                }
                */

            }
        });
        mDatabase.child("users").child(uid).get().addOnFailureListener(dataSnapshot -> {
            Toast.makeText(c, "getAllUserDataFromFirebase 에러",
                    Toast.LENGTH_LONG).show();
        });
    }
    public static void writeUserData() {
        //User_Safechildren user = new User_Safechildren(userId, email);
        mDatabase.child("users").child(Global.user.getUid()).setValue(Global.user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Write was successful!
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Write failed
            }
        });
    }
    public void getUser(String userId){
        User_Ingoout databaseUserEntry=null;
        if (mDatabase == null) {
            FirebaseDatabase.getInstance(serverAddress).setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance(serverAddress).getReference();
        }
        mDatabase.child("users").child(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                User_Ingoout userDataFromDB = dataSnapshot.getValue(User_Ingoout.class);

                Global.user = userDataFromDB;
                //Uid is missing in the data received from firebase
                Global.user.setUid(userId);
            }
        });
    }

    public static void updateUser(){
        //userToUpdate;
        Task<Void> t = mDatabase.child("users").child(Global.user.getUid()).setValue(Global.user);
        //boolean result = t.isSuccessful();
    }

    public static void getAllChildren(Context c, boolean bGetParentsChildren){

        if (mDatabase == null) {
            FirebaseDatabase.getInstance(serverAddress).setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance(serverAddress).getReference();
        }
        mDatabase.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                allChildrenList = new ArrayList<User_Ingoout>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    User_Ingoout userData = childDataSnapshot.getValue(User_Ingoout.class);
                    if(userData.type.compareTo("child")==0 || userData.type.length()==0){
                        allChildrenList.add(userData);

                    }

                }
                if(bGetParentsChildren)
                    ingooutCB.updateListCallback(c);
            }
        });
    }
}
