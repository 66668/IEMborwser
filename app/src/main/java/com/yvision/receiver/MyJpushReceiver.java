package com.yvision.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yvision.fragment.ListDataFragment;
import com.yvision.model.AttendModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyJpushReceiver extends BroadcastReceiver {
    private static final String TAG = "SJY";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        //点击通知，打开指定界面处理
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

            //打开自定义的Activity

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {//自定义消息.不在通知栏显示

            AttendModel model = new AttendModel();
            //保存message

            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            Log.d(TAG, "message=" + message);
            model.setMessage(message);

            //保存 type id
            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Log.d(TAG, "extra=" + extra);
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(extra);
                String Type = jsonObject.getString("Type");
                String Id = jsonObject.getString("Id");
                Log.d(TAG, "Type=" + Type);
                Log.d(TAG, "Id=" + Id);
                model.setId(Id);
                model.setType(Type);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "异常=" + e.toString());
            }

            //保存时间
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(calendar.getTime());
            Log.d(TAG, "date=" + date);
            model.setDate(date);

            //绑定消息到mainActivity中显示
            Intent msgIntent = new Intent(ListDataFragment.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra("AttendModel", model);
            context.sendBroadcast(msgIntent);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) { //接收到推送下来的通知

            //获取通知的ID,内容为空，id = 0
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

            //通知标题
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);

            //通知内容
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);

            //附加字段
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

            //富媒体通知推送下载的HTML的文件路径,用于展现WebView
            String fileHtml = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_PATH);

            //富媒体通知推送下载的图片资源的文件名,多个文件名用 “，” 分开
            String fileStr = bundle.getString(JPushInterface.EXTRA_RICHPUSH_HTML_RES);
            String[] fileNames = fileStr.split(",");

            //大图片通知样式中大图片的路径
            String bigPicPath = bundle.getString(JPushInterface.EXTRA_BIG_PIC_PATH);

        } else if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            // 获取注册id
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            //send the Registration Id to your server..

            //通知中设置按钮样式时，使用该操作
        } else if (JPushInterface.ACTION_NOTIFICATION_CLICK_ACTION.equals(intent.getAction())) {


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }
}
