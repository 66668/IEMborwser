package com.yvision.db.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.yvision.application.MyApplication;
import com.yvision.model.AttendModel;

import java.util.ArrayList;

/**
 * 推送消息的数据存储
 * Created by sjy on 2017/4/5.
 */

public class SQLAttend extends SQLiteOpenHelper {


    private Context context;

    //
    private static final int Db_CO_VERSION = 1;//数据库version
    private static final String DB_NAME = "contactcopy.db";//数据库
    private static final String TABLE_NAME = "attendDate";//表名

    //
    public SQLAttend(Context context) {
        super(context, DB_NAME, null, Db_CO_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME + "("
                + "message text ,"
                + "Type text ,"
                + "Id text ,"
                + "Pic text ,"
                + "date text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //保存model
    public void saveModel(AttendModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("message", model.getMessage());
        values.put("Type", model.getType());
        values.put("Id", model.getId());
        values.put("Pic", model.getPic());
        values.put("date", model.getCapTime());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    //读取所有数据
    public ArrayList<AttendModel> getModelList() {
        ArrayList<AttendModel> list = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME
                    , new String[]{"message", "Type", "date", "Id", "Pic"}
                    , null, null, null, null,  "date desc");
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String message = cursor.getString(cursor.getColumnIndex("message"));
                String Type = cursor.getString(cursor.getColumnIndex("Type"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String Id = cursor.getString(cursor.getColumnIndex("Id"));
                String Pic = cursor.getString(cursor.getColumnIndex("Pic"));

                AttendModel model = new AttendModel();

                model.setCapTime(date);
                model.setType(Type);
                model.setId(Id);
                model.setMessage(message);
                model.setPic(Pic);

                list.add(model);
            }
            cursor.close();
            db.close();
            if (list != null && list.size() > 0) {
                return list;
            }
        } catch (Exception e) {
            Log.d("SJY", "存储异常=" + e.toString());
        }
        return null;
    }

    //程序退出，清空所有表
    public void clearDb() {

        SQLiteDatabase db = new SQLAttend(MyApplication.getInstance()).getWritableDatabase();

        db.execSQL("DELETE FROM " + TABLE_NAME);

        db.close();
    }
}
