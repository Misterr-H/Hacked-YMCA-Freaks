package com.misterh.tech.angela

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import okhttp3.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit


class NotificationService: NotificationListenerService() {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.i("ServiceLog", "Service Started")
        return START_STICKY
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.i("ServiceLog", "Listener Connected")
        val notif: Array<StatusBarNotification> = activeNotifications
        Log.i("ServiceLog", notif.size.toString())
    }

//So many warnings

    override fun onNotificationPosted(sbn: StatusBarNotification?) {

        val pref = getSharedPreferences("com.misterh.tech.angela", Context.MODE_PRIVATE)
        val isServiceEnabled = pref.getBoolean("mode", false)
        super.onNotificationPosted(sbn)
        val pack: String = sbn!!.packageName
        Log.i("APP", "Package name is $pack")
        Toast.makeText(this@NotificationService, "Package is $pack", Toast.LENGTH_SHORT).show()
        if(pack == "com.whatsapp") {
            val notification: Notification = sbn.notification
            val extras: Bundle? = notification.extras
            val title: String? = extras!!.getString("android.title")
            val msg: String = extras.getCharSequence("android.text").toString()
            if (title != null && !msg.contains("new messages")) {
                Log.i("WhatsappTitle", title)
            }
            if(title!= null && (title=="Himanshu Saini YMCA" || title=="YMCA UCC Sourav") && msg!=null && !msg.contains("new messages") && !title.contains("You") && isServiceEnabled ) {
                val response:Response = run("http://161.97.165.160:4000/angela", msg)
                val replyBody = response.body()?.string()
                val json = JSONObject(replyBody)
                val jsonArray = json.getJSONArray("replies")
                val jsonObject: JSONObject = jsonArray.getJSONObject(0)
                val replyMessage= jsonObject.getString("message")
                Log.i("REPLYMESSAGE", replyMessage)



//                Log.i("WhatsappMessage", msg)
//                for(i in 0..NotificationCompat.getActionCount(notification) -1 ) {
//                for(i in 0..1) {
                if(NotificationCompat.getActionCount(notification)!=0) {
                    val action: NotificationCompat.Action? = NotificationCompat.getAction(notification, 0)
                    action?.remoteInputs?.let {
                        for(x in 0..it.size) {
                            val remoteInput: RemoteInput? = action.remoteInputs?.get(x)
                            if (remoteInput != null) {
                                if(remoteInput.resultKey.lowercase().contains("reply")) {
                                    val bundle = Bundle()
                                    val intent = Intent()
                                    val actualInput = ArrayList<RemoteInput>()
                                    for(input in action.remoteInputs!!) {
                                        Log.i("RESultKey", input.resultKey)
                                        bundle.putCharSequence(input.resultKey, replyMessage)
                                        val builder:RemoteInput.Builder = RemoteInput.Builder(input.resultKey)
                                        builder.setLabel(input.label)
                                        builder.setChoices(input.choices)
                                        builder.setAllowFreeFormInput(input.allowFreeFormInput)
                                        builder.addExtras(input.extras)
                                        actualInput.add(builder.build())
                                    }
                                    val inputs = actualInput.toTypedArray()
                                    RemoteInput.addResultsToIntent(inputs, intent, bundle)
                                    val p: PendingIntent = action.actionIntent
                                    p.send(this@NotificationService, 0, intent)
                                    break
                                }

                            }
                        }
                    }

                }

            }
        }

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        Log.i("NotificationService", "Notification Removed")
        Toast.makeText(this@NotificationService, "Notification removed", Toast.LENGTH_SHORT).show()
    }

    private fun run(url: String, message: String): Response {
        val SDK_INT = Build.VERSION.SDK_INT
        if (SDK_INT > 8) {
            val policy = ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)
            //your codes here
            Log.i("API Called", "API CAlled")
            val json = "{\"query\":{\"message\":\"${message}\"}}"
            val body = RequestBody.create(
                MediaType.parse("application/json"), json
            )
            Log.i("json", json)

            val request = Request.Builder()
                .url(url)
                .post(body)
                .build()

            val call: Call = client.newCall(request)
            return call.execute()
        }
        val policy = ThreadPolicy.Builder()
            .permitAll().build()
        StrictMode.setThreadPolicy(policy)
        //your codes here
        Log.i("API Called", "API CAlled")
        val json = "{\"query\":{\"message\":\"${message}\"}}"
        val body = RequestBody.create(
            MediaType.parse("application/json"), json
        )
        Log.i("json", json)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val call: Call = client.newCall(request)
        return call.execute()
    }
}