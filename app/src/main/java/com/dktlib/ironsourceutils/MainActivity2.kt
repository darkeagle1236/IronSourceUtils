package com.dktlib.ironsourceutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.dktlib.ironsourcelib.IronSourceUtil

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val transaction = this.supportFragmentManager.beginTransaction()
//        val frag = BlankFragment()
//        transaction.replace(R.id.container, frag)
//        transaction.commit()
    }
    override fun onResume() {
        val bannerContainer = findViewById<FrameLayout>(R.id.banner_container)
        IronSourceUtil.showBanner(this,bannerContainer,"banner_main")
        super.onResume()
    }

    override fun onPause() {
        val bannerContainer = findViewById<FrameLayout>(R.id.banner_container)
        IronSourceUtil.destroyBanner(bannerContainer)
        super.onPause()
    }
}