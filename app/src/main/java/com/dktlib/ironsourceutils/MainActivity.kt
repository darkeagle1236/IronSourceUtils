package com.dktlib.ironsourceutils

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dktlib.ironsourcelib.AdCallback
import com.dktlib.ironsourcelib.IronSourceActivityLifeCycle
import com.dktlib.ironsourcelib.IronSourceLifeCycleHelper
import com.dktlib.ironsourcelib.IronSourceUtil

class MainActivity : AppCompatActivity() {
    lateinit var bannerContainer : ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        IronSourceUtil.initIronSource(this, "85460dcd")
        IronSourceUtil.validateIntegration(this)
        this.lifecycle.addObserver(IronSourceActivityLifeCycle(this))
        val btn = findViewById<Button>(R.id.btn_load_inter)
        bannerContainer = findViewById<FrameLayout>(R.id.banner_container)
        val bannerContainer = findViewById<FrameLayout>(R.id.banner_container)
        IronSourceUtil.showBanner(this,bannerContainer,"main")
        btn.setOnClickListener {
            IronSourceUtil.showInterstitialAdsWithCallback(
                this,
                "",
                true,object : AdCallback {
                    override fun onAdClosed() {
                        val intent = Intent(this@MainActivity,MainActivity2::class.java)
                        startActivity(intent)
                    }

                    override fun onAdFail() {
                        onAdClosed()
                    }
                })
        }

    }

//    override fun onPause() {
//        if(this::bannerContainer.isInitialized){
//            IronSourceUtil.destroyBanner(bannerContainer)
//        }
//        super.onPause()
//    }
//    override fun onResume() {
//        super.onResume()
//    }
}