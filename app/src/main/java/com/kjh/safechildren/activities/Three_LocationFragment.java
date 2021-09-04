package com.kjh.safechildren.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kjh.safechildren.Global;
import com.kjh.safechildren.R;
import com.kjh.safechildren.data.User_Safechildren;

import java.util.ArrayList;

public class Three_LocationFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ArrayList<LatLng> locationArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return (ViewGroup) inflater.inflate(
                R.layout.locationactivity_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, mapFragment)
                .commit();

        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // in below line we are initializing our array list.
        locationArrayList = new ArrayList<>();

        // on below line we are adding our
        // locations in our array list.
        if (Global.arrayOfChildrenUsers != null && Global.login){
            if(Global.user.getType().compareTo("parent")==0){
                for(User_Safechildren child : Global.arrayOfChildrenUsers){
                    if(child.getLocation()!=null){
                        if(child.getLocation().length()>0){
                            String locString[] = child.getLocation().split(",");
                            if(locString.length==2){
                                Double lat = Double.parseDouble(locString[0]);
                                Double lon = Double.parseDouble(locString[1]);

                                LatLng loc = new LatLng(lat, lon);
                                locationArrayList.add(loc);
                            }
                        }
                    }
                }
            }
            else if (Global.user.getType().compareTo("child")==0){
                if(Global.user.getLocation()!=null){
                    if(Global.user.getLocation().length()>0){
                        String locString[] = Global.user.getLocation().split(",");
                        if(locString.length==2){
                            Double lat = Double.parseDouble(locString[0]);
                            Double lon = Double.parseDouble(locString[1]);
                            LatLng loc = new LatLng(lat, lon);
                            locationArrayList.add(loc);
                        }
                    }
                }
            }


        }

        else {
            Toast.makeText(getContext(),
                   "로그인이 필요한 서비스입니다.",
                    Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String marker = "";
        // inside on map ready method
        // we will be displaying all our markers.
        // for adding markers we are running for loop and
        // inside that we are drawing marker on our map.
        for (int i = 0; i < locationArrayList.size(); i++) {

            if (Global.arrayOfChildrenUsers.size() > 0){ //a,b
                marker = Global.arrayOfChildrenUsers.get(i).getName();
            }
            else{// 학생
                marker = "현재 위치";
            }

            // below line is use to add marker to each location of our array list.
            mMap.addMarker(new MarkerOptions().position(locationArrayList.get(i)).title(marker));

            /*// below lin is use to zoom our camera on map.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            // below line is use to move our camera to the specific location.
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList.get(i)));*/
            pointToPosition(locationArrayList.get(i));
        }
    }
    private void pointToPosition(LatLng position) {
        //Build camera position
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(15).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
