package com.example.madpractical7_21012012011

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.util.*

class MainActivity : AppCompatActivity() {
    var mili: Long = 0

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val CancleAlarmCardView = findViewById<MaterialCardView>(R.id.remove_alarm_card)
        val BtnCreateAlarm = findViewById<MaterialButton>(R.id.add_alarm)
        val SetAlarmTime = findViewById<TextView>(R.id.time)
        val BtnCancelAlarm = findViewById<MaterialButton>(R.id.remove_alarm)
        val clockTC = findViewById<TextClock>(R.id.text_clock)

        clockTC.format12Hour = "hh:mm:ss a"

        CancleAlarmCardView.visibility = View.VISIBLE

        BtnCreateAlarm.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
            val min: Int = cal.get(Calendar.MINUTE)
            val timepickerdialog =
                TimePickerDialog(this, { view, h, m ->
                    mili = getMillis(h, m)
                    setAlarm(getMillis(h, m), "Start")
                    SetAlarmTime.text = "$h:$m"
                }, hour, min, false)
            timepickerdialog.show()
        }

        BtnCancelAlarm.setOnClickListener {
            setAlarm(mili, "Stop")
            CancleAlarmCardView.visibility = View.VISIBLE
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setAlarm(millisTime: Long, str: String) {
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", str)
        val pendingIntent =
            PendingIntent.getBroadcast(applicationContext, 234324243, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if (str == "Start") {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntent
            )
            Toast.makeText(this, "Start Alarm", Toast.LENGTH_SHORT).show()
        } else if (str == "Stop") {
            alarmManager.cancel(pendingIntent)
            sendBroadcast(intent)
            Toast.makeText(this, "Stop Alarm", Toast.LENGTH_SHORT).show()
        }
    }

    fun getMillis(hour: Int, min: Int): Long {
        val setcalendar = Calendar.getInstance()
        setcalendar[Calendar.HOUR_OF_DAY] = hour
        setcalendar[Calendar.MINUTE] = min
        setcalendar[Calendar.SECOND] = 0
        return setcalendar.timeInMillis
    }
}