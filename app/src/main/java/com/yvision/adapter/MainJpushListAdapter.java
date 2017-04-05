package com.yvision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yvision.R;
import com.yvision.model.AttendModel;

import java.util.ArrayList;

/**
 * Created by sjy on 2017/4/1.
 */

public class MainJpushListAdapter extends BaseAdapter {
    public ArrayList<AttendModel> entityList = new ArrayList<AttendModel>();
    public Context context;
    public LayoutInflater inflater;

    public class WidgetHolder {
        public TextView tv_messge;
    }

    public MainJpushListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public MainJpushListAdapter(Context context, ArrayList<AttendModel> list) {
        this.context = context;
        this.entityList = list;
        inflater = LayoutInflater.from(context);
    }

    //listView赋值
    public void setEntityList(ArrayList entityList) {
        this.entityList.clear();
        this.entityList.addAll(entityList);
        notifyDataSetChanged();
    }

    //listView拼接
    public void addEntityList(ArrayList entityList) {
        this.entityList.addAll(entityList);
        notifyDataSetChanged();
    }

    //listView插入
    public void insertEntityList(ArrayList entityList) {
        if (entityList != null) {
            this.entityList.addAll(0, entityList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return entityList.size();
    }

    @Override
    public Object getItem(int position) {
        return entityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final WidgetHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_msg, null);
            holder = new WidgetHolder();
            holder.tv_messge = (TextView) convertView.findViewById(R.id.tv_msg);
            convertView.setTag(holder);
        } else {
            holder = (WidgetHolder) convertView.getTag();
        }
        AttendModel model = (AttendModel) entityList.get(position);
        holder.tv_messge.setText(model.getMessage());
        return convertView;
    }
}
