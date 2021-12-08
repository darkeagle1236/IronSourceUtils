package com.dktlib.ironsourceutils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dktlib.ironsourcelib.AdCallback
import com.dktlib.ironsourcelib.IronSourceUtil

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        IronSourceUtil.initIronSource(this, "85460dcd", true)
        IronSourceUtil.validateIntegration(this)
        IronSourceUtil.showInterstitialAdsWithCallback(this@SplashActivity,"main",false,object :
            AdCallback {
            override fun onAdClosed() {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            }

            override fun onAdFail() {
                onAdClosed()
            }
        })
    }
}