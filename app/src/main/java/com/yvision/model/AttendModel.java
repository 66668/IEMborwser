package com.yvision.model;

import java.io.Serializable;

/**
 * Created by sjy on 2017/4/5.
 */

public class AttendModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String Message ;
    private String Type;
    private String Id;
    private String Pic;
    private String CapTime ;

    public String getPic() {
        return Pic;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public String getCapTime() {
        return CapTime;
    }

    public void setCapTime(String capTime) {
        CapTime = capTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMessage () {
        return Message ;
    }

    public void setMessage (String Message ) {
        this.Message  = Message ;
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
