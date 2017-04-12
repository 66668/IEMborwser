package com.yvision.model;

import java.io.Serializable;

/**
 * Created by sjy on 2017/3/10.
 */

public class DoorAccessModel implements Serializable {
    private static final long serialVersionUID = 1L;

    public String AccessRecordID;
    /// 员工ID
    public String EmployeeID;
    /// 公司ID
    public String CompanyID;
    /// 注册图像ID int类型
    public String ImageID;
    /// 采集图像ID int类型
    public String CapImageID;
    /// 员工名称
    public String EmployeeName;
    /// 员工性别 int类型 男女
    public String EmployeeGender;
    /// 员工号
    public String WrokId;

    /// 注册图像相对路径(大图)
    public String ImagePath;
    /// 注册图像相对路径(小图)
    public String SmallImagePath;
    ///  采集图像相对路径(大图)
    public String CapImagePath;
    ///  采集图像相对路径(小图)
    public String SmallCapImagePath;
    /// 采集时间
    public String CapTime;
    /// 对比分数
    public String Score;
    /// 员工公司名称
    public String CompanyName;

    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccessRecordID() {
        return AccessRecordID;
    }

    public void setAccessRecordID(String accessRecordID) {
        AccessRecordID = accessRecordID;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public String getCapImageID() {
        return CapImageID;
    }

    public void setCapImageID(String capImageID) {
        CapImageID = capImageID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getEmployeeGender() {
        return EmployeeGender;
    }

    public void setEmployeeGender(String employeeGender) {
        EmployeeGender = employeeGender;
    }

    public String getWrokId() {
        return WrokId;
    }

    public void setWrokId(String wrokId) {
        WrokId = wrokId;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getSmallImagePath() {
        return SmallImagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        SmallImagePath = smallImagePath;
    }

    public String getCapImagePath() {
        return CapImagePath;
    }

    public void setCapImagePath(String capImagePath) {
        CapImagePath = capImagePath;
    }

    public String getSmallCapImagePath() {
        return SmallCapImagePath;
    }

    public void setSmallCapImagePath(String smallCapImagePath) {
        SmallCapImagePath = smallCapImagePath;
    }

    public String getCapTime() {
        return CapTime;
    }

    public void setCapTime(String capTime) {
        CapTime = capTime;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
