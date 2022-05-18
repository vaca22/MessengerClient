package com.vaca.messengerclient

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast


class MainActivity : Activity() {
    var messenger: Messenger? = null
    var reply: Messenger? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        reply = Messenger(handler)
        val intent = Intent()
        intent.setClassName("com.vaca.messengerserver", "com.vaca.messengerserver.MyService")

        // 绑定服务
        bindService(intent, object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName) {}
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                Toast.makeText(this@MainActivity, "bind success", 0).show()
                messenger = Messenger(service)
            }
        }, Context.BIND_AUTO_CREATE)
    }

    fun sendMessage() {
        val msg: Message = Message.obtain(null, 1)
        // 设置回调用的Messenger
        msg.replyTo = reply
        try {
            messenger!!.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val bundle = msg.data
            val lat = bundle.getString("lat")
            val lon = bundle.getString("lon")
            Log.i("TAG", "lat:$lat")
            Log.i("TAG", "lon :$lon")
        }
    }

    companion object {
        protected const val TAG = "MainActivity"
    }

    fun fuck(view: View) {
        sendMessage()
    }
}