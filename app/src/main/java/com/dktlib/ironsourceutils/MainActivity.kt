package com.dktlib.ironsourceutils

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dktlib.ironsourcelib.AdCallback
import com.dktlib.ironsourcelib.IronSourceUtil

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        IronSourceUtil.initIronSource(this, "85460dcd")
        IronSourceUtil.validateIntegration(this)
        val loadAndShowInterBtn = findViewById<Button>(R.id.loadAndShowInter)
        loadAndShowInterBtn.setOnClickListener {
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
}