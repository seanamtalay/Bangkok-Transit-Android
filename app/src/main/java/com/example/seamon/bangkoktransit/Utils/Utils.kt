package com.example.seamon.bangkoktransit.Utils

import android.content.Context
import android.content.res.Resources
import com.example.seamon.bangkoktransit.R

import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*


fun getEta(context: Context, station: String): String{
    val currentTime = getCurrentTime()
    val currentTimeHour = getCurrentTimeHour().toInt()
    val currentTimeMinute  = getCurrentTimeMinute().toInt()
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    if (getTrainType(context, station).equals(context.resources.getString(R.string.BTS_Sukhumvit))){
        if(!currentDay.equals(Calendar.SATURDAY) && !currentDay.equals(Calendar.SUNDAY)) {
            //Weekdays
            if (currentTimeHour == 6) {
                return "Every 5:00 min"
            }
            else if (currentTimeHour in 7..8 || (currentTimeHour == 9 && currentTimeMinute in 0..30)) {
                return "Every 2:30 – 3:00 min"
            }
            else if ((currentTimeHour == 9 && currentTimeMinute in 31..59) ||
                    currentTimeHour in 10..14 ||
                    (currentTimeHour == 15 && currentTimeMinute in 0..30)) {
                return "Every 5:30 – 6:00 min"
            }
            else if ((currentTimeHour == 15 && currentTimeMinute in 31..59) ||
                    currentTimeHour in 16..19) {
                return "Every 2:40 – 4:00 min"
            }
            else if (currentTimeHour == 20) {
                return "Every 5:00 min"
            }
            else if (currentTimeHour == 21) {
                return "Every 6:00 min"
            }
            else if (currentTimeHour in 22..23) {
                return "Every 8:00 min"
            }
        }
        else{
            //Weekends
            if (currentTimeHour in 6..11) {
                return "Every 5:55 min"
            }
            else if (currentTimeHour in 11..13) {
                return "Every 4:20 – 5:00 min"
            }
            else if (currentTimeHour in 14..18) {
                return "Every 4:00 – 5:30 min"
            }
            else if (currentTimeHour in 19..20) {
                return "Every 5:00 – 5:55 min"
            }
            else if (currentTimeHour == 21) {
                return "Every 7:00 min"
            }
            else if (currentTimeHour in 22..23) {
                return "Every 8:00 min"
            }
        }
    }

    if (getTrainType(context, station).equals(context.resources.getString(R.string.BTS_Silom))){
        if(!currentDay.equals(Calendar.SATURDAY) && !currentDay.equals(Calendar.SUNDAY)) {
            //Weekdays
            if (currentTimeHour == 6) {
                return "Every 6:00 min"
            }
            else if (currentTimeHour in 7..8) {
                return "Every 4:50 min"
            }
            else if (currentTimeHour in 9..16) {
                return "Every 6:00 min"
            }
            else if (currentTimeHour in 17..19) {
                return "Every 4:50 min"
            }
            else if (currentTimeHour in 20..21) {
                return "Every 6:00 min"
            }
            else if (currentTimeHour in 22..23) {
                return "Every 8:00 min"
            }
        }
        else{
            //Weekends
            if (currentTimeHour in 6..21) {
                return "Every 6:00 min"
            }
            else if (currentTimeHour in 22..23) {
                return "Every 8:00 min"
            }
        }
    }

    if (getTrainType(context, station).equals(context.resources.getString(R.string.ARL))){
        if(!currentDay.equals(Calendar.SATURDAY) && !currentDay.equals(Calendar.SUNDAY)) {
            //Weekdays
            if ((currentTimeHour == 5 && currentTimeMinute in 31..59)||
                    currentTimeHour in 6..23) {
                return "Every 10 – 15 min"
            }
        }
        else{
            //Weekends
            if ((currentTimeHour == 5 && currentTimeMinute in 31..59)||
                    currentTimeHour in 6..23) {
                return "Every 12 – 15 min"
            }
        }
    }

    if (getTrainType(context, station).equals(context.resources.getString(R.string.MRT_purple))||
            getTrainType(context, station).equals(context.resources.getString(R.string.MRT_blue))){
        if ((currentTimeHour in 6..8)) {
            return "Every 5:00 min"
        }
        else if (currentTimeHour in 9..15 ||
                (currentTimeHour == 16 && currentTimeMinute in 0..30)) {
            return "Every 10:00 min"
        }
        else if ((currentTimeHour == 16 && currentTimeMinute in 31..59) ||
                currentTimeHour in 17..18 ||
                (currentTimeHour == 19 && currentTimeMinute in 0..30)) {
            return "Every 5:00 min"
        }
        else if ((currentTimeHour == 19 && currentTimeMinute in 31..59) ||
                currentTimeHour in 20..23) {
            return "Every 10:00 min"
        }

    }

    //else
    return "N/A"

}

