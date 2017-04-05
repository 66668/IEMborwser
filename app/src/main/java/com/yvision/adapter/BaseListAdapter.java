package com.yvision.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter extends BaseAdapter {

	public Context context;
	public LayoutInflater inflater;
	public ArrayList entityList = new ArrayList();
	public boolean IsEnd=false;//翻页设置
	public static ArrayList<Boolean> isCheckedList = null;//用于标记checkBox值

	public BaseListAdapter(Context context){
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(entityList != null){
			return entityList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return entityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	} 


	public void setEntityList(ArrayList entityList) {
//		this.entityList = entityList;//
		this.entityList.clear();
		this.entityList.addAll(entityList);
		notifyDataSetChanged();
	}
	
	public void addEntityList(List entityList) {
		this.entityList.addAll(entityList);
		notifyDataSetChanged();
	}
	public void insertEntityList(List entityList){
		if(entityList != null){
			this.entityList.add(0,entityList);
		}
		notifyDataSetChanged();

	}
	
	protected abstract View inflateConvertView();
	
	protected abstract void initViewData(int position, View convertView);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = inflateConvertView();
		} 
		initViewData(position, convertView);
		return convertView;
	}

}
