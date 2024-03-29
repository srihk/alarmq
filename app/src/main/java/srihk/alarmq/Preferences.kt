package srihk.alarmq

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.compose.runtime.snapshots.SnapshotStateList

object Preferences {
    const val PREFERENCES_NAME = "prefs"
    private const val KEY_SNOOZE_LIST = "snoozeList"
    private const val KEY_IS_RUNNING = "isRunning"
    private const val KEY_STATE = "state"
    private const val KEY_NEXT_ALARM ="nextAlarm"

    fun setList(context: Context, list: SnapshotStateList<Int>) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, ComponentActivity.MODE_PRIVATE)
        val editor = prefs.edit()

        editor.putString(KEY_SNOOZE_LIST, list.joinToString(" "))
        editor.apply()
    }

    fun getList(context: Context): MutableList<Int> {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, ComponentActivity.MODE_PRIVATE)
        val s = prefs.getString(KEY_SNOOZE_LIST, "")
        val list = mutableListOf<Int>()
        if (s != null && s.isNotEmpty()) {
            for (elem in s.split(" ")) {
                list.add(elem.toInt())
            }
        }
        return list
    }

    fun getIsRunning(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, ComponentActivity.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_RUNNING, false)
    }

    fun setIsRunning(context: Context, isRunning: Boolean) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, ComponentActivity.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean(KEY_IS_RUNNING, isRunning)
        editor.apply()
    }

    fun getState(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, ComponentActivity.MODE_PRIVATE)
        return prefs.getInt(KEY_STATE, 0)
    }

    fun setState(context: Context, state: Int) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, ComponentActivity.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(KEY_STATE, state)
        editor.apply()
    }

    fun getNextAlarm(context: Context): String {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, ComponentActivity.MODE_PRIVATE)
        return prefs.getString(KEY_NEXT_ALARM, "") ?: return ""
    }

    fun setNextAlarm(context: Context, nextAlarm: String) {
        val prefs = context.getSharedPreferences(PREFERENCES_NAME, ComponentActivity.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_NEXT_ALARM, nextAlarm)
        editor.apply()
    }
}