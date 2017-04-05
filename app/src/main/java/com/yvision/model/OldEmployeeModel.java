package com.yvision.model;

import java.io.Serializable;

/**
 * Created by sjy on 2016/11/17.
 */

public class OldEmployeeModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String EmployeeName;
    private String WrokId;
    private String EmployeeId;
    private String StoreName;
    private String Gender;
    private String DeptName;
    private String Pic;

    private String IsAttend;
    private String IsVisitor;
    private String IsVip;
    private String IsDoorAccess;

    public String getIsAttend() {
        return IsAttend;
    }

    public void setIsAttend(String isAttend) {
        IsAttend = isAttend;
    }

    public String getIsVisitor() {
        return IsVisitor;
    }

    public void setIsVisitor(String isVisitor) {
        IsVisitor = isVisitor;
    }

    public String getIsVip() {
        return IsVip;
    }

    public void setIsVip(String isVip) {
        IsVip = isVip;
    }

    public String getIsDoorAccess() {
        return IsDoorAccess;
    }

    public void setIsDoorAccess(String isDoorAccess) {
        IsDoorAccess = isDoorAccess;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getWrokId() {
        return WrokId;
    }

    public void setWrokId(String wrokId) {
        WrokId = wrokId;
    }

    public String getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(String employeeId) {
        EmployeeId = employeeId;
    }
}
