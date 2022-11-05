package com.misterh.tech.angela.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import com.google.android.material.switchmaterial.SwitchMaterial
import com.misterh.tech.angela.MainActivity
import com.misterh.tech.angela.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getBoolean(ARG_PARAM1)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {





        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onStart() {
        super.onStart()

        param1 = (activity as MainActivity).checkNotificationEnabled()
        val switch = requireView().findViewById(R.id.switch2) as SwitchMaterial

        switch.setOnClickListener() {
            if(switch.isChecked) {
                if(param1 == false) {
                    val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                    startActivity(intent)
                }
                Toast.makeText(activity, "Bot Enabled", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(activity, "Bot Disabled", Toast.LENGTH_SHORT).show()
            }
            val pref = activity?.getSharedPreferences("com.misterh.tech.angela", Context.MODE_PRIVATE)
            with(pref?.edit()) {
                this?.putBoolean("mode", switch.isChecked)
                this?.apply()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Boolean) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_PARAM1, param1)
                }
            }
    }
}