package com.yvision.db.entity;

public class UserTable extends TableInfo {
    public static String C_TableName = "user";

    public static String C_userName = "workId";//登录的用户名
    public static String C_adminUserName = "adminUserName";//adminUserName 登录公司name
    public static String C_StoreID = "Store_ID";////公司name对应的id
    public static String C_StoreUserId = "StoreUserId";//用户名id
    public static String C_EmployeeId = "EmployeeId";//员工id
    public static String C_ClientID = "ClientID";//
    public static String C_URL = "url";//
    public static String C_DeviceId = "DeviceId";//

    public static String C_UserId = "user_id";
    public static String C_Fullname = "fullname";
    public static String C_StoreName = "store_name";
    public static String C_Account = "account";
    public static String C_Password = "password";
    public static String C_UserPicture = "userPicture";

    public UserTable() {
        _tableName = "user";
    }

    protected static UserTable _current;

    public static UserTable Current() {
        if (_current == null) {
            Initial();
        }
        return _current;
    }

    private static void Initial() {
        _current = new UserTable();

        _current.Add(C_userName, new ColumnInfo(C_userName, "userName", false, "String"));
        _current.Add(C_StoreID, new ColumnInfo(C_StoreID, "Store_ID", false, "String"));
        _current.Add(C_adminUserName, new ColumnInfo(C_adminUserName, "adminUserName", false, "String"));
        _current.Add(C_StoreUserId, new ColumnInfo(C_StoreUserId, "StoreUserId", false, "String"));
        _current.Add(C_EmployeeId, new ColumnInfo(C_EmployeeId, "EmployeeId", false, "String"));
        _current.Add(C_DeviceId, new ColumnInfo(C_DeviceId, "DeviceId", false, "String"));
        _current.Add(C_ClientID, new ColumnInfo(C_ClientID, "ClientID", false, "String"));
        _current.Add(C_URL, new ColumnInfo(C_URL, "url", false, "String"));


        _current.Add(C_UserId, new ColumnInfo(C_UserId, "UserId", true, "String"));
        _current.Add(C_Fullname, new ColumnInfo(C_Fullname, "Fullname", false, "String"));
        _current.Add(C_StoreName, new ColumnInfo(C_StoreName, "StoreName", false, "String"));

        _current.Add(C_Account, new ColumnInfo(C_Account, "Account", false, "String"));
        _current.Add(C_Password, new ColumnInfo(C_Password, "Password", false, "String"));
        _current.Add(C_UserPicture, new ColumnInfo(C_UserPicture, "UserPicture", false, "String"));
    }

    //
    public ColumnInfo userName() {
        return GetColumnInfoByName(C_userName);
    }

    //
    public ColumnInfo adminUserName() {
        return GetColumnInfoByName(C_adminUserName);
    }

    //
    public ColumnInfo StoreID() {
        return GetColumnInfoByName(C_StoreID);
    }

    //
    public ColumnInfo EmployeeId() {
        return GetColumnInfoByName(C_EmployeeId);
    }

    //
    public ColumnInfo ClientID() {
        return GetColumnInfoByName(C_ClientID);
    }

    //
    public ColumnInfo url() {
        return GetColumnInfoByName(C_URL);
    }

    //
    public ColumnInfo DeviceId() {
        return GetColumnInfoByName(C_DeviceId);
    }

    //
    public ColumnInfo StoreUserId() {
        return GetColumnInfoByName(C_StoreUserId);
    }

    public ColumnInfo UserId() {
        return GetColumnInfoByName(C_UserId);
    }

    public ColumnInfo Fullname() {
        return GetColumnInfoByName(C_Fullname);
    }

    public ColumnInfo StoreName() {
        return GetColumnInfoByName(C_StoreName);
    }

    public ColumnInfo Account() {
        return GetColumnInfoByName(C_Account);
    }

    public ColumnInfo Password() {
        return GetColumnInfoByName(C_Password);
    }

    public ColumnInfo UserPicture() {
        return GetColumnInfoByName(C_UserPicture);
    }

}
