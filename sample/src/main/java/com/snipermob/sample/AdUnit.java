package com.snipermob.sample;


import com.snipermob.sdk.mobileads.model.AdFormatter;

/**
 * Created by Jerome on 2018/2/26.
 */

public class AdUnit {

    public enum Style {
        SMALL,
        MEDIUM,
        LARGE
    }
    public String title ;
    public String pId ;
    public AdFormatter adFormatter ;
    public Style style ;
    public AdUnit(String title,String pId,AdFormatter adFormatter) {
        this.title = title ;
        this.pId = pId ;
        this.adFormatter = adFormatter ;
        style = Style.MEDIUM ;
    }

    public AdUnit(String title, String pId, AdFormatter adFormatter, Style style) {
        this.title = title ;
        this.pId = pId ;
        this.adFormatter = adFormatter ;
        this.style = style ;
    }
}
