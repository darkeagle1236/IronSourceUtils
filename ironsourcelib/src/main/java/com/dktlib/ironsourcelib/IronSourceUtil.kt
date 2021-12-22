package com.dktlib.ironsourcelib

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.IronSourceBannerLayout
import com.ironsource.mediationsdk.integration.IntegrationHelper
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.BannerListener
import com.ironsource.mediationsdk.sdk.InterstitialListener
import com.vapp.admoblibrary.utils.SweetAlert.SweetAlertDialog
import kotlinx.coroutines.*
import java.lang.Exception


object IronSourceUtil : LifecycleObserver {
    var enableAds = true
    var isInterstitialAdShowing = false
    lateinit var banner: IronSourceBannerLayout
    var lastTimeInterstitial: Long = 0L
    var isLoadInterstitialFailed = false
    fun initIronSource(activity: Activity, appKey: String, enableAds: Boolean) {
        this.enableAds = enableAds
        IronSource.init(
            activity,
            appKey,
            IronSource.AD_UNIT.INTERSTITIAL,
            IronSource.AD_UNIT.BANNER
        )
    }

    fun validateIntegration(activity: Activity) {
        IntegrationHelper.validateIntegration(activity);
    }

    val TAG: String = "IronSourceUtil"
    fun showInterstitialAdsWithCallback(
        activity: AppCompatActivity,
        adPlacementId: String,
        showLoadingDialog: Boolean,
        callback: AdCallback
    ) {
        IronSource.removeInterstitialListener()
        if (!enableAds) {
            callback.onAdFail()
            return
        }
        var dialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        dialog.getProgressHelper().barColor = Color.parseColor("#A5DC86")
        dialog.setTitleText("Loading ads. Please wait...")
        dialog.setCancelable(false)
        activity.lifecycle.addObserver(DialogHelperActivityLifeCycle(dialog))
        val mInterstitialListener = object : InterstitialListener {
            override fun onInterstitialAdReady() {
                if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && dialog.isShowing()) {
                    dialog.dismiss()
                }
                Log.d(TAG, activity.lifecycle.currentState.toString())
                if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    Log.d(TAG, "onInterstitialAdReady")
                    IronSource.showInterstitial(adPlacementId)
                }
                isLoadInterstitialFailed = false
            }

            override fun onInterstitialAdLoadFailed(p0: IronSourceError) {
                if (!activity.isFinishing() && dialog.isShowing()) {
                    dialog.dismiss()
                }
                isLoadInterstitialFailed = true
                callback.onAdFail()
                Log.d(TAG, "onInterstitialAdLoadFailed " + p0.errorMessage)
            }

            override fun onInterstitialAdOpened() {
                Log.d(TAG, "onInterstitialAdOpened")
            }

            override fun onInterstitialAdClosed() {
                callback.onAdClosed()
                isInterstitialAdShowing = false
                loadInterstitials()
                Log.d(TAG, "onInterstitialAdClosed")
            }

            override fun onInterstitialAdShowSucceeded() {
                Log.d(TAG, "onInterstitialAdShowSucceeded")
                lastTimeInterstitial = System.currentTimeMillis()
                isInterstitialAdShowing = true
            }

            override fun onInterstitialAdShowFailed(p0: IronSourceError) {
                if (!activity.isFinishing() && dialog.isShowing()) {
                    dialog.dismiss()
                }
                Log.d(TAG, "onInterstitialAdShowFailed " + p0.errorMessage)
            }

