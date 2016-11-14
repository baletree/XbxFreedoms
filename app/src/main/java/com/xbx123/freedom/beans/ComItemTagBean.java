package com.xbx123.freedom.beans;

import java.io.Serializable;

/**
 * Created by EricYuan on 2016/6/30.
 */
public class ComItemTagBean implements Serializable {
    private String comTagId;
    private String comTagName;

    public String getComTagId() {
        return comTagId;
    }

    public void setComTagId(String comTagId) {
        this.comTagId = comTagId;
    }

    public String getComTagName() {
        return comTagName;
    }

    public void setComTagName(String comTagName) {
        this.comTagName = comTagName;
    }
}
