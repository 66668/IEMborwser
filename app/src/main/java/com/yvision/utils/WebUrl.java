package com.yvision.utils;

public class WebUrl {
    /**
     * 根接口
     */
    // 云端
    //    public static final String LOGIN_URL = "http://101.201.72.112:9016/";//公司使用地址
    //    public static final String LOGIN_URL = "http://101.201.72.112:7016/";//测试地址
    //    public static final String LOGIN_URL = "http://192.168.1.85:9011/";//江苏银行
    public static final String LOGIN_URL = "http://192.168.1.245:1132/"; //IEM测试地址


    /**
     * 注册根目录
     */
    public static final String API = "openapi/";



    /**
     * 使用者管理
     *
     * @author JackSong
     */
    public static class User {
        /**
         * 退出
         */
        public static final String QUIT_OUT = LOGIN_URL + API + "User/AppDeviceLogoutByPassword";
    }

    /**
     * 01 密码登录
     * 测试url:http://192.168.1.127:9012/openapi/User/LoginByPassword
     */
    public static final String LOGIN_POST = LOGIN_URL + API + "User/AppDeviceLoginByPassword";


    /**
     * 02 修改登录密码
     * http://192.168.1.127:9012/openapi/User/ChangePasswordN
     */
    public static final String CHANGE_PASSWORD = LOGIN_URL + API + "User/ChangePasswordN";

  /**
     * 获取登录人信息
     */
    public static final String GET_PERSON_MESSAGE= LOGIN_URL + API + "User/Main/GetEmployeeInfoByID/";


    /**
     * 03 获取受访者
     * <p>
     * get
     * <p>
     * http://192.168.1.127:9012/openapi/Main/GetEmployeeListByStoreID/{storeID}/{typeN}
     */
    public static final String GET_RESPONDENTS = LOGIN_URL + API + "Main/GetEmployeeListByStoreID/";


    /**
     * 访客管理
     *
     * @author JackSong
     */
    public class VisitorManager {

    }

    /**
     * 01 获取所有信息列表 GetVisitorRecordsByPage 0819之前使用接口
     * http://192.168.1.127:9012/openapi/Main/GetVisitorRecordsByPage
     */
    public static final String GET_RRECORD_BYPAGE = LOGIN_URL + API + "Main/GetVisitorRecordsByPage";

    /**
     * 02获取特定时间的记录 GetVisitorRecordsByPageA 0819以后使用的接口
     */
    public static final String GET_RRECORD_BYPAGEA = LOGIN_URL + API + "Main/GetVisitorRecordsByPageA";
    /**
     * 03 添加访客
     * http://192.168.1.127:9012/openapi/Main/AddOneVisitorRecord
     */
    public static final String ADD_VISITORRECORD = LOGIN_URL + API + "Main/AddOneVisitorRecord";


    /**
     * 04 删除一条记录
     * http://192.168.1.127:9012/openapi/Main/DeleteVisitorRecordsByIDList
     */
    public static final String DELET_VISITORRECORD = LOGIN_URL + API + "Main/DeleteVisitorRecordsByIDList";


    /**
     * 05 获取一条记录的详细信息
     * http://192.168.1.127:9012/openapi/Main/GetOneVisitorRecordByID/{recordID}/{storeId}
     */
    public static final String GET_ONE＿VISITORRECORD = LOGIN_URL + API + "Main/GetOneVisitorRecordByID";

    /**
     * 06 搜索 GetVisitorWithSameName
     * 获取同名访客信息
     * http://192.168.1.127:9012/openapi/Main/GetVisitorWithSameName
     */
    public static final String GET_VISITOR_WITH_SAME_NAME = LOGIN_URL + API + "Main/GetVisitorWithSameName";


    /**
     * 07 修改  UpdateOneVisitorRecord 
     * http://192.168.1.127:9012/openapi/Main/UpdateOneVisitorRecord
     */
    public static final String UPDATE_ONE_VISITORRECORD = LOGIN_URL + API + "Main/UpdateOneVisitorRecord";

    /**
     * 08 更新版本 CheckVersion 
     */
    public static final String CLIENT_UPGRADE_URL = LOGIN_URL + API + "Main/CheckVersion";


    /**
     * 02考勤管理
     */
    public static class Attend {
        /**
         * 01获取考勤记录
         * <p>
         * http://101.201.72.112:9016/openapi/Main/GetAttendanceRecordByPage
         * httppost
         */

        public static final String GET_ATTENDANCE_BYPAGE = LOGIN_URL + API + "Attend/GetAttendanceRecordByPage";

        /**
         * 地图签到
         */
        public static final String POST_ATTENDANCE_MAP = LOGIN_URL + API + "Attend/AddOneMapAttendanceRecord";