            override fun onInterstitialAdClicked() {
                Log.d(TAG, "onInterstitialAdClicked")
            }
        }
        Log.d(TAG, "isInterstitialNotReady")
        IronSource.removeInterstitialListener()
        IronSource.setInterstitialListener(mInterstitialListener)
        IronSource.loadInterstitial()
        if (showLoadingDialog && (!activity.isFinishing)) {
            dialog.show()
        }
    }

    fun loadInterstitials(callback: InterstititialCallback) {
        if (!enableAds) {
            callback.onInterstitialClosed()
            return
        }
        IronSource.removeInterstitialListener()
        IronSource.setInterstitialListener(object : InterstitialListener {
            override fun onInterstitialAdReady() {
                callback.onInterstitialReady()
                isLoadInterstitialFailed = false
            }

            override fun onInterstitialAdLoadFailed(p0: IronSourceError?) {
                callback.onInterstitialLoadFail()
                isLoadInterstitialFailed = true
            }

            override fun onInterstitialAdOpened() {

            }

            override fun onInterstitialAdClosed() {
                callback.onInterstitialClosed()
                isInterstitialAdShowing = false
                IronSource.loadInterstitial()
            }

            override fun onInterstitialAdShowSucceeded() {
                callback.onInterstitialShowSucceed()
                lastTimeInterstitial = System.currentTimeMillis()
                isInterstitialAdShowing = true
            }

            override fun onInterstitialAdShowFailed(p0: IronSourceError?) {
                callback.onInterstitialClosed()
            }

            override fun onInterstitialAdClicked() {

            }
        })
        if (!IronSource.isInterstitialReady()) {
            IronSource.loadInterstitial()
        }
    }

    @MainThread
    fun loadInterstitials() {
        if (!enableAds) {
            return
        }
        IronSource.setInterstitialListener(emptyListener)
//            override fun onInterstitialAdReady() {
//                Log.d(TAG,"onInterstitialAdReady")
//            }
//
//            override fun onInterstitialAdLoadFailed(p0: IronSourceError?) {
//                Log.d(TAG,"onInterstitialAdLoadFailed")
//            }
//
//            override fun onInterstitialAdOpened() {
//                Log.d(TAG,"onInterstitialAdOpened")
//            }
//
//            override fun onInterstitialAdClosed() {
//                Log.d(TAG,"onInterstitialAdClosed")
//            }
//
//            override fun onInterstitialAdShowSucceeded() {
//                Log.d(TAG,"onInterstitialAdShowSucceeded")
//            }
//
//            override fun onInterstitialAdShowFailed(p0: IronSourceError?) {
//                Log.d(TAG,"onInterstitialAdShowFailed")
//            }
//
//            override fun onInterstitialAdClicked() {
//                Log.d(TAG,"onInterstitialAdClicked")
//            }
//        })
        if(!IronSource.isInterstitialReady()){
            IronSource.loadInterstitial()
        }
    }

    @Deprecated("Use the new showInterstitialsWithDialog method")
    fun showInterstitials(placementId: String) {
        if (IronSource.isInterstitialReady()) {
            IronSource.showInterstitial(placementId)
        }
    }

    val emptyListener = object : InterstitialListener {
        override fun onInterstitialAdReady() {
            Log.d(TAG, "onInterstitialAdReady")
            isLoadInterstitialFailed = false
        }

        override fun onInterstitialAdLoadFailed(p0: IronSourceError?) {
            Log.d(TAG, "onInterstitialAdLoadFailed")
            isLoadInterstitialFailed = true
        }

        override fun onInterstitialAdOpened() {
            Log.d(TAG, "onInterstitialAdOpened")
        }

        override fun onInterstitialAdClosed() {
            Log.d(TAG, "onInterstitialAdClosed")
        }

        override fun onInterstitialAdShowSucceeded() {
            Log.d(TAG, "onInterstitialAdShowSucceeded")
        }

        override fun onInterstitialAdShowFailed(p0: IronSourceError?) {
            Log.d(TAG, "onInterstitialAdShowFailed")
        }

        override fun onInterstitialAdClicked() {
            Log.d(TAG, "onInterstitialAdClicked")
        }
    }

    @MainThread
    fun showInterstitialsWithDialog(
        activity: AppCompatActivity,
        placementId: String,
        dialogDelayTime: Long,
        callback: InterstititialCallback
    ) {
        if (!enableAds) {
            callback.onInterstitialLoadFail()
            return
        }
        IronSource.setInterstitialListener(object : InterstitialListener {
            override fun onInterstitialAdReady() {
                activity.lifecycleScope.launch(Dispatchers.Main) {
                    isLoadInterstitialFailed = false
                    callback.onInterstitialReady()
                    IronSource.setInterstitialListener(emptyListener)
                }
            }

            override fun onInterstitialAdLoadFailed(p0: IronSourceError?) {
                activity.lifecycleScope.launch(Dispatchers.Main) {
                    isLoadInterstitialFailed = true
                    callback.onInterstitialLoadFail()
                    IronSource.setInterstitialListener(emptyListener)
                }
            }

            override fun onInterstitialAdOpened() {

            }

            override fun onInterstitialAdClosed() {
                callback.onInterstitialClosed()
                isInterstitialAdShowing = false
                loadInterstitials()
            }

            override fun onInterstitialAdShowSucceeded() {
                callback.onInterstitialShowSucceed()
                lastTimeInterstitial = System.currentTimeMillis()
                isInterstitialAdShowing = true
            }

            override fun onInterstitialAdShowFailed(p0: IronSourceError?) {
                callback.onInterstitialClosed()
            }

            override fun onInterstitialAdClicked() {

            }
        })

        if (IronSource.isInterstitialReady()) {
            activity.lifecycleScope.launch {
                if (dialogDelayTime > 0) {
                    var dialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
                    dialog.getProgressHelper().barColor = Color.parseColor("#A5DC86")
                    dialog.setTitleText("Loading ads. Please wait...")
                    dialog.setCancelable(false)
                    activity.lifecycle.addObserver(DialogHelperActivityLifeCycle(dialog))
                    if (!activity.isFinishing) {
                        dialog.show()
                    }
                    delay(dialogDelayTime)
                    if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && dialog.isShowing()) {
                        dialog.dismiss()
                    }
                }
                if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    Log.d(TAG, "onInterstitialAdReady")
                    IronSource.showInterstitial(placementId)
                }
            }
        } else {
            activity.lifecycleScope.launch(Dispatchers.Main) {
                IronSource.setInterstitialListener(emptyListener)
                callback.onInterstitialClosed()
            }
        }
    }

    fun showInterstitialAdsWithCallbackCheckTime(
        activity: AppCompatActivity,
        adPlacementId: String,
        showLoadingDialog: Boolean,
        timeInMillis: Long,
        callback: AdCallback
    ) {
        if (!enableAds) {
            callback.onAdFail()
            return
        }
        IronSource.removeInterstitialListener()
        if (!(System.currentTimeMillis() - timeInMillis > lastTimeInterstitial) || (!enableAds)) {
            callback.onAdFail()
            return
        }
        var dialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        dialog.getProgressHelper().barColor = Color.parseColor("#A5DC86")
        dialog.setTitleText("Loading ads. Please wait...")
        dialog.setCancelable(false)
        activity.lifecycle.addObserver(DialogHelperActivityLifeCycle(dialog))
        val mInterstitialListener = object : InterstitialListener {
            override fun onInterstitialAdReady() {
                if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED) && dialog.isShowing()) {
                    dialog.dismiss()
                }
                Log.d(TAG, activity.lifecycle.currentState.toString())
                if (activity.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    Log.d(TAG, "onInterstitialAdReady")
                    IronSource.showInterstitial(adPlacementId)
                }
            }

            override fun onInterstitialAdLoadFailed(p0: IronSourceError) {
                if (!activity.isFinishing() && dialog.isShowing()) {
                    dialog.dismiss()
                }
                callback.onAdFail()
                Log.d(TAG, "onInterstitialAdLoadFailed " + p0.errorMessage)
            }

            override fun onInterstitialAdOpened() {
                Log.d(TAG, "onInterstitialAdOpened")
            }

            override fun onInterstitialAdClosed() {
                callback.onAdClosed()
                isInterstitialAdShowing = false
                Log.d(TAG, "onInterstitialAdClosed")
            }

            override fun onInterstitialAdShowSucceeded() {
                Log.d(TAG, "onInterstitialAdShowSucceeded")
                lastTimeInterstitial = System.currentTimeMillis()
                isInterstitialAdShowing = true
            }

            override fun onInterstitialAdShowFailed(p0: IronSourceError) {
                if (!activity.isFinishing() && dialog.isShowing()) {
                    dialog.dismiss()
                }
                Log.d(TAG, "onInterstitialAdShowFailed " + p0.errorMessage)
            }

            override fun onInterstitialAdClicked() {
                Log.d(TAG, "onInterstitialAdClicked")
            }
        }
        Log.d(TAG, "isInterstitialNotReady")
        IronSource.loadInterstitial()
        if (showLoadingDialog && (!activity.isFinishing)) {
            dialog.show()
        }
        IronSource.setInterstitialListener(mInterstitialListener);
    }

    fun showBanner(activity: AppCompatActivity, bannerContainer: ViewGroup, adPlacementId: String) {
        if (!enableAds) {
            return
        }
        destroyBanner()
        bannerContainer.removeAllViews()
        banner = IronSource.createBanner(activity, ISBannerSize.SMART)
        banner.bannerListener = object : BannerListener {
            override fun onBannerAdLoaded() {
                Log.d(TAG, "onBannerAdLoaded")
            }

            override fun onBannerAdLoadFailed(p0: IronSourceError?) {
                Log.d(TAG, "onBannerAdLoadFailed" + p0.toString())
            }

            override fun onBannerAdClicked() {
                Log.d(TAG, "onBannerAdClicked")
            }

            override fun onBannerAdScreenPresented() {
                Log.d(TAG, "onBannerAdScreenPresented")
            }

            override fun onBannerAdScreenDismissed() {
                Log.d(TAG, "onBannerAdScreenDismissed")
            }

            override fun onBannerAdLeftApplication() {
                Log.d(TAG, "onBannerAdLeftApplication")
            }
        }
        bannerContainer.addView(banner)
        IronSource.loadBanner(banner, adPlacementId)
//        activity.lifecycle.addObserver(object:LifecycleObserver{
//            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
//            fun onPause(){
//                destroyBanner(banner,bannerContainer)
//                activity.lifecycle.removeObserver(this)
//            }
//        })
    }

    fun destroyBanner() {
//        viewGroup.removeAllViews()
        if (this::banner.isInitialized) {
            IronSource.destroyBanner(banner)
        }
    }
}