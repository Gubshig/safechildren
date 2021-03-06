package com.kjh.safechildren.data;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.kjh.safechildren.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChildrenDetailsListAdapter extends BaseAdapter {
    ArrayList<User_Safechildren> usersList;
    Context c;
    public ChildrenDetailsListAdapter(Context context, ArrayList<User_Safechildren> usersList) {
        this.c = context;
        this.usersList = usersList;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(usersList==null)
            return 0;
        return usersList.size();
    }
    @Override
    public User_Safechildren getItem(int position) {
        // TODO Auto-generated method stub
        return usersList.get(position);
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        View row = null;
        User_Safechildren user = usersList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            row = inflater.inflate(R.layout.child_details, parent,
                    false);
        } else {
            row = convertView;
        }
        /*
          <TextView
    android:id="@+id/childname">
    android:id="@+id/childemail">
    android:id="@+id/childgender">
    android:id="@+id/childdob">
    android:id="@+id/childlocation">
    android:id="@+id/childschools">
         */
        TextView tvName = (TextView) row.findViewById(R.id.childname);
        TextView tvEmail = (TextView) row.findViewById(R.id.childemail);
        TextView tvGender = (TextView) row.findViewById(R.id.childgender);
        TextView tvDoB = (TextView) row.findViewById(R.id.childdob);
        TextView tvLocation = (TextView) row.findViewById(R.id.childlocation);
        TextView tvSchools = (TextView) row.findViewById(R.id.childschools);
        TextView tvStatus = (TextView) row.findViewById(R.id.childstatus);
        TextView tvStatusUpdateTime = (TextView) row.findViewById(R.id.childstatusupdatetime);
        // Populate the data into the template view using the data object
        tvName.setText("??????:  "+user.getName());
        tvEmail.setText("?????????:  "+user.getEmail());
        tvGender.setText("??????:  "+user.getGender());
        tvDoB.setText("????????????:  "+user.getDateOfBirth());
        tvLocation.setText("??????:  "+getAddress(user.getLocation()));
        tvSchools.setText("??????/?????? ??????:  "+user.getSchoolsCSV());
        if(user.getStatus()){
            tvStatus.setText("??????: ??????");
        }
        else {
            tvStatus.setText("??????: ??????");
        }
        tvStatusUpdateTime.setText("?????????????????????: "+user.getLastStatusUpdate());
        // Return the completed view to render on screen
        return row;
    }

    private String getAddress(String loc){
        String locString[] = loc.split(",");
        String cityName = null;
        if(locString.length==2){
            Double lat = Double.parseDouble(locString[0]);
            Double lon = Double.parseDouble(locString[1]);

            Geocoder gcd = new Geocoder(c, Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(lat,
                        lon, 1);

                if (addresses.size() > 0) {
                    cityName = addresses.get(0).getAddressLine(0).toString().replace("????????????","");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(c,"?????? ????????? ???????????? ???????????????.",Toast.LENGTH_LONG);
            }
        }

        return cityName;
    }
}

