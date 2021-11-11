package com.dktlib.ironsourceutils

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dktlib.ironsourcelib.AdCallback
import com.dktlib.ironsourcelib.IronSourceUtil
import com.dktlib.ironsourceutils.databinding.FragmentBlankBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    lateinit var binding:FragmentBlankBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_blank,container,false)
        binding.loadAndShowInter.setOnClickListener{

                IronSourceUtil.showInterstitialAdsWithCallback(
                    requireActivity() as AppCompatActivity,
                    "",
                    true,object : AdCallback {
                        override fun onAdClosed() {
//                        val intent = Intent(this@MainActivity,MainActivity2::class.java)
//                        startActivity(intent)
                            Toast.makeText(requireActivity(),"YOOY",Toast.LENGTH_LONG).show()
                        }

                        override fun onAdFail() {
                            onAdClosed()
                        }
                    })

        }
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            BlankFragment()
    }
}