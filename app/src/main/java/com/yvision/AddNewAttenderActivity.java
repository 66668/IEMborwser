package com.yvision;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.yvision.adapter.DepartmentSpinnerAdapter;
import com.yvision.adapter.GroupSpinnerAdapter;
import com.yvision.base.BaseActivity;
import com.yvision.common.HttpParameter;
import com.yvision.common.MyException;
import com.yvision.dialog.Loading;
import com.yvision.helper.UserHelper;
import com.yvision.inject.ViewInject;
import com.yvision.model.DepartmentModel;
import com.yvision.model.GroupModel;
import com.yvision.utils.ImageUtils;
import com.yvision.utils.PageUtil;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * 添加考勤人员
 * Created by sjy on 2016/11/11.
 */

public class AddNewAttenderActivity extends BaseActivity {
    //back
    @ViewInject(id = R.id.layout_back, click = "forBack")
    RelativeLayout layout_back;

    //
    @ViewInject(id = R.id.tv_title)
    TextView tv_title;

    //
    @ViewInject(id = R.id.tv_right, click = "refresh")
    TextView tv_right;

    // 注册
    @ViewInject(id = R.id.Register_btn_register, click = "btnRegister")
    Button btnRegister;

    // 姓名

    @ViewInject(id = R.id.RegisterActivity_et_name)
    EditText etName;

    // 编号
    @ViewInject(id = R.id.RegisterActivity_et_strUserIDNumber)
    EditText etIDNumber;

    // 性别
    @ViewInject(id = R.id.registerActivity_radiogroup)
    RadioGroup group_gender;

    // 男
    @ViewInject(id = R.id.radioBtn_male)
    RadioButton radioBtn_male;

    // 女
    @ViewInject(id = R.id.radioBtn_female)
    RadioButton radioBtn_female;

    // imageView
    @ViewInject(id = R.id.imageView_picture, click = "btnSnapShot")
    ImageView imgView;

    // 人脸库
    @ViewInject(id = R.id.spinnerType)
    Spinner spinnerType;

    // 部门库
    @ViewInject(id = R.id.spinnerDepartment)
    Spinner spinnerDepartment;

    //常量
    public static final int FACE_DATABASE_SUCCESS = -29;// 人脸库
    public static final int DEPARTMENT_DATABASE_SECCESS = -28;// 部门库
    public static final int SUCCESS_REGISTER = -27;// 注册成功
    public static final int FAILED_REGISTER = -30;// 注册成功
    private static final int REQUEST_CAMERA = 0;//自定义相机
    //变量
    //    private CameraGalleryUtils updateAvatarUtil;// 头像上传工具
    //    // 外部jar包：universal-image-loader-1.9.2.jar/异步加载图片
    //    private DisplayImageOptions imgOption;
    //    private ImageLoader imgLoader;
    //    private String avatarBase64 = "";

