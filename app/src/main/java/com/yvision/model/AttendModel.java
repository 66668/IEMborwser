package com.yvision.model;

import java.io.Serializable;

/**
 * Created by sjy on 2017/4/5.
 */

public class AttendModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String message;
    private String Type;
    private String Id;
    private String Pic;
    private String DateTime;

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
