package com.example.video_explorer.ui.render

import android.content.Context
import android.util.Log
import com.example.video_explorer.data.youtubeData.CommentItem
import com.example.video_explorer.data.youtubeData.VideoItem
import com.example.video_explorer.data.youtubeData.YoutubeVideoComment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun calculateView(view: String): String {
    val viewNumber = view.toDouble()
    if(viewNumber >= 1000000000)
        return "${String.format("%.1f", viewNumber / 1000000000)} T"
    else if (viewNumber >= 10000000)
        return "${(viewNumber / 1000000).toInt()} Tr"
    else if (viewNumber >= 1000000)
        return "${String.format("%.1f", viewNumber / 1000000)} Tr"
    else if (viewNumber >= 10000)
        return "${(viewNumber / 1000).toInt()} N"
    else if (viewNumber >= 1000)
        return "${String.format("%.1f", viewNumber / 1000)} N"
    else
        return view
}

fun calculateLike(view: String): String {
    return calculateView(view = view)
}

fun calculateSubscriber(subscriber: String): String {
    return calculateView(subscriber)
}

fun calculateTime(start: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    formatter.timeZone = TimeZone.getTimeZone("UTC")

    val startTime = formatter.parse(start)
    val currentTime = Date()

    val numberOfDaysInStartMonth = numberOfDays(startTime.month, startTime.year)

    val diff = (currentTime.time - startTime.time) / 1000

    if (currentTime.year - startTime.year >= 1 && diff / (60*60) >= (24*30*12))
        return "${currentTime.year - startTime.year} năm trước"
    else if ((currentTime.month - startTime.month + 12) % 12 >= 1 && diff / (60*60) >= (24*30))
        return "${(currentTime.month - startTime.month + 12) % 12} tháng trước"
    else if ((currentTime.date - startTime.date + numberOfDaysInStartMonth) % numberOfDaysInStartMonth >= 1 && diff / (60*60) >= 24)
        return "${(currentTime.date - startTime.date + numberOfDaysInStartMonth) % numberOfDaysInStartMonth} ngày trước"
    else if (diff / (60*60) >= 1)
        return "${diff / (60*60)} giờ trước"
    else
        return "${(diff % (60 * 60)) / (60)} phút trước"
}

fun numberOfDays(month: Int, year: Int): Int {
    val isLeapYear = (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)
    return when (month) {
        1 -> 31
        2 -> if (isLeapYear) 29 else 28
        3 -> 31
        4 -> 30
        5 -> 31
        6 -> 30
        7 -> 31
        8 -> 31
        9 -> 30
        10 -> 31
        11 -> 30
        12 -> 31
        else -> 30
    }
}

fun getFirstTag(video: VideoItem): String {
    val tags = video.videoSnippet.tags?.get(0)
    if (tags == null)
        return ""
    else
        return "#$tags"
}

fun reduceStringLength(string: String, length: Int): String {
    if (string.length > length)
        return "${string.substring(0,length)}..."
    else
        return string
}

fun getRandomComment(commentList: YoutubeVideoComment): CommentItem {
    val randomNumber = (0..commentList.items.size - 1).random()
    Log.i("ex_getRandomComment", "${commentList.items.size} $randomNumber")
    return commentList.items[randomNumber]
}

fun formatNumber(number: String): String {
    val formatter = NumberFormat.getNumberInstance(Locale.GERMANY)
    return formatter.format(number.toLong())
}

fun getGoogleSignInClient(context: Context): GoogleSignInClient {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()

    return GoogleSignIn.getClient(context, gso)
}













