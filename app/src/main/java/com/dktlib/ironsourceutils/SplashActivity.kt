package com.dktlib.ironsourceutils

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dktlib.ironsourcelib.InterstititialCallback
import com.dktlib.ironsourcelib.IronSourceLifeCycleHelper
import com.dktlib.ironsourcelib.IronSourceUtil
import com.dktlib.ironsourcelib.IronSourceUtil.showInterstitialsWithDialog
import com.dktlib.ironsourceutils.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        IronSourceUtil.initIronSource(this, "85460dcd", true)
        IronSourceUtil.validateIntegration(this)
        this.application.registerActivityLifecycleCallbacks(IronSourceLifeCycleHelper)
        binding.btnNext.setOnClickListener {
            showInterstitialsWithDialog(this,"splash",0,object : InterstititialCallback {
                override fun onInterstitialReady() {

                }

                override fun onInterstitialClosed() {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                }

                override fun onInterstitialLoadFail() {
                    onInterstitialClosed()
                }

                override fun onInterstitialShowSucceed() {

                }
            })
        }
        IronSourceUtil.loadInterstitials(this,3000,object : InterstititialCallback {
            override fun onInterstitialReady() {
                binding.btnNext.visibility = View.VISIBLE
            }

            override fun onInterstitialClosed() {

                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }

            override fun onInterstitialLoadFail() {
                onInterstitialClosed()
            }

            override fun onInterstitialShowSucceed() {

            }
        })
    }
}