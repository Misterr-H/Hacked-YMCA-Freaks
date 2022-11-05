package com.misterh.tech.angela

import android.content.ContentResolver
import android.content.Context
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.misterh.tech.angela.Fragment.MainFragment
import com.misterh.tech.angela.Fragment.ScrollingFragment
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import okhttp3.*


class MainActivity : AppCompatActivity() {
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)

        val viewpager: ViewPager2 = findViewById(R.id.view_pager)
        val fragments: ArrayList<Fragment> = arrayListOf(
            MainFragment.newInstance(checkNotificationEnabled()),
            ScrollingFragment()
        )

        val adapter = ViewPagerAdapter(fragments, this)
        viewpager.adapter = adapter
        dotsIndicator.attachTo(viewpager)
    }

    fun run(url: String, message: String): Response {
        val json = "{\"query\":{\"message\":${message}}}"
        val body = RequestBody.create(
            MediaType.parse("application/json"), json
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val call: Call = client.newCall(request)
        return call.execute()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
        val pref = this.getSharedPreferences("com.misterh.tech.angela", Context.MODE_PRIVATE)
        with(pref.edit()) {
            putBoolean("mode", false)
            apply()
        }
    }

    public fun checkNotificationEnabled(): Boolean {
        val contentResolver: ContentResolver = contentResolver
        val enabledNotificationListeners: String = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val packageName: String = packageName
        return enabledNotificationListeners.contains(packageName)
    }
}