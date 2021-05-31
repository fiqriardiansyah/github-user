package com.example.githubuser

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceActivity
import android.widget.Toast

class SettingPreferenceActivity : PreferenceActivity(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference)

        alarmReceiver = AlarmReceiver()
    }

    override fun onResume() {
        super.onResume()
        this.preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if(key == "alarm"){

            val isActiv = sharedPreferences?.getBoolean("alarm",false)!!

            if (isActiv){
                alarmReceiver.setAlarm(this)
            }else{
                alarmReceiver.cancelAlarm(this)
            }

            Toast.makeText(this@SettingPreferenceActivity,if(isActiv) "alarm on" else "alarm off",Toast.LENGTH_SHORT).show()
        }
    }
}