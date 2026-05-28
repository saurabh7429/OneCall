package com.onecall.data

import android.content.Context

object DeviceRoleStore {
    private const val PREFS_NAME = "onecall_device_role"
    private const val KEY_ROLE = "device_role"

    const val ROLE_MAIN = "MAIN"
    const val ROLE_SECONDARY = "SECONDARY"

    fun setRole(context: Context, role: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_ROLE, role)
            .apply()
    }

    fun getRole(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_ROLE, null)
    }

    fun isMain(context: Context): Boolean {
        return getRole(context) == ROLE_MAIN
    }
}
