package com.kjh.safechildren.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kjh.safechildren.Global;
import com.kjh.safechildren.R;
import com.kjh.safechildren.SafeChildrenCallback;
import com.kjh.safechildren.data.Academy_API;
import com.kjh.safechildren.data.SchoolListAdapter;
import com.kjh.safechildren.data.School_API;
import com.kjh.safechildren.data.User_Firebase;
import com.kjh.safechildren.data.User_Safechildren;
import com.kjh.safechildren.data.UsersListAdapter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class One_UserPageFragment extends Fragment implements SafeChildrenCallback, View.OnClickListener {
    ListView childlist;
    TextView emptylisttext;
    EditText childtoadd;
    Dialog dialog;
    RelativeLayout school,academy;
    User_Firebase user_firebase;
    UsersListAdapter childrenListAdapter;

    String dateString = "";
    String school_type = "elem_list";
    String academy_code = "B10";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return (ViewGroup) inflater.inflate(
                R.layout.userpage_layout, container, false);

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        user_firebase = new User_Firebase(getContext(), getFragmentManager(), view, this::updateListCallback);
        EditText nameEditText = (EditText) view.findViewById(R.id.name_text_input);
        EditText introductionEditText = (EditText) view.findViewById(R.id.intro_text_input);
        CheckBox parentCheckBox = (CheckBox) view.findViewById(R.id.parent_checkbox);
        Button usersavebtn = (Button) view.findViewById(R.id.usersavebtn);
        Button userlogoutbtn = (Button) view.findViewById(R.id.userlogoutbtn);
        RadioGroup genderGroup = (RadioGroup) view.findViewById(R.id.radio_gender);
        RadioButton maleRadio = (RadioButton) view.findViewById(R.id.radio_male);
        maleRadio.setOnClickListener(this);
        RadioButton femaleRadio = (RadioButton) view.findViewById(R.id.radio_female);
        femaleRadio.setOnClickListener(this);
        Button schoolbtn = (Button)view.findViewById(R.id.schoolbtn);
        LinearLayout childlayout = (LinearLayout) view.findViewById(R.id.childlayout);
        childlist = (ListView) view.findViewById(R.id.childlist);
        childtoadd = (EditText)view.findViewById(R.id.childtoadd);
        emptylisttext = (TextView)view.findViewById(R.id.emptylisttext);
        Button addchildbtn = (Button)view.findViewById(R.id.addchildbtn);

        if (Global.user.getGender().matches("male")) {
            maleRadio.setChecked(true);
        } else {
            femaleRadio.setChecked(true);
        }
        addchildbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailOfChildToAdd = childtoadd.getText().toString();
                //get list of users who are not parents from firebase DB
                User_Firebase.getAllChildren(getContext(), true);
            }
        });
        User_Firebase.getAllChildren(getContext(), true);

        userlogoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.getFirstActivity().signOut();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.container,new LoginAndRegisterFragment()).commit();
            }
        });
        usersavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Global.user.setName(nameEditText.getText().toString());
                Global.user.setIntro(introductionEditText.getText().toString());
                User_Firebase.writeUserData();
            }
        });

        //부모 계정일 때 child firebase 데이터 변경 시 알림
        if (Global.user.getType().compareTo("parent") == 0){
            User_Firebase.setChildEventListener(getContext());
        }



        parentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Global.user.setType("parent");
                    childlayout.setVisibility(View.VISIBLE);
                } else {
                    Global.user.setType("child");
                    childlayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        Global.arrayOfChildrenUsers = new ArrayList<User_Safechildren>();
        /*childrenListAdapter = new UsersListAdapter(this, arrayOfChildrenUsers);
        // Attach the adapter to a ListView
        childlist.setAdapter(childrenListAdapter);*/

        if (childlist.getCount() == 0)
            emptylisttext.setVisibility(View.VISIBLE);
        else
            emptylisttext.setVisibility(View.INVISIBLE);

        Button datebtn = (Button) view.findViewById((R.id.datebtn));
        Calendar mcurrentTime = Calendar.getInstance();
        SimpleDateFormat formattedTime = new SimpleDateFormat("EEEE, MMMM d, yyyy");

        datebtn.setText("생년월일 " + Global.user.getDateOfBirth());

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateString = Integer.toString(year) + "-" + Integer.toString(monthOfYear + 1) + "-" + Integer.toString(dayOfMonth);
                datebtn.setText("생년월일: " + dateString);

                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                Global.user.setDateOfBirth(dateString);
            }
        };
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), date, mcurrentTime.get(Calendar.YEAR), mcurrentTime.get(Calendar.MONTH), mcurrentTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //school search dialog
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.school_dialog);
        school = (RelativeLayout)dialog.findViewById(R.id.school);
        academy = (RelativeLayout)dialog.findViewById(R.id.academy);
        RadioButton radio_school = (RadioButton) dialog.findViewById(R.id.radio_school);
        RadioButton radio_academy = (RadioButton) dialog.findViewById(R.id.radio_academy);
        radio_school.setOnClickListener(this);
        radio_academy.setOnClickListener(this);
        radio_school.setChecked(true);
        Spinner spinner_school = (Spinner)dialog.findViewById(R.id.spinner_school);
        ArrayAdapter spinner_adapter = ArrayAdapter.createFromResource(getContext(), R.array.school_type, android.R.layout.simple_spinner_dropdown_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_school.setAdapter(spinner_adapter);
        spinner_school.setSelection(0);
        spinner_school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                school_type = getResources().getStringArray(R.array.school_code)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Spinner spinner_academy = (Spinner)dialog.findViewById(R.id.spinner_academy);
        ArrayAdapter spinner_adapter_ac = ArrayAdapter.createFromResource(getContext(), R.array.academy_area, android.R.layout.simple_spinner_dropdown_item);
        spinner_adapter_ac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_academy.setAdapter(spinner_adapter_ac);
        spinner_academy.setSelection(0);
        spinner_academy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                academy_code = getResources().getStringArray(R.array.academy_code)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        ListView school_list = (ListView)dialog.findViewById(R.id.school_list);
        SchoolListAdapter list_adapter = new SchoolListAdapter(getContext());
        school_list.setAdapter(list_adapter);
        EditText school_search = (EditText)dialog.findViewById(R.id.edit_school);
        EditText academy_search = (EditText)dialog.findViewById(R.id.edit_academy);
        Button searchbtn = (Button)dialog.findViewById(R.id.search_btn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_adapter.clear();
                if (radio_school.isChecked()){
                    new School_API(getString(R.string.careernet_key),school_type,school_search.getText().toString(),list_adapter,getContext()).execute();
                }
                else{
                    new Academy_API(getString(R.string.neis_key),academy_code,academy_search.getText().toString(),list_adapter,getContext()).execute();
                }

            }
        });

        if (Global.user.getSchoolName().length() > 0 )  {
            schoolbtn.setText(Global.user.getSchoolName());
        }
        else {
            schoolbtn.setText("학교 / 학원");
        }
        schoolbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        school_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Global.user.setSchoolName(list_adapter.getItem(i));
                Global.user.setSchoolAddr(list_adapter.getAddrItem(i));
                schoolbtn.setText(list_adapter.getItem(i));
                dialog.dismiss();
            }
        });




        if (Global.user != null) {
            //((GlobalClass)getApplicationContext()).setUser(currUser);
            //find user's tutor information and fill in EditText views
            nameEditText.setText(Global.user.getName());
            introductionEditText.setText(Global.user.getIntro());
            if (Global.user.getType().compareTo("parent") == 0)
                parentCheckBox.setChecked(true);
        }

       // updateLocation();
    }



   /* void updateLocation() {
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }*/

    @Override
    public void onClick(View view) {

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked

        switch(view.getId()) {
            case R.id.radio_female:
                if (checked)
                    Global.user.setGender("female");
                break;
            case R.id.radio_male:
                if (checked)
                    Global.user.setGender("male");
                break;

            case R.id.radio_school:
                if (checked){
                    school.setVisibility(View.VISIBLE);
                    academy.setVisibility(View.GONE);
                }

                break;

            case R.id.radio_academy:
                if (checked){
                    school.setVisibility(View.GONE);
                    academy.setVisibility(View.VISIBLE);
                }

                break;
        }

    }

    /*---------- Listener class to get coordinates ------------- */
    /*private class MyLocationListener implements LocationListener {
        final String TAG = "safechildren";
        @Override
        public void onLocationChanged(Location loc) {
            Toast.makeText(
                    getActivity().getBaseContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v(TAG, longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v(TAG, latitude);

            ------- To get city name from coordinates --------
            String cityName = null;
            Geocoder gcd = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;
            //editLocation.setText(s);
            Global.user.setLocation(loc.getLatitude()+","+loc.getLatitude());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            Global.user.setLastStatusUpdate(currentDateandTime);
            User_Firebase.updateUser();
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }

    //it doesn't work with fragment
    /*public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked

        switch(view.getId()) {
            case R.id.radio_female:
                if (checked)
                    Global.user.setGender("female");
                break;
            case R.id.radio_male:
                if (checked)
                    Global.user.setGender("male");
                break;
        }
    }*/

    @Override
    public void updateListCallback(Context c) {
        Global.arrayOfChildrenUsers.clear();
        for(User_Safechildren child : User_Firebase.allChildrenList){
            if(child.getEmail().matches(childtoadd.getText().toString())){
                //update user DB, add to childrenCSV
                String prefix="";
                if(Global.user.getChildrenCSV().length()>0)
                    prefix = ",";
                //check if already exist, skip if it does
                if(Global.user.getChildrenCSV().contains(child.getEmail())==false){
                    Global.user.setChildrenCSV(Global.user.getChildrenCSV().concat(prefix+child.getEmail()));
                }

                User_Firebase.updateUser();
                if(!Global.arrayOfChildrenUsers.contains(child))
                    Global.arrayOfChildrenUsers.add(child);
            }
            //also loop through childrenCSV

            if(Global.user.getChildrenCSV().length()>0){
                String[] childrenEmails = Global.user.getChildrenCSV().split(",");
                for(String email: childrenEmails){
                    if(email.matches(child.getEmail())){
                        if(!Global.arrayOfChildrenUsers.contains(child))
                            Global.arrayOfChildrenUsers.add(child);
                    }
                }
            }
        }

        //arrayOfChildrenUsers = User_Firebase.allChildrenList;
        childrenListAdapter = new UsersListAdapter(getContext(), Global.arrayOfChildrenUsers);
        childlist.setAdapter(childrenListAdapter);
        childlist.postInvalidate();
        childlist.invalidate();
        if(Global.arrayOfChildrenUsers.size()>0)
            emptylisttext.setVisibility(View.INVISIBLE);
    }
}
