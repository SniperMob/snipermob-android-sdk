package com.snipermob.sample;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jerome on 2018/2/26.
 */

public class ToastUtils {
    public static void showShortToast(Context ctx, String content) {
        Toast.makeText(ctx,content,Toast.LENGTH_SHORT).show();
    }

    public static void showLongTaost(Context ctx,String content) {
        Toast.makeText(ctx,content,Toast.LENGTH_LONG).show();
    }
}
