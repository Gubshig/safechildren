package com.kjh.safechildren.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kjh.safechildren.R;

import java.util.ArrayList;

public class SchoolListAdapter extends BaseAdapter {
    ArrayList<String> school_name, school_addr;
    Context c;
    public SchoolListAdapter(Context context) {
        this.c = context;
        this.school_name = new ArrayList<String>();
        this.school_addr = new ArrayList<String>();
    }


    public void addItem(String name, String addr){

        school_name.add(name);
        school_addr.add(addr);
    }

    public void clear(){
        school_name.clear();
        school_addr.clear();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(school_name ==null)
            return 0;
        return school_name.size();
    }
    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return school_name.get(position);
    }

    public String getAddrItem(int position) {
        // TODO Auto-generated method stub
        return school_addr.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.school_list, parent, false);

        TextView name = (TextView) view.findViewById(R.id.school_name);
        TextView addr = (TextView) view.findViewById(R.id.school_addr);
        // Populate the data into the template view using the data object
        name.setText("학교/학원명:  "+school_name.get(position));
        addr.setText("주소:  "+school_addr.get(position));
        // Return the completed view to render on screen
        return view;
    }
}
