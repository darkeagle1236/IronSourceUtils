# IronSourceUtils
[![](https://jitpack.io/v/darkeagle1236/IronSourceUtils.svg)](https://jitpack.io/#darkeagle1236/IronSourceUtils)
An useful, quick implementation of IronSource Mediation SDK


<!-- GETTING STARTED -->

### Prerequisites

Add this to your project-level build.gradle
  ```sh
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  ```
Add this to your app-level build.gradle
  ```sh
  dependencies {
	        implementation 'com.github.darkeagle1236:IronSourceUtils:Tag'
	}
  ```
### Usage

#### Init
Add this to onCreate of your first activity
 ```sh
        IronSourceUtil.initIronSource(this, "app-key",true)
 ```
Register lifecyclecallback to auto-reload after failed to load interstitial

 ```sh
            this.getApplication().registerActivityLifecycleCallbacks(IronSourceLifeCycleHelper.INSTANCE);
 ```
 #### Mediation Adapter
 
 If you're going to use IronSource Mediation with other networks, you have to implement the corresponding network adapter
 Here's all network adapter you need:
 https://developers.is.com/ironsource-mobile/android/mediation-networks-android/#step-1
 Example : you want to add Meta Audience Network, Admob and UnityAds : 
 
 ```sh
         	repositories {
   			mavenCentral()
   			maven {url 'https://android-sdk.is.com/'} 
		}
		dependencies {
		// Add AdMob and Ad Manager Network
		implementation 'com.google.android.gms:play-services-ads:20.6.0'
		implementation 'com.ironsource.adapters:admobadapter:4.3.27'
		// Add Facebook Network
		implementation 'com.ironsource.adapters:facebookadapter:4.3.34'
		implementation 'com.facebook.android:audience-network-sdk:6.8.0'
		// Add UnityAds Network
		implementation 'com.ironsource.adapters:unityadsadapter:4.3.20'
		implementation 'com.unity3d.ads:unity-ads:4.0.1'
		}
 ```
#### Load interstitial
 ```sh
         IronSourceUtil.loadInterstitials(object : InterstititialCallback {
                override fun onInterstitialReady() {
                    //Interstitial ready, can be able to call IronSourceUtils.showInterstitial()
                }

                override fun onInterstitialClosed() {
                    //Handle user closing the interstitial ads
                }

                override fun onInterstitialLoadFail() {
                    //Interstitial loading failed
                }
            })
 ```
 
#### Load interstitial with timeout (use for SplashActivity)
Load intersitital, but will call callback.onInterstitialLoadFail() when timeout is reached
 ```sh		
         IronSourceUtil.loadInterstitials(activity,timeoutInMillis,interstitialCallback)
 ```
 
#### Show interstitial with dialog
If ready, showing interstitial with a fake loading dialog
 ```sh
         IronSourceUtil.showInterstitialsWithDialog(activity,placementId,dialogShowTime,interstitialCallback)
 ```
#### Load interstitial
Load intersitital to show later
 ```sh		
         IronSourceUtil.loadInterstitials(placementId)
 ```
#### Show interstitial
Only available after intersitital loaded successfully
 ```sh		
         IronSourceUtil.showInterstitials(placementId)
 ```
 #### Load a banner(place in onResume)
 
 ```sh
 IronSourceUtil.showBanner(activity,viewgroup,"ad-placement-id")
  ```
