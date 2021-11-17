# IronSourceUtils
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
        IronSourceUtil.initIronSource(this, "app-key")
 ```
 #### Mediation Adapter
 
 If you're going to use IronSource Mediation with other networks, you have to implement the corresponding network adapter
 Here's all network adapter you need:
 https://developers.is.com/ironsource-mobile/android/mediation-networks-android/#step-1
#### Load and show interstitials
 ```sh
         IronSourceUtil.showInterstitialAdsWithCallback(
                this,
                "ad-placement-id",
                true,object : AdCallback {
                    override fun onAdClosed() {
                       // TODO
                    }

                    override fun onAdFail() {
                        // TODO
                    }
                })
        }
 ```
 #### Load a banner
 
 ```sh
 IronSourceUtil.showBanner(activity,viewgroup,"ad-placement-id")
  ```
