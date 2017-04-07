package com.yvision.model;

import java.io.Serializable;

/**
 * 人像库model
 * Created by sjy on 2016/11/13.
 */

public class GroupModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String groupID;
    private String groupName;
    private String groupThreshold;
    private String groupDesc;
    private String groupType;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getGroupThreshold() {
        return groupThreshold;
    }

    public void setGroupThreshold(String groupThreshold) {
        this.groupThreshold = groupThreshold;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

}
