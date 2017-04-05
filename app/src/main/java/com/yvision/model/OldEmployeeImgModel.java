package com.yvision.model;

import java.io.Serializable;

/**
 * 老员工图片详情
 * Created by sjy on 2016/11/17.
 */

public class OldEmployeeImgModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String imageID;
    private String companyID;
    private String imagePath;
    private String groupID;
    private String groupName;// 人脸库名称
    private String groupType;// 人脸库类型

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
