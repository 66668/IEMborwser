package com.yvision.db.entity;


public class UserEntity extends EntityBase {
    public UserTable TableSchema() {
        return (UserTable) _tableSchema;
    }

    public UserEntity() {
        _tableSchema = UserTable.Current();
    }

    //
    public String getUserName() {
        return (String) GetData(UserTable.C_userName);
    }

    public void setUserName(String value) {
        SetData(UserTable.C_userName, value);
    }


    //公司编号
    public String getAdminUserName() {
        return (String) GetData(UserTable.C_adminUserName);
    }
    public void setAdminUserName(String value) {
        SetData(UserTable.C_adminUserName, value);
    }

    //
    public String getClientID() {
        return (String) GetData(UserTable.C_ClientID);
    }

    public void setClientID(String value) {
        SetData(UserTable.C_ClientID, value);
    }

    //
    public String getURL() {
        return (String) GetData(UserTable.C_URL);
    }

    public void setURL(String value) {
        SetData(UserTable.C_URL, value);
    }

    //设备id
    public String getDeviceId() {
        return (String) GetData(UserTable.C_DeviceId);
    }

    public void setDeviceId(String value) {
        SetData(UserTable.C_DeviceId, value);
    }

    //用户id
    public String getStoreUserId() {
        return (String) GetData(UserTable.C_StoreUserId);
    }

    public void setStoreUserId(String value) {
        SetData(UserTable.C_StoreUserId, value);
    }

    //员工id
    public String getEmployeeId() {
        return (String) GetData(UserTable.C_EmployeeId);
    }

    public void setEmployeeId(String value) {
        SetData(UserTable.C_EmployeeId, value);
    }


    //公司编号 id
    //公司编号 id
    public String getStoreID() {
        return (String) GetData(UserTable.C_StoreID);
    }

    public void setStoreID(String value) {
        SetData(UserTable.C_StoreID, value);
    }

    public String getUserId() {
        return (String) GetData(UserTable.C_UserId);
    }

    public void setUserId(String value) {
        SetData(UserTable.C_UserId, value);
    }

    public String getFullname() {
        return (String) GetData(UserTable.C_Fullname);
    }

    public void setFullname(String value) {
        SetData(UserTable.C_Fullname, value);
    }

    public String getStoreName() {
        return (String) GetData(UserTable.C_StoreName);
    }

    public void setStoreName(String value) {
        SetData(UserTable.C_StoreName, value);
    }


    public String getAccount() {
        return (String) GetData(UserTable.C_Account);
    }

    public void setAccount(String value) {
        SetData(UserTable.C_Account, value);
    }

    public String getPassword() {
        return (String) GetData(UserTable.C_Password);
    }

    public void setPassword(String value) {
        SetData(UserTable.C_Password, value);
    }

    public String getUserPicture() {
        return (String) GetData(UserTable.C_UserPicture);
    }

    public void setUserPicture(String value) {
        SetData(UserTable.C_UserPicture, value);
    }

}