fun getTrainType(context: Context, station: String): String{
    val BtsSilomArray = context.resources.getStringArray(R.array.stations_BTS_Silom_array)
    val BtsSukArray = context.resources.getStringArray(R.array.stations_BTS_Sukhumvit_array)
    val ArlArray = context.resources.getStringArray(R.array.stations_ARL_array)
    val MrtBlueArray = context.resources.getStringArray(R.array.stations_MRT_Blue_array)
    val MrtPurpleArray = context.resources.getStringArray(R.array.stations_MRT_Purple_array)

    if(BtsSukArray.contains(station)){
        return context.resources.getString(R.string.BTS_Sukhumvit)
    }
    else if(BtsSilomArray.contains(station)){
        return context.resources.getString(R.string.BTS_Silom)
    }
    else if(ArlArray.contains(station)){
        return context.resources.getString(R.string.ARL)
    }
    else if(MrtBlueArray.contains(station)){
        return context.resources.getString(R.string.MRT_blue)
    }
    else /*(MrtPurpleArray.contains(station))*/{
        return context.resources.getString(R.string.MRT_purple)
    }

}

fun getCurrentTime(): String{
    val date = Date()
    val strDateFormat = "HH:mm"
    val dateFormat = SimpleDateFormat(strDateFormat)
    val formattedDate = dateFormat.format(date)

    return formattedDate
}

fun getCurrentTimeHour(): String{
    val date = Date()
    val strDateFormat = "HH"
    val dateFormat = SimpleDateFormat(strDateFormat)
    val formattedDate = dateFormat.format(date)

    return formattedDate
}

fun getCurrentTimeMinute(): String{
    val date = Date()
    val strDateFormat = "mm"
    val dateFormat = SimpleDateFormat(strDateFormat)
    val formattedDate = dateFormat.format(date)

    return formattedDate
}

fun getStationLat(context: Context, stationName: String): Double {
    //get Lat & Lng from the second station name
    //Get the station code
    val splitedName = stationName.split("\\s+".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
    val stationCode = splitedName[0]

    //get the station info based on the stationCode
    val resId = context.resources.getIdentifier(stationCode, "array", context.packageName) // might cause a bug (return 0)
    val stringArray = context.resources.getStringArray(resId)
    val destinationLatLng = stringArray[4]

    val latLng = destinationLatLng.split(", ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
    return java.lang.Double.parseDouble(latLng[0])

}

fun getStationLng(context: Context, stationName: String): Double {
    //get Lat & Lng from the second station name
    //Get the station code
    val splitedName = stationName.split("\\s+".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
    val stationCode = splitedName[0]

    //get the station info based on the stationCode
    val resId = context.resources.getIdentifier(stationCode, "array", context.packageName) // might cause a bug (return 0)
    val stringArray = context.resources.getStringArray(resId)
    val destinationLatLng = stringArray[4]

    val latLng = destinationLatLng.split(", ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
    return java.lang.Double.parseDouble(latLng[1])

}