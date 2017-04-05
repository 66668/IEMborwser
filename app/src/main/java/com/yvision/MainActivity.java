package com.yvision;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yvision.base.BaseActivity;
import com.yvision.base.BaseFragment;
import com.yvision.common.MyException;
import com.yvision.dialog.Loading;
import com.yvision.fragment.ListDataFragment;
import com.yvision.fragment.RegistFragment;
import com.yvision.helper.UserHelper;
import com.yvision.inject.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    //退出
    @ViewInject(id = R.id.tv_quit, click = "quit")
    TextView tv_quit;

    //listView
    @ViewInject(id = R.id.listView)
    ListView listView;

    //
    private static final int QUIT_SCCESS = -39;


    //
    private RadioGroup mRadioGroup;
    private RadioButton msg_btn;
    private RadioButton reg_btn;
    private ViewPager viewPaper;

    private RegistFragment registFragment;
    private ListDataFragment listDataFragment;
    private List<BaseFragment> listFragment;
    private int currentFragment;

    //
    //接受JpushReceiver的msg
    private static final String TAG = "SJY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initMyView();
        initViewPaperAndFragment();
        initListener();
    }

    private void initMyView() {
        viewPaper = (ViewPager) findViewById(R.id.viewpager);
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        msg_btn = (RadioButton) findViewById(R.id.btn_message);
        reg_btn = (RadioButton) findViewById(R.id.btn_vip);


    }

    private void initViewPaperAndFragment() {
        listDataFragment = ListDataFragment.newInstance();
        registFragment = RegistFragment.newInstance();
        listFragment = new ArrayList<>();
        listFragment.add(listDataFragment);
        listFragment.add(registFragment);
        viewPaper.setOffscreenPageLimit(2);
        viewPaper.setOnPageChangeListener(onPageChangeListener);
    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    mRadioGroup.check(R.id.btn_message);
                    break;
                case 1:
                    mRadioGroup.check(R.id.btn_vip);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    /**
     * 设置监听
     */
    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.btn_message:
                        currentFragment = 0;
                        break;

                    case R.id.btn_vip:
                        currentFragment = 1;
                        break;


                }


                viewPaper.setCurrentItem(currentFragment, false);

            }
        });

        viewPaper.setAdapter(new FragmentPagerAdapter(
                getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return listFragment.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return listFragment.get(arg0);
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                super.destroyItem(container, position, object);
            }

        });
    }


    @Override
    protected void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case QUIT_SCCESS:
                Intent intent = new Intent();
                intent.setAction(EXIT_APP_ACTION);
                sendBroadcast(intent);//发送退出的广播
                break;


        }
    }


    /**
     * 退出
     */
    public void quit(View view) {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    UserHelper.logout(getApplicationContext());
                    sendMessage(QUIT_SCCESS);
                } catch (MyException e) {
                    Log.d("SJY", "异常=" + e.getMessage());
                }
            }
        });

    }
}
