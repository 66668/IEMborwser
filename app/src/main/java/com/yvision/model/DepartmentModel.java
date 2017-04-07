package com.yvision.model;

import java.io.Serializable;


/**
 * 部门model
 * Created by sjy on 2016/11/13.
 */

public class DepartmentModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String DeptID;
    private String CompanyID;
    private String DeptName;
    private String ParentDeptID;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDeptID() {
        return DeptID;
    }

    public void setDeptID(String deptID) {
        DeptID = deptID;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getParentDeptID() {
        return ParentDeptID;
    }

    public void setParentDeptID(String parentDeptID) {
        ParentDeptID = parentDeptID;
    }
}
