package com.snipermob.sample;

import android.app.Application;
import android.widget.ImageView;

import com.snipermob.sdk.mobileads.SniperMobSDK;
import com.squareup.picasso.Picasso;

/**
 * Created by Jerome on 2018/2/26.
 */

public class SampleApplication extends Application {

    private static final String APPID = "appid1";
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 初始化CootekSDK
         */
        SniperMobSDK.init(this, APPID, new SniperMobSDK.IImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(SampleApplication.this).load(url).into(imageView);
            }
        });
        SniperMobSDK.setDebugMode(true);
    }


}
