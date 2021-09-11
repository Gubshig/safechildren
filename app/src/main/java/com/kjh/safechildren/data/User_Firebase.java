package com.kjh.safechildren.data;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kjh.safechildren.Global;
import com.kjh.safechildren.R;
import com.kjh.safechildren.SafeChildrenCallback;
import com.kjh.safechildren.activities.LoginAndRegisterFragment;
import com.kjh.safechildren.activities.One_UserPageFragment;
import com.kjh.safechildren.activities.Two_StatusFragment;

import java.util.ArrayList;

public class User_Firebase {
    private static DatabaseReference mDatabase;
    private static View view;
    private static FragmentManager fm;
    private Context c;

    static SafeChildrenCallback safechildrenCB;

    public static ArrayList<User_Safechildren> allChildrenList;


    private final static String serverAddress= "https://safe-children-e23dc-default-rtdb.asia-southeast1.firebasedatabase.app/";
   //원장님 계정
   // private final static String serverAddress="https://safechildren-55a0a-default-rtdb.asia-southeast1.firebasedatabase.app/";
    public User_Firebase(Context c, FragmentManager fm, View view, SafeChildrenCallback safechildrenCB) {
        // [START initialize_database_ref]
        this.safechildrenCB = safechildrenCB;
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
                User_Safechildren userDataFromDB = dataSnapshot.getValue(User_Safechildren.class);
                //userDataFromDB가 null이라면 firebase의 realtime database에는 없는것임, 즉 "Authentication" db에만 있는것임
                if (userDataFromDB == null) {
                    Global.user = new User_Safechildren(firebaseUserData.getUid(), firebaseUserData.getEmail());
                } else {
                    Global.user = userDataFromDB;
                    //Uid is missing in the data received from firebase
                    Global.user.setUid(firebaseUserData.getUid());
                }
                //Global.user.setUid(firebaseUserData.getUid());
                //Global.user.setEmail(firebaseUserData.getEmail());
                //User_Firebase.getAllTutorsAndLessons(c, firebaseUserData, bLogin);

                fm.beginTransaction().replace(R.id.container, new One_UserPageFragment()).commit();

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
        User_Safechildren databaseUserEntry=null;
        if (mDatabase == null) {
            FirebaseDatabase.getInstance(serverAddress).setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance(serverAddress).getReference();
        }
        mDatabase.child("users").child(userId).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                User_Safechildren userDataFromDB = dataSnapshot.getValue(User_Safechildren.class);

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


   /*public static void setChildEventListener(Context c){
        if (mDatabase == null) {
            FirebaseDatabase.getInstance(serverAddress).setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance(serverAddress).getReference();
        }
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull  DataSnapshot snapshot, @Nullable  String previousChildName) {
                String ref = snapshot.getRef().toString();
                //users 안 데이터가 바뀐 경우에만 탐색 (manager 제외)
                if (ref.substring(ref.length()-5, ref.length()).equals("users")){
                    if(Global.user.getType().compareTo("parent")==0){
                        for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                            User_Safechildren childData = childDataSnapshot.getValue(User_Safechildren.class);
                            for (int i = 0; i < Global.arrayOfChildrenUsers.size();i++){
                                boolean child = childData.email.compareTo(Global.arrayOfChildrenUsers.get(i).getEmail())==0;
                                boolean status = childData.status.compareTo(Global.arrayOfChildrenUsers.get(i).getStatus())!=0;
                                if (child && status){
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(c,"default");

                                    builder.setSmallIcon(R.mipmap.ic_launcher);
                                    builder.setContentTitle("알림 제목");
                                    builder.setContentText("알람 세부 텍스트");
                                    // 알림 표시
                                    NotificationManager notificationManager = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
                                    }
                                    // id값은  정의해야하는 각 알림의 고유한 int값
                                    notificationManager.notify(1, builder.build());
                                    User_Firebase.getAllChildren(c, true);//child정보 update
                                }
                            }

                        }
                    }
                }



            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable  String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

    public static void getAllChildren(Context c, boolean bGetParentsChildren){

        if (mDatabase == null) {
            FirebaseDatabase.getInstance(serverAddress).setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance(serverAddress).getReference();
        }
        mDatabase.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                allChildrenList = new ArrayList<User_Safechildren>();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    User_Safechildren userData = childDataSnapshot.getValue(User_Safechildren.class);
                    if(userData.type.compareTo("child")==0 || userData.type.length()==0){
                        allChildrenList.add(userData);

                    }

                }
                if(bGetParentsChildren)
                    safechildrenCB.updateListCallback(c);
            }
        });
    }
}
