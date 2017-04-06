package com.yvision.model;

import java.io.Serializable;

/**
 * 考勤信息 实体类
 * Created by JackSong on 2016/9/12.
 */
public class AttendDetailModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String AttendID;//考勤编号
    private String AttendType;// 考勤类型--1:local/ 2:map/3:wifi
    private String CapImagePath;//  采集图像相对路径(大图)
    private String CapTime;// 采集时间
    private String EmployeeCardNo;
    private String EmployeeGender;// 员工性别
    private String EmployeeID;//员工ID
    private String EmployeeName;// 员工名称
    private String Latitude;// 纬度
    private String Longitude;// 经度
    private String Score;//对比分数


    private String SmallCapImagePath;//  采集图像相对路径(小图)
    private String CompanyID;//公司ID
    private String ImageID;//注册图像ID
    private String CapImageID;//采集图像ID
    private String WrokId;//员工号
    private String DepartmentID;//员工部门ID
    private String ImagePath;//注册图像相对路径(大图)
    private String SmallImagePath;//注册图像相对路径(小图)
    private String DepartmentName;// 员工部门名称
    private String CompanyName;// 员工公司名称

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAttendID() {
        return AttendID;
    }

    public String getAttendType() {
        return AttendType;
    }

    public String getCapImagePath() {
        return CapImagePath;
    }

    public String getScore() {
        return Score;
    }

    public String getLongitude() {
        return Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public String getEmployeeGender() {
        return EmployeeGender;
    }

    public String getCapTime() {
        return CapTime;
    }

    public String getEmployeeCardNo() {
        return EmployeeCardNo;
    }

    public void setAttendID(String attendID) {
        AttendID = attendID;
    }

    public void setAttendType(String attendType) {
        AttendType = attendType;
    }

    public void setCapImagePath(String capImagePath) {
        CapImagePath = capImagePath;
    }

    public void setCapTime(String capTime) {
        CapTime = capTime;
    }

    public void setEmployeeCardNo(String employeeCardNo) {
        EmployeeCardNo = employeeCardNo;
    }

    public void setEmployeeGender(String employeeGender) {
        EmployeeGender = employeeGender;
    }

    public void setEmployeeID(String employeeID) {
        EmployeeID = employeeID;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setScore(String score) {

        Score = score;
    }

    public String getSmallCapImagePath() {
        return SmallCapImagePath;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public String getImageID() {
        return ImageID;
    }

    public String getCapImageID() {
        return CapImageID;
    }

    public String getWrokId() {
        return WrokId;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public String getSmallImagePath() {
        return SmallImagePath;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setSmallCapImagePath(String smallCapImagePath) {
        SmallCapImagePath = smallCapImagePath;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public void setCapImageID(String capImageID) {
        CapImageID = capImageID;
    }

    public void setWrokId(String wrokId) {
        WrokId = wrokId;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public void setSmallImagePath(String smallImagePath) {
        SmallImagePath = smallImagePath;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}
