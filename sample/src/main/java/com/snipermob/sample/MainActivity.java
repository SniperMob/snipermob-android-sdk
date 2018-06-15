package com.snipermob.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.snipermob.sdk.mobileads.exception.AdError;
import com.snipermob.sdk.mobileads.loader.BannerAdLoader;
import com.snipermob.sdk.mobileads.loader.InterstitialAdLoader;
import com.snipermob.sdk.mobileads.loader.NativeAdLoader;
import com.snipermob.sdk.mobileads.loader.RewardedVideoLoader;
import com.snipermob.sdk.mobileads.loader.VideoAdLoader;
import com.snipermob.sdk.mobileads.loader.impl.BannerAdLoaderImpl;
import com.snipermob.sdk.mobileads.loader.impl.BaseAdLoader;
import com.snipermob.sdk.mobileads.loader.impl.InterstitialAdLoaderImpl;
import com.snipermob.sdk.mobileads.loader.impl.NativeAdLoaderImpl;
import com.snipermob.sdk.mobileads.loader.impl.RewardedVideoLoaderImpl;
import com.snipermob.sdk.mobileads.loader.impl.VideoAdLoaderImpl;
import com.snipermob.sdk.mobileads.model.AdFormatter;
import com.snipermob.sdk.mobileads.model.NativeAd;
import com.snipermob.sdk.mobileads.widget.ad.AdView;
import com.snipermob.sdk.mobileads.widget.ad.VideoContainerView;
import com.snipermob.sdk.mobileads.widget.ad.nativeview.NativeBaseView;
import com.snipermob.sdk.mobileads.widget.ad.nativeview.NativeRenderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    ViewGroup mBannerContainer ;
    ViewGroup mNativeContainer ;
    Spinner mSpinner ;
    Button mBtnShow ;

    int mSelectPosition ;

    BaseAdLoader mCurrentLoader ;

    static List<AdUnit> sAds = new ArrayList<>();
    static {
        sAds.add(new AdUnit("NativeVideo","test_plid_31",AdFormatter.FORMATTER_NATIVE, AdUnit.Style.LARGE));
        sAds.add(new AdUnit("Banner32050","test_plid_10", AdFormatter.FORMATTER_BANNER));
        sAds.add(new AdUnit("Banner320250","test_plid_11", AdFormatter.FORMATTER_BANNER));
        sAds.add(new AdUnit("INTERSTITIAL320480","test_plid_20", AdFormatter.FORMATTER_INTERSTITIAL));
        sAds.add(new AdUnit("Native","test_plid_30",AdFormatter.FORMATTER_NATIVE));
        sAds.add(new AdUnit("Video","test_plid_40", AdFormatter.FORMATTER_VIDEO));
        sAds.add(new AdUnit("Video","000001", AdFormatter.FORMATTER_VIDEO));
    }


    private void setButtonEnable(boolean enable) {
        mBtnShow.setText(enable?"Show":"Loading...");
        mBtnShow.setEnabled(enable);
    }

    private void loadAd(AdUnit adUnit) {
        switch (adUnit.adFormatter) {
            case FORMATTER_BANNER:
                loadBanner(adUnit.pId,mBannerContainer);
                break;
            case FORMATTER_INTERSTITIAL:
                loadInterstitial(adUnit.pId);
                break;
            case FORMATTER_NATIVE:
                loadNative(adUnit.pId,adUnit.style);
                break;
            case FORMATTER_VIDEO:
                loadVideoAd(adUnit.pId,mBannerContainer);
                break;
            case FORMATTER_REWARED_VIDEO:
                loadRewardedVideo(adUnit);
                break;

        }
    }

    private void loadRewardedVideo(AdUnit adUnit) {
        RewardedVideoLoaderImpl rewardedVideoLoader = new RewardedVideoLoaderImpl();
        rewardedVideoLoader.setRewardedVideoListener(new RewardedVideoLoader.RewardedVideoListener() {
            @Override
            public void onRewaredVideoLoaded() {

            }

            @Override
            public void onRewaredVideoLoadError(AdError adError) {

            }

            @Override
            public void onRewardSuccess() {
            }

            @Override
            public void onRewardedVideoClose() {

            }
        });
        rewardedVideoLoader.setUid("123");
        rewardedVideoLoader.setAdUnit(adUnit.pId);
        rewardedVideoLoader.loadAd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mBannerContainer = (ViewGroup) findViewById(R.id.rl_banner_container);
        this.mNativeContainer = (ViewGroup) findViewById(R.id.rl_native_container);
        this.mBtnShow = (Button) findViewById(R.id.btn_show);
        this.mBtnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllAppendView();
                AdUnit adUnit = sAds.get(mSelectPosition);
                loadAd(adUnit);
            }
        });
        this.mSpinner = (Spinner) findViewById(R.id.spinner1);
        this.mSpinner.setAdapter(new AdSpinnerAdapter(this,sAds));
        this.mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectPosition = position ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void removeAllAppendView() {
        if( mCurrentLoader != null ) {
            mCurrentLoader.destroy();
            mCurrentLoader = null ;
        }

        View nativeView = mNativeContainer.getChildAt(0);
        if( nativeView != null && nativeView instanceof NativeBaseView ) {
            ((NativeBaseView) nativeView).destroy();
        }
        mBannerContainer.removeAllViews();
        mNativeContainer.removeAllViews();
    }

    private void loadVideoAd(String adId, final ViewGroup adContainer) {
        //1.创建Loader
        VideoAdLoader videoAdLoader = new VideoAdLoaderImpl();
        //2.设置placementId
        videoAdLoader.setAdUnit(adId);
        //4.设置监听器
        videoAdLoader.setVideoAdListener(new VideoAdLoader.VideoAdListener() {
            @Override
            public void onVideoAdLoaded(VideoContainerView videoView, Map<String,String> extras) {
                setButtonEnable(true);
                //广告加载成功
                adContainer.addView(videoView);
            }

            @Override
            public void onVideoAdError(AdError adError) {
                //广告加载失败
                setButtonEnable(true);
                Log.d("MainActivity","onVideoAdError."+adError.getErrorMessage());
            }

            @Override
            public void onVideoClicked() {
                //广告被点击
            }
        });
        //5.加载广告
        videoAdLoader.loadAd();
        setButtonEnable(false);
    }



    private void loadBanner(String adId, final ViewGroup adContainer) {
        BannerAdLoader bannerAdLoader = new BannerAdLoaderImpl();
        bannerAdLoader.setAdUnit(adId);
        bannerAdLoader.setBannerListener(new BannerAdLoader.BannerAdListener() {
            @Override
            public void onBannerLoaded(AdView bannerView, Map<String,String> extras) {
                adContainer.addView(bannerView);
                setButtonEnable(true);
                String ecpmStr = extras.get("ecpm");
                if( ecpmStr != null ) {
                    double ecpm = Double.parseDouble(ecpmStr);
                    //do something with ecpm
                    Log.d("MainActivity","ECPM is "+ecpm);
                }


            }

            @Override
            public void onBannerLoadError(AdError adError) {
                setButtonEnable(true);
                ToastUtils.showShortToast(MainActivity.this,adError.getErrorMessage());
            }

            @Override
            public void onBannerClicked() {
                ToastUtils.showShortToast(MainActivity.this,"onBannerClicked");
            }
        });

        bannerAdLoader.loadAd();
        setButtonEnable(false);
        mCurrentLoader = (BannerAdLoaderImpl)bannerAdLoader ;
    }

    private void loadInterstitial(String id) {
        //1.创建Loader
        final InterstitialAdLoader interstitialAdLoader = new InterstitialAdLoaderImpl();
        //2.设置placementId
        interstitialAdLoader.setAdUnit(id);
        //4.设置监听器
        interstitialAdLoader.setInterstitialListener(new InterstitialAdLoader.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(Map<String,String> extras) {
                //加载成功
                interstitialAdLoader.show();
                setButtonEnable(true);
            }

            @Override
            public void onInterstitialLoadError(AdError adError) {
                //广告加载失败
                ToastUtils.showShortToast(MainActivity.this,"onInterstitialLoadError"+adError.getErrorMessage());
                setButtonEnable(true);
            }

            @Override
            public void onInterstitialClosed() {
                //广告被关闭
                ToastUtils.showShortToast(MainActivity.this,"onInterstitialClosed");
            }

            @Override
            public void onInterstitialClicked() {
                //广告被点击
                ToastUtils.showShortToast(MainActivity.this,"onInterstitialClicked");
            }
        });
        //5.加载广告
        interstitialAdLoader.loadAd();
        setButtonEnable(false);
        mCurrentLoader = (InterstitialAdLoaderImpl)interstitialAdLoader ;
    }

    private void loadNative(String pId, final AdUnit.Style style) {
        //1.创建Loader
        NativeAdLoader nativeAdLoader = new NativeAdLoaderImpl();
        //2.设置广告位Id
        nativeAdLoader.setAdUnit(pId);
        //4.设置监听器
        nativeAdLoader.setNativeAdListener(new NativeAdLoader.NativeAdListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd,Map<String,String> extras) {
                //广告加载成功S
                View view = null ;
                if( style == AdUnit.Style.SMALL ) {
                    view = NativeRenderUtils.render(MainActivity.this,nativeAd, NativeRenderUtils.NativeRenderStyle.RENDER_STYLE_NATIVE_SMALL) ;
                } else if( style == AdUnit.Style.MEDIUM ) {
                    view = NativeRenderUtils.render(MainActivity.this,nativeAd, NativeRenderUtils.NativeRenderStyle.RENDER_STYLE_NATIVE_MEDIUM) ;
                } else {
                    view = NativeRenderUtils.render(MainActivity.this,nativeAd, NativeRenderUtils.NativeRenderStyle.RENDER_STYLE_NATIVE_LARGE) ;
                }
                nativeAd.registerView(view);
                mNativeContainer.addView(view);
                setButtonEnable(true);
            }

            @Override
            public void onNativeAdError(AdError adError) {
                //广告加载失败
                setButtonEnable(true);
            }
        });
        //5.加载广告
        nativeAdLoader.loadAd();
        setButtonEnable(false);
        mCurrentLoader = (NativeAdLoaderImpl)nativeAdLoader ;
    }

}
