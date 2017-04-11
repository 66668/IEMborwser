package com.yvision.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.yvision.R;
import com.yvision.fragment.ListDataFragment;
import com.yvision.model.AttendModel;

import org.json.JSONException;
import org.json.JSONObject;

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

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {//自定义消息.不在通知栏显示

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
                String DateTime = jsonObject.getString("DateTime");
                String Pic = jsonObject.getString("Pic");

                model.setId(Id);
                model.setType(Type);
                model.setCapTime(DateTime);
                model.setPic(Pic);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "异常=" + e.toString());
            }

            //绑定消息到mainActivity中显示
            Intent msgIntent = new Intent(ListDataFragment.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra("AttendModel", model);
            context.sendBroadcast(msgIntent);

            processCustomMessage(context, bundle);//声音提示
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

        }
    }

    /**
     * 实现自定义推送声音(原本无通知栏，加了该方法后又显示通知栏)
     *
     * @param context
     * @param bundle
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Ticker是状态栏显示的提示
        builder.setTicker(bundle.getString(JPushInterface.EXTRA_TITLE));
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
        //第二行内容 通常是通知正文
        builder.setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        //可以点击通知栏的删除按钮删除
        builder.setAutoCancel(true);
        //系统状态栏显示的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        Notification notification = builder.build();
        notification.sound = Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.push_notification_price_sound);
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        Intent clickIntent = new Intent(); //点击通知之后要发送的广播
        int id = (int) (System.currentTimeMillis() / 1000);
        //        clickIntent.addCategory(MyApplication.getAppPackageName(context));
        clickIntent.setAction(JPushInterface.ACTION_NOTIFICATION_OPENED);
        clickIntent.putExtra(JPushInterface.EXTRA_EXTRA, bundle.getString(JPushInterface.EXTRA_EXTRA));
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, id, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = contentIntent;
        manger.notify(id, notification);
    }
}
