package com.familidata.sbnwas_cma.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.main.MainActivity
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.entity.CmaNotice
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.round


// H.Point 서버와 API 통신
object CommonUtil {

    private var toast: Toast? = null

    fun showToast(context: Context, message: String) {
        try {
            if (toast != null) toast?.cancel()
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
            toast?.show()
        } catch (e: Exception) {
        }
    }


    /*
      workdate: 'yyyy-MM-dd',  time : 'HH:mm'
      을 파라메터로 받아서 data 형식으로 변환한다
     */
    fun stringToDate(workdate: String, time: String): Date? {
        // 문자열
        var dateStr = "$workdate $time"
        // 포맷터
        var formatter = SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 문자열 -> Date
        var date = formatter.parse(dateStr);
        Log.i(date)
        return date
    }

    ///일시작 1시간전 확인하기
//    fun checkOneHourBeforeWork(workdate: String, time: String) {
//        val date = stringToDate(workdate, time)
//        val cal = Calendar.getInstance()
//        if (date != null) {
//            cal.time = date
//        }
//        cal.add(Calendar.HOUR, -1)
//        var sdformat = SimpleDateFormat("
//
//
//    }

    fun createNotificationChannel(context: Context, title: String, descrption: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        // SAVE CMA NOTICE
        saveCmaNotice(title, descrption, "CMA")

        val name = context.getString(R.string.app_name)
        val descriptionText = context.getString(R.string.address)
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)


        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.mipmap.app_icon)
            .setContentTitle(title)
            .setContentText(descrption)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    fun saveCmaNotice(title: String, body: String, source: String) {

        DBManager.withDB().withCmaNotice().insert(
            CmaNotice(
                NOTICE_TITLE = title,
                NOTICE_BODY = body,
                NOTICE_SOURCE = source,
                CREATE_DTTM = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )

    }


    fun getDayOfWeek(dateString: String): Int {

        var dates: List<String> = dateString.split("-")

        if (dates.size < 3) return 0

        var cal: Calendar = Calendar.getInstance();

        cal.set(Calendar.YEAR, dates.get(0).toInt())
        cal.set(Calendar.MONTH, dates.get(1).toInt() - 1)
        cal.set(Calendar.DATE, dates.get(2).toInt())

        return cal.get(Calendar.DAY_OF_WEEK)

    }

    fun getTimeStringWithAmPm(timeString: String): String {

        var times: List<String> = timeString.split(":")

        if (times.size < 2) return ""

        val sb = StringBuilder()

        if (times[0].toInt() < 12) {
            sb.append("오전 ").append(times[0].toInt())
        } else {
            sb.append("오후 ").append(times[0].toInt() - 12)
        }

        sb.append(":").append(times[1])

        return sb.toString()

    }

    fun convertLongToyyyyMMdd_E_(time: Long?): String {
        val date = time?.let { Date(it) }
        val format = SimpleDateFormat("yyyy-MM-dd (E)")
        return format.format(date)
    }

    fun convertLongToyyyyMMddHHmm(time: Long?): String {
        val date = time?.let { Date(it) }
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date)
    }

    fun convertDateStringToLong(dateString: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = dateFormat.parse(dateString)
        return date.time
    }

    fun convertLongToHHmm(time: Long?): String {
        val date = time?.let { Date(it) }
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    fun convertStringToHHmm(time: String?): String {
//        val date = time?.let { Date(it.toLong()) }
//        val format = SimpleDateFormat("HH:mm")
        return time!!.substring(time.length - 8, time.length - 3)
    }

    fun convertLogToHour(mins: Long?): Pair<String, String> {
        val minutes = mins
        val hours = mins!!.toInt() / 60
        var mins = mins.toInt() % 60

        var strH = "0"
        var strM = "00"
        if (hours > 0) strH = hours.toString()
        if (mins > 0) strM = mins.toString()
        return Pair(strH, strM)
    }

    fun convertLogToHourDetail(mins: Long?): Pair<String, String> {
        val minutes = mins
        val hours = mins!!.toInt() / 60
        var mins = mins.toInt() % 60

        var strH = ""
        var strM = "00"
        if (hours > 0) strH = hours.toString() + "시간"
        if (mins > 0) strM = mins.toString()
        strM += "분"
        return Pair(strH, strM)
    }

    fun toIntStringData(originString: String): String {
        if (originString.isEmpty()) {
            return ""
        } else {
            return originString.toDouble().toInt().toString()
        }
    }

    fun toDoubleStringData(originString: String): String {
        // 소수점 1자리 문자열로
        if (originString.isEmpty()) {
            return ""
        } else {
            return String.format("%.1f", (round(originString.toDouble() * 10) / 10))
        }
    }


    fun getNowTimeOnUTC(): String {
        return LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }

}

//fun String.toDate(dateFormat: String = "yyyy-MM-dd HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
//    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
//    parser.timeZone = timeZone
//    return parser.parse(this)
//}
//
//fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
//    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
//    formatter.timeZone = timeZone
//    return formatter.format(this)
//}
