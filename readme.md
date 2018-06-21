# SniperMob Android SDK

Maximum your apps revenue stream, sign up for an account at [snipermob.com](https://console.snipermob.com).

# Integration

## Import SDK

* Add the following to your `build.gradle`.

```java
android {
    ...
}
repositories {
    flatDir {
        dirs 'libs'
    }
}
dependencies {
	...
    compile(name: 'SniperMob_vxxx', ext: 'aar')
    //add play-services-ads lib
    compile 'com.google.android.gms:play-services-ads:x.x.x'
}
```

* You also need to add `play-services-ads` to the project.

## Initialization

```java
SniperMobSDK.init(Context, "AppID", new SniperMobSDK.IImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(DemoApplication.this).load(url).into(imageView);
            }
});
```

* Required parameters:
Context object
AppID (Apply for your APP ID at [snipermob.com](https://console.snipermob.com))
native IImageLoader proxy
* SniperMob SDK has built a simple image loading logic. It is recommended to implement the proxy and use the application's own image loading engine.  
* It is recommended to call in the Application's `onCreate` method.

## Add Banner Ad

```java
//1.Construct bannerAdLoader
BannerAdLoader bannerAdLoader = new BannerAdLoaderImpl();
//2.Set up placement id (Apply for your Placement ID at https://console.snipermob.com)
bannerAdLoader.setAdUnit("PLACEMENTID");
//3.Set up BannerAdListener
bannerAdLoader.setBannerListener(new BannerAdLoader.BannerAdListener() {
            @Override
            public void onBannerLoaded(AdView bannerView, Map<String,String> extras) {
                //return ad
                //get the eCPM price of this ad
                String ecpmStr = extras.get("ecpm");
                if( ecpmStr != null ) {
                    double ecpm = Double.parseDouble(ecpmStr);
                }
            }

            @Override
            public void onBannerLoadError(AdError adError) {
                //return error
            }

            @Override
            public void onBannerClicked() {
				//ad is clicked
            }
        });
//4.Load ad
bannerAdLoader.loadAd();
```
* Set up placement id (apply for your placement id at [snipermob.com](https://console.snipermob.com)) and listener before calling  `loadAd()`.
* Banner view loaded successfully will be returned in method `onBannerLoaded()`, you can add the view to the layout at this time.
* `onBannerLoadError()` will be executed when the banner view fails to load, you can get the corresponding error via `AdError.getErrorMessage()`.
* `onBannerClicked()` will be executed when the ad is clicked.

***Please note that the ad’s expiration time is 15 minutes.***

## Add Interstitial Ad

```java
//1.Construct InterstitialLoader
final InterstitialAdLoader interstitialAdLoader = new InterstitialAdLoaderImpl();
//2.Set up placement id (Apply for your Placement ID at https://console.snipermob.com)
interstitialAdLoader.setAdUnit("PLACEMENTID");
//3.Set up InterstitialAdListener
interstitialAdLoader.setInterstitialListener(new InterstitialAdLoader.InterstitialAdListener() {
            @Override
            public void onInterstitialLoaded(Map<String,String> extras) {
                //ad loaded successfully, use show() method to display
                interstitialAdLoader.show();
            }

            @Override
            public void onInterstitialLoadError(AdError adError) {
                //faild to load ad
            }

            @Override
            public void onInterstitialClosed() {
                //ad is closed
            }

            @Override
            public void onInterstitialClicked() {
                //ad is clicked
            }
        });
//4.Load ad
interstitialAdLoader.loadAd();
```

* Set placement id (apply for your placement id at [snipermob.com](https://console.snipermob.com)) and listener before calling method `loadAd()`.
* When the ad is loaded successfully, it will callback in  listener `onInterstitialLoaded()`,  you can execute method `show()` at this point.

***Please note that the ad’s expiration time is 15 minutes.***

## Add Native Ad

Native ad contains the following elements：
* icon
* title
* description
* rating
* call to action button
* mediaview

***Please note: mediaview may be a picture or video, etc. SDK is required to help complete the rendering.***

```java
//1.Construct nativeAdLoader
 NativeAdLoader nativeAdLoader = new NativeAdLoaderImpl();
//2.Set up placement id (Apply for your Placement ID at https://console.snipermob.com)
nativeAdLoader.setAdUnit(PLACEMENTID);
//3.Set up NativeAdListener
nativeAdLoader.setNativeAdListener(new NativeAdLoader.NativeAdListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd,Map<String,String> extras) {
                //ad loaded successfully 
            }

            @Override
            public void onNativeAdError(AdError adError) {
                //faild to load ad
            }
        });
//4.Load ad
nativeAdLoader.loadAd();
```

***Please note that the ad’s expiration time is 15 minutes.***

#### Construct View

You have two ways to use native ad after it load successfully: 
1. Use `NativeAd` data to assemble your view.
2. Use the configuration provided by SDK to load the view.

* Construct view using the SDK built-in method.

```java
public static enum NativeRenderStyle {
        //large native view
        RENDER_STYLE_NATIVE_LARGE,
        //medium native view
        RENDER_STYLE_NATIVE_MEDIUM,
        //small native view
        RENDER_STYLE_NATIVE_SMALL;
 }
```

```java
View view = NativeRenderUtils.render(MainActivity.this,nativeAd, NativeRenderUtils.NativeRenderStyle.RENDER_STYLE_BANNER_MEDIUM) ;
```

* Custom native view

If you want to customize your native view with `NativeAd` data, in addition to `mediaview` other data can be obtained through the NativeAd related fields.

  
Construct `mediaview` with the following code:
  ```java
  nativeAd.bindMediaView(mediaview);
  ```

###  Set Up Interaction

If you are using `NativeAd` data to assemble your view, you need to bind `View` with native ad to complete the interaction. 

The specific binding method is:
> NativeAd.java

```java
public void registerView(View view){...}
```

## Add Video Ad

```
//1.Construct videoAdLoader
VideoAdLoader videoAdLoader = new VideoAdLoaderImpl();
//2.Set up placement id (Apply for your Placement ID at https://console.snipermob.com)
videoAdLoader.setAdUnit(PLACEMENTID);
//3.Set up VideoAdListener
videoAdLoader.setVideoAdListener(new VideoAdLoader.VideoAdListener() {
            @Override
            public void onVideoAdLoaded(VideoAdView videoView,Map<String,String> extras) {
                //ad loaded successfully 
                adContainer.addView(videoView);
            }

            @Override
            public void onVideoAdError(AdError adError) {
                //faild to load ad
            }

            @Override
            public void onVideoClicked() {
                //ad is clicked
            }
        });
//4.Load ad
videoAdLoader.loadAd();
```

***Please note that the ad’s expiration time is 15 minutes.***

## Interface Description

### Error Code

| No. | Error                                              | Description                                                   |
| ---- | ------------------------------------------------- | ------------------------------------------------------ |
| 1    | ERROR_SERVER_NOAD_FILL                            | No ad faild                                             |
| 2    | ERROR_SERVER_UNKNOW_ERROR                         | Unknown error                                               |
| 3    | ERROR_SERVER_LAKE_REQUEST_ARGUMENT                | Missing required parameters                                               |
| 4    | ERROR_SERVER_APPID_NOT_VALID                      | App id is not available            |
| 5    | ERROR_SERVER_APPID_OS_ERROR                       | App id does not match |                                      |
| 6    | ERROR_SERVER_PLACEMENTID_INVALID                  | Placement id is not available                               |
| 7    | ERROR_SERVER_PLACEMENTID_OS_ERROR                 | Placement id does not match                                  |
| 8    | ERROR_SERVER_PLACEMENTID_ARGUMENT_NOT_EQUAL_PENDA | App id in the ad request does not match with the App id which associated with placement                     |
| 9    | ERROR_SERVER_PLACEMENTID_INTERNAL_ADSOURCE        | Server error, please contact us                             |
| 10   | ERROR_SERVER_SDKVERSION_LOWER                     | Please update the SDK version                                 |
| -1   | ERROR_NETWORK_REQUESTPOOL_FULL                    | Too many requests sent at one time, please update your code logic                 |
| -2   | ERROR_NETWORK_ERROR                               | Network error                                               |
| 1000 | ERROR_NETWORK_TIMEOUT                             | Request timeout                                               |
| 1001 | ERROR_NETWORK_SEVER_RETURN_NOT_OK                 | Server did not return correctly                                       |
| 1100 | ERROR_PARSE_ERROR                                 | Data parse error                                           |
| 1101 | ERROR_PARSE_FORMAT_UNCONFORMITY                   | The requested placement ad format does not match the ad format created |
| 1102 | ERROR_PARSE_NOT_SUPPORT_MEDIATYPE                 | Unsupported ad format                      |
| 1110 | ERROR_PARSE_HTMLAD_ERROR                          | Parsing html error                                      |
| 1120 | ERROR_PARSE_RICHMEDIA_ERROR                       | Parsing rich media error                                  |
| 1130 | ERROR_PARSE_NATIVEAD_ERROR                        | Parsing native ad error                                     |
| 1140 | ERROR_PARSE_VIDEOAD_ERROR                         | Parsing video ad error                                       |
| 1141 | ERROR_PARSE_VIDEOAD_REQUEST_WRAPPER_ERROR         | Parsing video wrapper error                               |
| 1142 | ERROR_PARSE_VIDEO_REQUEST_JUMP_ERROR              | Too many jump in video wrapper                       |
| 1200 | ERROR_RENDER_CREATE_ERROR                         | Creating UI error                                            |
| 1201 | ERROR_RENDER_UI_ERROR                             | Rendering UI error                                             |
| 1210 | ERROR_RENDER_HTMLAD_ERROR                         | Rendering html error                                       |
| 1220 | ERROR_RENDER_RICHMEDIA_ERROR                      | Rendering rich media error                                  |
| 1230 | ERROR_RENDER_NATIVE_UI_ERROR                      | Rendering native ad error                                     |
| 1240 | ERROR_RENDER_VIDEO_NATIVE_UI_ERROR                | Creating native player error                                   |
| 1241 | ERROR_RENDER_VIDEO_VPAID_UI_ERROR                 | Creating VPAID player error                                    |
| 1242 | ERROR_RENDER_VIDEO_H5_UI_ERROR                    | Creating H5 player error                                       |
| 1243 | ERROR_RENDER_VIDEO_UI_ERROR_NOTSUPPORT_PLAYER     | Construct video player error, unsupported media type                       |
| 1244 | ERROR_RENDER_VIDEO_UI_LOAD_VIDEO_CONTENR_ERROR    | Loading video content error                                      |
| 1250 | ERROR_RENDER_UI_AFTRE_DESTROY                     | Loading UI after destroy                              |

###  Permission

SniperMob SDK need the following permission:

| Permission                   | Description                 | Required |
| ---------------------- | -------------------- | -------- |
| INTERNET               | Network access           | Yes     |
| WRITE_EXTERNAL_STORAGE | External storage | Yes     |
| ACCESS_COARSE_LOCATION | Get location information         | Recommended     |
| ACCESS_FINE_LOCATION   | Get location information         | Recommended     |

### Resource release

After the ad loader is used, you need to execute the destroy method to release the resource. The destroy method needs to be called in the main thread.

### Developer Mode

```java
//The SDK works by default in non-debug mode. After the debug mode is enabled, the test ad will be returned.
SniperMobSdk.setDebugMode(true);
```

Test ad will be returned under test mode.

| Placement ID     | App ID  | Ad Format     | Ad Size | Media Type       |
| ------------ | ------ | ------------ | -------- | ------------------------ |
| test_plid_10 | appid1 | banner       | 32050    | richmedia \|  image \| native |
| test_plid_11 | appid1 | banner       | 320250   | richmedia \| video \| native |
| test_plid_20 | appid1 | interstitial | 320480   | richmedia \| video         |
| test_plid_30 | appid1 | native       | customized   | native                   |
| test_plid_40 | appid1 | video        | 320480   | video                    |

***Please note: turn off debug mode before going live.***
```java
SniperMobSdk.setDebugMode(false);
```

### Additional Parameters

When the SDK loads the ad successfully, some extra parameters also returned, the parameters are stored as key-values in the Map structure.

* ecpm

  ```
  public void onBannerLoaded(AdView bannerView, Map<String,String> extras) {
  				//Get the ecpm price of this ad
                  String ecpmStr = extras.get("ecpm");
                  if( ecpmStr != null ) {
                      double ecpm = Double.parseDouble(ecpmStr);
                      //do something with ecpm
                      Log.d("MainActivity","ECPM is "+ecpm);
                  }
  }
  ```
  
## Requirements
* Android 4.0.3 (API Version 15) and up

## Code obfuscation

Please add the following code to your `proguard`.
```
-keep class com.snipermob.**{*;}
-keepclassmembers class com.snipermob.** {*;}
```

## Release Notes

* VERSION 2.0.5
  * Supported Banner, Interstitial, Native and Video Ads.
  * Supported MRAID 2.0 in Banner and Interstitial Ads.
  * Supported VAST 3.0 & VPAID 2.0 in Video Ads.