        /**
         * 02 根据employeeID查看登录人信息
         * <p>
         * httpget
         */
        public static final String GET_ONE＿EMPLOYEE_INFO = LOGIN_URL + API + "Main/GetEmployeeInfoByID/";

        /**
         * 03 获取详细考勤记录
         * <p>
         * httpget
         */
        public static final String EMPLOYEE_DETAIL = LOGIN_URL + API + "Attend/GetOneAttendanceRecordByID/";

    }

    /**
     * 04vip管理
     */
    public static class Vip {
        /**
         * VIP记录
         */
        public static final String GET_VIP_LIST = LOGIN_URL + API + "VIP/GetVIPRecordByPage";

        /**
         * 获取详细vip
         * <p>
         * httpget
         */
        public static final String VIP_DETAIL = LOGIN_URL + API + "VIP/GetVipRecordByID/";
    }

    /**
     * 05门禁管理
     */
    public static class DoorAccess {
        /**
         *  门禁记录
         */
        public static final String GET_DOOR_ACCESS_LIST = LOGIN_URL + API + "DoorAccess/GetDoorAccessRecordByPage";
        /**
         * 获取门禁详细
         * <p>
         * httpget
         */
        public static final String DOORACCESS_DETAIL = LOGIN_URL + API + "DoorAccess/GetOneDoorAccessRecordByID/";
    }


    /**
     * 03 添加一条地图考勤记录
     * http://101.201.72.112:9016/openapi/Main/AddOneMapAttendanceRecord
     * httppost
     */
    public static final String ADD_ONE＿MAP_ATTENDANCE = LOGIN_URL + API + "Main/AddOneMapAttendanceRecord";

    /**
     * 04 五、添加一条地wifi考勤记录
     * http://101.201.72.112:9016/openapi/Main/AddOneWIFIAttendanceRecord
     * httppost
     */
    public static final String ADD_ONE＿WIFI_ATTENDANCE = LOGIN_URL + API + "Main/AddOneWIFIAttendanceRecord";


    /**
     * 获取人脸库
     * <p>
     */
    //		public static final String GET_FACE_DATEBASE_BY_COMPANYID = REGISTER_URL+ API+ "Attend/GetGroupByCyId/";
    public static final String GET_FACE_DATEBASE_BY_COMPANYID = LOGIN_URL + API + "Attend/GetGroupListByType";

    /**
     * 获取部门库
     * <p>
     * 正式地址：http://192.168.1.127:1132/openapi/Main/GetDeptByCyId/{companyID}
     */
    //		public static final String GET_DEPARTMENT_DATEBASE_BY_COMPANYID =  REGISTER_URL+ API+ "Main/GetDeptByCyId/";
    public static final String GET_DEPARTMENT_DATEBASE_BY_COMPANYID = LOGIN_URL + API + "Main/GetDeptByCyId/";

    /**
     * 新员工注册接口
     * <p>
     * <p>
     * 地址： http://192.168.1.127:1132/openapi/EmployeeImage/AddEmployeeAndImage
     */
    //		public static final String POST_NEW_EMPLOYEE = REGISTER_URL+ API+ "EmployeeImage/AddEmployeeAndImage";
    public static final String POST_NEW_EMPLOYEE = LOGIN_URL + API + "EmployeeImage/AddEmployeeAndImage";

    /**
     * 老员工注册接口
     * <p>
     * <p>
     * 地址：
     */
    //		public static final String POST_OLD_EMPLOYEE = REGISTER_URL+API+"Employee/AddImageAndType";
    public static final String POST_OLD_EMPLOYEE = LOGIN_URL + API + "Employee/AddImageAndType";

    /**
     * 获取老员工列表
     */
    //		public static final String GET_OLD_EMPLOYEE_LIST = REGISTER_URL+API+"Employee/GetEmployeeList/";
    public static final String GET_OLD_EMPLOYEE_LIST = LOGIN_URL + API + "Employee/GetEmployeeList/";

    /**
     * 获取老员工详细信息
     */
    //		public static final String GET_OLD_EMPLOYEE_DETAILS = REGISTER_URL+API+"Employee/GetEmployeeByID/";
    public static final String GET_OLD_EMPLOYEE_DETAILS = LOGIN_URL + API + "Employee/GetEmployeeByID/";

    /**
     * 获取老员工图片集合信息
     */
    //		public static final String GET_OLD_EMPLOYEE_DETAILS = REGISTER_URL+API+"Employee/GetEmployeeByID/";
    public static final String GET_OLD_EMPLOYEE_IMG = LOGIN_URL + API + "Image/GetImageListByEmployeeID/";




}
