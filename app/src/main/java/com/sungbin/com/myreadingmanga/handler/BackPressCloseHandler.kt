package com.sungbin.com.myreadingmanga.handler

import android.app.Activity
import android.widget.Toast
import com.shashank.sony.fancytoastlib.FancyToast
import com.sungbin.com.myreadingmanga.R
import com.sungbin.com.myreadingmanga.utils.Utils

class BackPressCloseHandler(private val activity: Activity) {

    private var backKeyPressedTime: Long = 0
    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis()
            Utils.toast(activity,
                activity.getString(R.string.one_more_exit),
                FancyToast.LENGTH_SHORT, FancyToast.WARNING
            )
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish()
        }
    }

}