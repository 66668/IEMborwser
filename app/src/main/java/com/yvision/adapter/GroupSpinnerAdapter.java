package com.yvision.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yvision.R;
import com.yvision.model.GroupModel;

import java.util.ArrayList;

/**
 * 人像库model与spinner适配
 * Created by sjy on 2016/11/13.
 */

public class GroupSpinnerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();

    public GroupSpinnerAdapter(Context context, ArrayList<GroupModel> groupList) {
        this.context = context;
        this.groupList.addAll(groupList);
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public GroupModel getItem(int position) {
        return groupList.get(position);
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
        textView.setText(groupList.get(position).getGroupName());
        return convertView;
    }

}