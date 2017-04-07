package com.yvision.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yvision.R;
import com.yvision.model.DepartmentModel;

import java.util.ArrayList;

/**
 * Created by sjy on 2016/11/13.
 */

public class DepartmentSpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DepartmentModel> departmentList = new ArrayList<DepartmentModel>();

    public DepartmentSpinnerAdapter(Context context, ArrayList<DepartmentModel> departmentList) {
        this.context = context;
        this.departmentList.addAll(departmentList);
    }

    @Override
    public int getCount() {
        return departmentList.size();
    }

    @Override
    public DepartmentModel getItem(int position) {
        return departmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = View.inflate(context, R.layout.act_facedb_item, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.item_txt);
        textView.setText(departmentList.get(position).getDeptName());
        return convertView;
    }

}