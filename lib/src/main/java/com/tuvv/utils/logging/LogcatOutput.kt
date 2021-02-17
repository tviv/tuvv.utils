package com.tuvv.utils.logging

import android.util.Log

class LogcatOutput: IMessageOutput {
    override fun send(priority: Int, tag: String, msg: String) {
        when (priority) {
            ALog.ERROR -> Log.e(tag, msg)
            ALog.WARN -> Log.w(tag, msg)
            ALog.INFO -> Log.i(tag, msg)
            ALog.DEBUG -> Log.d(tag, msg)
            ALog.VERBOSE -> Log.v(tag, msg)
            else -> throw IllegalArgumentException("bad priority argument")
        }
    }
}