package com.tuvv.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock

//todo add interface
class SystemTimer() {

    enum class AlarmType { ExactSingle, ExactRepeating, InExactRepeating }

    lateinit var context: Context

    private val am by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    constructor(context: Context) : this() {
        this.context = context
    }

    fun setTimer(pendingIntent: PendingIntent, interval: Int, alarmType: AlarmType) {
        val currentTime = System.currentTimeMillis()
        when (alarmType) {
            AlarmType.ExactSingle ->
                am.setExact(AlarmManager.RTC_WAKEUP, currentTime + interval * 1000, pendingIntent)
            AlarmType.ExactRepeating ->
                am.setRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + interval * 1000L,
                    interval * 1000L, pendingIntent
                )
            AlarmType.InExactRepeating ->
                am.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + interval * 1000L,
                    interval * 1000L, pendingIntent
                )
        }
    }

    fun clearTimer(pendingIntent: PendingIntent) {
        am.cancel(pendingIntent)
    }

    companion object {

        @JvmStatic
        fun makeSimpleOperation(context: Context, action: String, zlass: Class<*>): PendingIntent {
            val intent = Intent(
                action,
                null,
                context,
                zlass
            )
            return PendingIntent.getBroadcast(context, 0, intent, 0)

        }
    }
}
