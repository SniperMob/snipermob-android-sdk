package com.snipermob.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

/**
 * Created by Jerome on 2018/2/26.
 */

public class SplashActivity extends Activity{

    public static final int REQUEST_CODE = 100 ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkPermission();
    }

    /**
     * 请求相关权限
     */
    private void checkPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.INTERNET
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if( report.areAllPermissionsGranted() ) {
                            finish();
                            start2Main();
                        } else {
                            ToastUtils.showLongTaost(SplashActivity.this,getString(R.string.tips_permission));
                            openDetail();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
    }

    private void start2Main() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    /**
     * 打开应用详情页，申请授权
     */
    private void openDetail() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, REQUEST_CODE);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( REQUEST_CODE == requestCode ) {
            checkPermission();
        }
    }
}
