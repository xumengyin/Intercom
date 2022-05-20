package com.jerry.intercom.utils


fun Int.formatTime(): String {

    val hour = this / 60
    val minute = this % 60

    if (hour == 0 && minute == 0) {
        return "0分钟"
    }
    var data = ""
    if (hour > 0) {
        data = "${hour}小时"
    }
    if (minute > 0) {
        data += "${minute}分钟"
    }
    return data
}