    // 注册
    private File picPath = null;
    private Point mPoint;//获取屏幕像素尺寸
    //人脸库
    ArrayList<GroupModel> groupIDList;// 人脸库列表
    GroupSpinnerAdapter groupSpinnerAdapter;
    String groupID = "";//人脸库groupID
    String operatorName = "";//操作者工号
    String employeeID = "";//员工编号
    String companyID = "";//公司编号storeID
    String type = "1";//默认 1为考勤，3 为vip，4为门禁，必填
    //部门库
    ArrayList<DepartmentModel> departmentList;//部门库
    DepartmentSpinnerAdapter departmentSpinnerAdapter;
    String deptID = "";//部门ID
    String name = "";//姓名
    String gender = "0";//1表示男，0表示女
    String workID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_new_employee_register);

        tv_title.setText("注册考勤");
        tv_right.setText("");

        //获取屏幕像素尺寸
        Display display = getWindowManager().getDefaultDisplay();
        mPoint = new Point();
        display.getSize(mPoint);

        getData();
        initRegisterView();
    }

    private void getData() {
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    //获取人像库信息
                    JSONArray jsonArrayGroupID = UserHelper.getAttendFaceDatabase(AddNewAttenderActivity.this);
                    sendMessage(FACE_DATABASE_SUCCESS, jsonArrayGroupID);
                    //获取部门库
                    JSONArray jsonArrayDepartment = UserHelper.getDataDepartment(AddNewAttenderActivity.this);
                    sendMessage(DEPARTMENT_DATABASE_SECCESS, jsonArrayDepartment);
                } catch (MyException e) {
                    Log.d("SJY", "异常=" + e.getMessage());
                }
            }
        });
    }

    private void initRegisterView() {

        // 性别默认设置
        gender = "1";
        radioBtn_male.setChecked(true);//默认性别 男

        //选择人脸库类型
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (groupIDList == null) {
                        return;
                    }
                    groupID = groupSpinnerAdapter.getItem(position).getGroupID() + "";
                    type = getType(groupSpinnerAdapter.getItem(position).getGroupName());
                    Log.d("SJY", "AddNewEmployeeActivity--人脸库类型--type=" + getType(groupSpinnerAdapter.getItem(position).getGroupName()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //部门库类别
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (departmentList == null) {
                        return;
                    }
                    deptID = departmentSpinnerAdapter.getItem(position).getDeptID() + "";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * 选择照片
     */
    public void btnSnapShot(View view) {
        ////自定义相机1
        //        cameraGalleryUtils.showChoosePhotoDialog(CameraGalleryUtils.IMG_TYPE_CAMERA);//-99

        //自定义相机2
        Intent mCameraIntent = new Intent(this, MyChangeCameraActivity.class);
        startActivityForResult(mCameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        updateAvatarUtil.onActivityResultAction(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = data.getData();
            picPath = new File(ImageUtils.getImageAbsolutePath(this, photoUri));
            //            picPath = new File(photoUri.toString());
            // Get the bitmap in according to the width of the device
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromPath(photoUri.getPath(), mPoint.x, mPoint.x);
            imgView.setImageBitmap(bitmap);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 注册
     */
    public void btnRegister(View view) {
        Log.d("SJY", "注册1");
        UUID guid = UUID.randomUUID();
        employeeID = guid.toString();//员工编号
        operatorName = UserHelper.getCurrentUser().getUserName();//操作人姓名/登录时的工号
        companyID = UserHelper.getCurrentUser().getStoreID();//storeID公司编号
        name = etName.getText().toString().trim();//姓名
        gender = group_gender.getCheckedRadioButtonId() == R.id.radioBtn_male ? "1" : "0";//性别
        workID = etIDNumber.getText().toString().trim();//工号

        //非空验证
        if (picPath == null) {
            PageUtil.DisplayToast("无法添加，请选择图片");
            return;
        }
        if (TextUtils.isEmpty(name.trim())) {
            PageUtil.DisplayToast("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(workID.trim())) {
            PageUtil.DisplayToast("请输入工号");
            return;
        }
        if (TextUtils.isEmpty(groupID.trim())) {
            PageUtil.DisplayToast("请选择人像库");
            return;
        } if (TextUtils.isEmpty(deptID.trim())) {
            PageUtil.DisplayToast("请选择部门");
            return;
        }

        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    // 对数据处理
                    int code = UserHelper.registerNew(AddNewAttenderActivity.this,
                            HttpParameter.create().add("operatorName", operatorName).
                                    add("employeeID", employeeID).
                                    add("groupID", groupID).
                                    add("storeID", UserHelper.getCurrentUser().getStoreID()).
                                    add("name", name).
                                    add("operatorName", UserHelper.getCurrentUser().getUserName()).
                                    add("departmentID", deptID).
                                    add("gender", gender).
                                    add("type", type).
                                    add("workID", workID),
                            picPath);
                    // 消息处理
                    sendMessage(SUCCESS_REGISTER, code);
                } catch (MyException e) {
                    sendMessage(FAILED_REGISTER, e.getMessage());
                }
            }
        });
    }

    public String getType(String groupName) {
        if (groupName.contains("考勤")) {
            return "1";
        } else if (groupName.contains("门禁")) {
            return "4";
        } else if (groupName.contains("VIP") || groupName.contains("vip")) {
            return "3";
        } else {
            return "1";//不选默认考勤
        }
    }


    @Override
    protected void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case SUCCESS_REGISTER:
                if (((Integer) msg.obj).toString().equals("1")) {
                    new AlertDialog.Builder(this).setTitle("注册成功！").setPositiveButton("确定", null).create()
                            .show();
                    setClear();
                } else {
                    new AlertDialog.Builder(this).setTitle("注册失败，重新注册！").setNegativeButton("取消", null)
                            .create().show();
                }
                break;
            case FAILED_REGISTER:
                PageUtil.DisplayToast((String) msg.obj);
                break;
            case FACE_DATABASE_SUCCESS:
                Log.d("SJY", "已连接人脸库");
                bindFaceData((JSONArray) msg.obj);
                break;
            case DEPARTMENT_DATABASE_SECCESS:
                Log.d("SJY", "已连接部门库");
                //绑定数据
                bindDepartmentData((JSONArray) msg.obj);
                break;
            default:
                break;
        }
    }

    private void setClear() {
        etName.setText("");
        etIDNumber.setText("");
        picPath = null;
    }

    ;

    private void bindFaceData(JSONArray jsonArray) {
        groupIDList = new ArrayList<GroupModel>();
        String groupID = null;
        String groupName = null;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                groupID = jsonArray.getJSONObject(i).getString("groupID");
                groupName = jsonArray.getJSONObject(i).getString("groupName");
                GroupModel groupModel = new GroupModel();
                groupModel.setGroupID(groupID);
                groupModel.setGroupName(groupName);
                groupIDList.add(groupModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        groupSpinnerAdapter = new GroupSpinnerAdapter(this, groupIDList);
        spinnerType.setAdapter(groupSpinnerAdapter);

        //获取登录人model,并将登录人中的信息获取出来（不适合本app要求）
        //        try {
        //            spinnerType.setSelection(getGroupIDIndex(vfaceMemberModel.getProvinceId()));
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }

    }

    private void bindDepartmentData(JSONArray jsonArray) {
        departmentList = new ArrayList<DepartmentModel>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                deptID = jsonArray.getJSONObject(i).getString("DeptID");
                String deptName = jsonArray.getJSONObject(i).getString("DeptName");
                DepartmentModel departmentModel = new DepartmentModel();
                departmentModel.setDeptID(deptID);
                departmentModel.setDeptName(deptName);
                departmentList.add(departmentModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        departmentSpinnerAdapter = new DepartmentSpinnerAdapter(this, departmentList);
        spinnerDepartment.setAdapter(departmentSpinnerAdapter);

        //获取登录人model,并将登录人中的信息获取出来（不适合本app要求）
        //        try {
        //            spinnerType.setSelection(getGroupIDIndex(vfaceMemberModel.getProvinceId()));
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }

    }

    //    private int getGroupIDIndex(int provinceId) {
    //        for (int i = 0; i < groupIDList.size(); i++) {
    //            if (provinceId == groupIDList.get(i).getGroupID()) {
    //                return i;
    //            }
    //        }
    //        return 0;
    //    }


    /**
     * 返回
     */
    public void forBack(View v) {
        this.finish();
    }

}
