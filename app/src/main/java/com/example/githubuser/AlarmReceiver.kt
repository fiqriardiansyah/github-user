package com.example.githubuser

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ID_REPEATING = 101
        const val EXTRA_MESSAGE = "MESSAGE"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.

        val intent = Intent(context,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,intent,0)

        val channelId = "channel_1"
        val channelName ="AlarmManagerChannel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context,channelId)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_baseline_notifications_black)
            .setContentTitle("alarm")
            .setContentText("balik yukk")
            .setColor(ContextCompat.getColor(context,android.R.color.transparent))
            .setVibrate(longArrayOf(1000,1000,500,1000,1000))
            .setSound(alarmSound)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val chanel = NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT)
            chanel.enableVibration(true)
            chanel.vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(chanel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(ID_REPEATING,notification)
    }

    fun setAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val mIntent = Intent(context,AlarmReceiver::class.java)
        mIntent.putExtra(EXTRA_MESSAGE,"ayo liat github")

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,9)
        calendar.set(Calendar.MINUTE,0)
        calendar.set(Calendar.SECOND,0)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING,mIntent,0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,AlarmManager.INTERVAL_DAY,pendingIntent)

    }

    fun cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }




}