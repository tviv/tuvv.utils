package com.tuvv.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.tuvv.utils.logging.ALog
import java.util.*

open class PreferencesWrapper(val context: Context) {

    private lateinit var prefs: SharedPreferences
    private var sharedEditor: SharedPreferences.Editor? = null

    init {
        setSharedPreferences(PreferenceManager.getDefaultSharedPreferences(context))
    }

    fun setSharedPreferences(prefs: SharedPreferences) {
        this.prefs = prefs
    }

    fun setValues(values: HashMap<String, Any>) {
        startEditing()
        values.forEach {x-> setValue(x.key, x.value) }
        endEditing()
    }

    /**
     * Enter in edit mode
     * To use for bulk modifications
     */
    @SuppressLint("CommitPrefEdits")
    fun startEditing() {
        synchronized(this) {
            sharedEditor = prefs.edit()
        }
    }

    /**
     * Leave edit mode
     */
    fun endEditing() {
        synchronized(this) {
            sharedEditor?.let {
                it.apply()
                sharedEditor = null
            }
        }
    }

    /**
     * Set a preference boolean value
     * @param key the preference key to set
     * @param value the value for this key
     */
    @SuppressLint("CommitPrefEdits")
    fun <T> setValue(key: String?, value: T): Boolean {
        val editor = sharedEditor ?: prefs.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            else -> {
                ALog.e("wrong type to save")
                return false
            }
        }
        sharedEditor ?: run {editor.apply()}
        return true
    }

    fun getStringValue(key: String): String {
        return prefs.getString(key, "")!!
    }

    fun getIntValue(key: String): Int {
        return prefs.getInt(key, 0)
    }

    @JvmOverloads
    fun getBooleanValue(key: String, defValue: Boolean = false): Boolean {
        return prefs.getBoolean(key, defValue)
    }
}