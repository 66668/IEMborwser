package com.yvision.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yvision.R;

/**
 * jpush自定义消息 的自定义弹窗显示
 * Created by sjy on 2017/4/5.
 */

public class JpushMsgToast {
    private Toast mToast;

    private JpushMsgToast(Context context, CharSequence type, CharSequence msg, CharSequence time,int duration) {
        //init
        View v = LayoutInflater.from(context).inflate(R.layout.toast_jpush_show, null);

        TextView tv_msg = (TextView) v.findViewById(R.id.tv_msg);
        TextView tv_type = (TextView) v.findViewById(R.id.tv_type);
        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);

        tv_type.setText(type);
        tv_msg.setText(msg);
        tv_time.setText(time);

        mToast = new Toast(context);
        mToast.setDuration(duration);
        mToast.setView(v);
    }

    public static JpushMsgToast makeText(Context context, CharSequence type, CharSequence messge, CharSequence time,int duration) {
        return new JpushMsgToast(context, type, messge, time,duration);
    }

    public void show() {
        if (mToast != null) {
            mToast.show();
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast != null) {
            mToast.setGravity(gravity, xOffset, yOffset);
        }
    }
}
