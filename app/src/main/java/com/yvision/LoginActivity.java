package com.yvision;


import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.yvision.application.MyApplication;
import com.yvision.base.BaseActivity;
import com.yvision.common.MyException;
import com.yvision.dialog.Loading;
import com.yvision.helper.UserHelper;
import com.yvision.inject.ViewInject;
import com.yvision.utils.ConfigUtil;
import com.yvision.utils.PageUtil;
import com.yvision.utils.Utils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class LoginActivity extends BaseActivity {

    // 公司
    @ViewInject(id = R.id.et_storeId)
    EditText et_storeId;

    // 工号
    @ViewInject(id = R.id.et_UserNmae)
    EditText et_UserName;

    // 密码
    @ViewInject(id = R.id.et_Password)
    EditText et_Password;

    // 登录按钮
    @ViewInject(id = R.id.btnLogin, click = "login")
    Button btnLogin;

    //常量
    private final int LOGIN_SUCESS = 2001; // 登陆成功
    private final int LOGIN_FAILED = 2002; // 失败

    private String storeId, workId, password;
    private String registRationID;//极光注册成功 会返回这个id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_login_password);

        //判断自动登录
        if (MyApplication.getInstance().isLogin()) {
            startActivity(MainActivity.class);
            this.finish();
        }

        //中断保存
        ConfigUtil configUtil = new ConfigUtil(this);
        et_UserName.setText(configUtil.getUserName());
        et_storeId.setText(configUtil.getAdminUserName());
        et_Password.setText(configUtil.getPassword());
    }


    // 登录
    public void login(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:

                PageUtil.DisplayToast("登录");
                Log.d("SJY", "登录");

                storeId = et_storeId.getText().toString().trim();
                workId = et_UserName.getText().toString().trim();
                password = et_Password.getText().toString().trim();
                registRationID = JPushInterface.getRegistrationID(getApplicationContext());
                // //非空验证
                if (!checkInput()) {
                    return;
                }
                // 线程处理登录
                Loading.run(this, new Runnable() {

                    @Override
                    public void run() {
                        try {
                            UserHelper.loginByPs(LoginActivity.this,
                                    storeId, // 公司编号
                                    workId, // 工号
                                    password,// 密码
                                    "1");//是否接受推送/0/1

                            // 访问服务端成功，消息处理
                            sendMessage(LOGIN_SUCESS);

                        } catch (MyException e) {
                            sendMessage(LOGIN_FAILED, e.getMessage());
                        }
                    }
                });
                break;
        }


    }

    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case LOGIN_SUCESS: // 1001
                //推送设置别名
                setAlias(Utils.getMacByWifi());
                //设置自动登录
                MyApplication.getInstance().setIsLogin(true);
                // 文本跳转
                startActivity(MainActivity.class);
                this.finish();// 页面注销
                break;
            case LOGIN_FAILED: // 1001
                Log.d("SJY", (String) msg.obj);
                PageUtil.DisplayToast((String) msg.obj);
                break;
            default:
                break;
        }
    }

    /*
     * 非空验证
     */
    private boolean checkInput() {
        if (TextUtils.isEmpty(et_storeId.getText().toString().trim())) {
            PageUtil.DisplayToast(R.string.LoginActivity_companyNum);// 公司编号
            return false;
        }
        if (TextUtils.isEmpty(et_UserName.getText().toString().trim())) {
            PageUtil.DisplayToast(R.string.LoginActivity_employeeNum);// 工号
            return false;
        }
        if (TextUtils.isEmpty(et_Password.getText().toString().trim())) {
            PageUtil.DisplayToast(R.string.LoginActivity_psd);
        }
        return true;
    }

    /**
     * jpush 绑定别名
     */
    private void setAlias(String alias) {
        //设置别名，替换非法字符
        final String newAlias = alias.replace(":", "_");
        Log.d("JPush", "newAlias=" + newAlias);
        JPushInterface.setAliasAndTags(getApplicationContext(), newAlias, null, new TagAliasCallback() {

            @Override
            public void gotResult(int code, String s, Set<String> set) {
                Log.d("JPush", "极光推送别名设置-->");
                switch (code) {
                    case 0:
                        Log.d("JPush", "newAlias=" + newAlias + "Set tag and alias success极光推送别名设置成功");
                        break;
                    case 6002:
                        Log.d("JPush", "newAlias=" + newAlias + "极光推送别名设置失败，Code = 6002");
                        break;
                    default:
                        Log.d("JPush", "newAlias=" + newAlias + "极光推送设置失败，Code = " + code);
                        break;
                }
            }
        });
    }

}
