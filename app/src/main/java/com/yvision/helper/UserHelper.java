package com.yvision.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yvision.R;
import com.yvision.application.MyApplication;
import com.yvision.common.HttpParameter;
import com.yvision.common.HttpResult;
import com.yvision.common.MyException;
import com.yvision.common.NetworkManager;
import com.yvision.db.entity.UserEntity;
import com.yvision.model.AttendDetailModel;
import com.yvision.model.AttendModel;
import com.yvision.model.OldEmployeeImgModel;
import com.yvision.model.OldEmployeeModel;
import com.yvision.utils.APIUtils;
import com.yvision.utils.ConfigUtil;
import com.yvision.utils.JSONUtils;
import com.yvision.utils.Utils;
import com.yvision.utils.WebUrl;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理者帮助类
 * <p/>
 * 处理访问服务端信息类 解析js对象，调用Gson外部包：gson-2.2.2.jar
 *
 * @author JackSong
 */
public class UserHelper {
    static UserEntity mCurrentUser;
    static String configUserManager = null;//

    /**
     * (1)获取用户id(工号)
     */
    static String workId = null;

    public static String getCongfigworkId() {
        if (workId == null) {
            ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
            workId = config.getUserName();
        }
        return workId;
    }

    /**
     * (2)获取用户账号
     *
     * @return
     */
    public static UserEntity getCurrentUser() {
        // 调用下边的方法
        return getCurrentUser(true);
    }

    public static UserEntity getCurrentUser(boolean isAutoLoad) {

        if (mCurrentUser == null && isAutoLoad) {// 判断MemberModel类是否为空
            // 中断保存
            ConfigUtil config = new ConfigUtil(MyApplication.getInstance());// 中断保存获取信息
            String workId = config.getUserName();
            if (!"".equals(workId)) {
                // 获取所有当前用户信息，保存到mCurrentUser对象中
                mCurrentUser = config.getUserEntity();
            }
        }
        return mCurrentUser;
    }

