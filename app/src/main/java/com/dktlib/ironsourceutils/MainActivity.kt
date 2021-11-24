package com.dktlib.ironsourceutils

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dktlib.ironsourcelib.*

class MainActivity : AppCompatActivity() {
    lateinit var bannerContainer: ViewGroup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        IronSourceUtil.initIronSource(this, "85460dcd", true)
        IronSourceUtil.validateIntegration(this)
        val btnLoad = findViewById<Button>(R.id.btn_load_inter)
        val btnShow = findViewById<Button>(R.id.btn_show_inter)
        bannerContainer = findViewById<FrameLayout>(R.id.banner_container)
        val bannerContainer = findViewById<FrameLayout>(R.id.banner_container)
        btnLoad.setOnClickListener {
            IronSourceUtil.loadInterstitials(object : InterstititialCallback {
                override fun onInterstitialReady() {
                    btnShow.visibility = View.VISIBLE
                }

                override fun onInterstitialClosed() {
                    startActivity(Intent(this@MainActivity, MainActivity2::class.java))
                }

                override fun onInterstitialLoadFail() {
                    Toast.makeText(this@MainActivity,"Load failed",Toast.LENGTH_LONG).show()
                }
            })


        }
        btnShow.setOnClickListener {
            IronSourceUtil.showInterstitials("yo")
        }
        }
        //    override fun onPause() {
//        if(this::bannerContainer.isInitialized){
//            IronSourceUtil.destroyBanner(bannerContainer)
//        }
//        super.onPause()
//    }
        override fun onResume() {
            val bannerContainer = findViewById<FrameLayout>(R.id.banner_container)
            IronSourceUtil.showBanner(this, bannerContainer, "main")
            super.onResume()
        }
    }