    /**
     * 退出登录
     */
    public static void logout(Context context) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);
        try {

            HttpResult httpResult = APIUtils.postForObject(WebUrl.User.QUIT_OUT,
                    HttpParameter.create()
                            .add("storeId", UserHelper.getCurrentUser().getStoreID().trim())
                            .add("userName", UserHelper.getCurrentUser().getUserName().trim())
                            .add("deviceId", UserHelper.getCurrentUser().getDeviceId().trim()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }

            ConfigUtil configUtil = new ConfigUtil(context);
            configUtil.setAutoLogin(false);
            //修改自动登录的判断
            MyApplication.getInstance().setIsLogin(false);
        } catch (Exception e) {
            throw new MyException(e.getMessage());
        } finally {
            mCurrentUser = null;
        }
    }

    /**
     * 01 密码登录
     */
    public static void loginByPs(Context context, String adminUserName, String userName, String password, String clientID) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);
        HttpResult httpResult = APIUtils.postForObject(WebUrl.LOGIN_POST,
                HttpParameter.create().
                        add("adminUserName", adminUserName).//storeid
                        add("userName", userName).//workid
                        add("password", password).

                        add("MAC", Utils.getMacByWifi()).//手机mac
                        add("IP", Utils.getIPAddress(context)).//手机ip
                        add("deviceType", Utils.getPhoneModel()).//手机设备类型
                        add("deviceName", userName).//手机设备名称

                        add("Remark", "").//
                        add("DeviceSN", "").//
                        add("DeviceInfo", clientID + "@1001"));//

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }


        UserEntity userEntity = new UserEntity();
        //返回值
        userEntity.setDeviceId(JSONUtils.getString(httpResult.jsonObject, "DeviceId"));//设备id
        userEntity.setStoreID(JSONUtils.getString(httpResult.jsonObject, "StoreId"));//公司id
        userEntity.setEmployeeId(JSONUtils.getString(httpResult.jsonObject, "EmployeeId"));//员工id
        userEntity.setStoreUserId(JSONUtils.getString(httpResult.jsonObject, "StoreUserId"));//用户id

        userEntity.setUserName(userName);
        userEntity.setPassword(password);
        userEntity.setAdminUserName(adminUserName);
        userEntity.setClientID(clientID);

        // ConfigUtil中断保存，在退出后重新登录用getAccount()调用
        ConfigUtil config = new ConfigUtil(MyApplication.getInstance());
        config.setAdminUserName(adminUserName);// 保存公司编号
        config.setUserName(userName);// 保存用户名
        config.setPassword(password);
        config.setUserEntity(userEntity);// 保存已经登录成功的对象信息
        mCurrentUser = userEntity;// 将登陆成功的对象信息，赋值给全局变量
    }

    /**
     * 02 GetEmployeeListByStoreID
     * <p/>
     * 获取受访者(添加访客时，填写受访者的调用)
     *
     * @throws MyException
     */
    public static JSONArray getEmployeeListByStoreID(Context context, String storeId, String typeN) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        //        String newUrl = new String(WebUrl.UserManager.GET_RESPONDENTS + storeId + "/" + typeN);
        String newUrl = new String(WebUrl.GET_RESPONDENTS + storeId + "/" + typeN);
        HttpResult httpResult = APIUtils.getForObject(newUrl);
        /**
         * json转换到userManager中，现在不需要--0802
         */
        // // Gson.jar包,解析HttpResult中的jsonArray 给对象UserManagers
        // UserEntity userManagers = null;
        // List<UserEntity> list = new ArrayList<UserEntity>();
        // JSONArray jsonArray = httpResult.jsonArray;
        // Iterable<JsonElement> iterable = (Iterable<JsonElement>) jsonArray;
        // Iterator<JsonElement> iterator = iterable.iterator();
        // while(iterator.hasNext()){
        // JsonElement element = (JsonElement)iterator.next();
        // userManagers = new Gson().fromJson(element, UserEntity.class);
        // list.add(userManagers);
        // //数组赋值
        // userManagers.setList(list);
        // }
        // mCurrentUser = userManagers;//将登陆成功的对象信息，赋值给全局变量
        /**
         * 完成代码后，需要加上--0802
         */
        // if (httpResult.hasError()) {
        // throw httpResult.getError();
        // }

        return httpResult.jsonArray;
    }

    /**
     * 03 AddOneVisitorRecord  添加访客
     *
     * @throws MyException
     */
    public static String addOneVisitorRecord(Context context, String parameters, File fileName) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        HttpResult hr = APIUtils.postForObject(WebUrl.ADD_VISITORRECORD,
                HttpParameter.create().add("obj", parameters),
                fileName);
        if (hr.hasError()) {
            throw hr.getError();
        }
        return hr.Message;
    }

    /**
     * 获取老员工列表(用于修改老员工图片)
     * get
     */
    public static ArrayList<OldEmployeeModel> getOldEmployList(Context context) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        HttpResult httpResult = APIUtils.getForObject(WebUrl.GET_OLD_EMPLOYEE_LIST + UserHelper.getCurrentUser().getStoreID());

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonArray.toString(),
                new TypeToken<List<OldEmployeeModel>>() {
                }.getType());
    }

    /**
     * 获取老员工详细信息
     * get
     */
    public static OldEmployeeModel getOldEmployeeDetails(Context context, String emloyeeID) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }

        HttpResult httpResult = APIUtils.getForObject(WebUrl.GET_OLD_EMPLOYEE_DETAILS + emloyeeID);
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonObject.toString(), OldEmployeeModel.class);
    }

    /**
     * 获取老员工图片列表
     * get
     */
    public static List<OldEmployeeImgModel> getOldEmployeeImgDetails(Context context, String emloyeeID) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        String url = WebUrl.GET_OLD_EMPLOYEE_IMG + emloyeeID;
        Log.d("SJY", url);

        HttpResult httpResult = APIUtils.getForObject(url);
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<List<OldEmployeeImgModel>>() {
        }.getType());
    }


    /**
     * 获取考勤详情
     * get
     */
    public static AttendDetailModel getAttendDetail(Context context, String attendId)
            throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        HttpResult httpResult = APIUtils.getForObject(WebUrl.Attend.EMPLOYEE_DETAIL + attendId);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonObject.toString(), AttendDetailModel.class);
    }

    /**
     * 14获取 vip 人脸库
     */

    public static JSONArray getVIPFaceDatabase(Context context) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        }
        String companyID = UserHelper.getCurrentUser().getStoreID();//公司编号companyID
        String url = WebUrl.GET_FACE_DATEBASE_BY_COMPANYID + "/" + companyID + "/3";
        HttpResult httpResult = APIUtils.getForObject(url);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        //       if(httpResult.jsonArray != null){
        //            return (new Gson()).fromJson(httpResult.jsonArray.toString(),
        //                    new TypeToken<List<GroupModel>>() {
        //                    }.getType());
        //        }else{
        //           //后台js不标准，多这一步保险
        //           return (new Gson()).fromJson(httpResult.resultJsonString.toString(),
        //                   new TypeToken<List<GroupModel>>() {
        //                   }.getType());
        //        }
        Log.d("SJY", "人脸库jsonArray=" + httpResult.jsonArray.toString());
        return httpResult.jsonArray;
    }

    /**
     * 14获取 考勤 人脸库
     */

    public static JSONArray getAttendFaceDatabase(Context context) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        }
        String companyID = UserHelper.getCurrentUser().getStoreID();//公司编号companyID

        //        String url = WebUrl.GET_FACE_DATEBASE_BY_COMPANYID +"?companyID="+ companyID + "&groupType=1";
        String url = WebUrl.GET_FACE_DATEBASE_BY_COMPANYID + "/" + companyID + "/1";

        HttpResult httpResult = APIUtils.getForObject(url);
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        //       if(httpResult.jsonArray != null){
        //            return (new Gson()).fromJson(httpResult.jsonArray.toString(),
        //                    new TypeToken<List<GroupModel>>() {
        //                    }.getType());
        //        }else{
        //           //后台js不标准，多这一步保险
        //           return (new Gson()).fromJson(httpResult.resultJsonString.toString(),
        //                   new TypeToken<List<GroupModel>>() {
        //                   }.getType());
        //        }
        Log.d("SJY", "人脸库jsonArray=" + httpResult.jsonArray.toString());
        return httpResult.jsonArray;
    }

    /**
     * 获取部门库
     *
     * @param context
     * @return
     * @throws MyException
     */
    public static JSONArray getDataDepartment(Context context) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        }

        String companyID = UserHelper.getCurrentUser().getStoreID();//公司编号companyID/ 5596c761-493a-48f4-8f18-1e6191537405

        HttpResult httpResult = APIUtils.getForObject(WebUrl.GET_DEPARTMENT_DATEBASE_BY_COMPANYID + companyID);

        Log.d("SJY", "部门库jsonArray=" + httpResult.jsonArray.toString());

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        //       if(httpResult.jsonArray != null){
        //            return (new Gson()).fromJson(httpResult.jsonArray.toString(),
        //                    new TypeToken<List<GroupModel>>() {
        //                    }.getType());
        //        }else{
        //           //后台js不标准，多这一步保险
        //           return (new Gson()).fromJson(httpResult.resultJsonString.toString(),
        //                   new TypeToken<List<DepartmentModel>>() {
        //                   }.getType());
        //        }
        return httpResult.jsonArray;
    }

    /**
     * 15新员工注册
     *
     * @throws MyException
     */

    public static int registerNew(Context context, HttpParameter params, File picPath) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        }

        // 访问服务端：http://192.168.1.127:2016/api/Attend/RegAttFace
        HttpResult httpResult = APIUtils.postForObject(WebUrl.POST_NEW_EMPLOYEE, params, picPath);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return httpResult.code;
    }

    /**
     * 获取离线推送
     */
    public static ArrayList<AttendModel> getOffLineDate(Context context, HttpParameter params) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        }
        // 访问服务端：http://192.168.1.127:2016/api/Attend/RegAttFace
        HttpResult httpResult = APIUtils.postForObject(WebUrl.GET_OFFLINE_DATA, params);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonArray.toString(), new TypeToken<ArrayList<AttendModel>>() {
        }.getType());

    }
}
