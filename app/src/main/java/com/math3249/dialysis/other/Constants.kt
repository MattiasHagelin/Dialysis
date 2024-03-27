package com.math3249.dialysis.other

object Constants {

    const val MEDICATION_CHANNEL_ID = "medication_notification_channel"
    const val DATABASE_BASE_URL = "https://dialysis-8dae4-default-rtdb.europe-west1.firebasedatabase.app/"
    const val TABLE_DIALYSIS_ENTRIES = "dialysis_entries"
    const val TABLE_FLUID_BALANCE = "fluid_balance"
    const val TABLE_FLUID_BALANCE_HISTORY = "fluid_balance_history"
    const val TABLE_GROUP_MEMBER = "group_member"
    const val TABLE_GROUP = "group"
    const val TABLE_MEDICATIONS = "medications"
    const val TABLE_MEMBER_GROUP = "member_group"
    const val SESSION = "session"

    //Date and Time pattern
    const val DATE_PATTERN = "dd-MM-yyyy"
    const val TIME_24_H = "HH:mm"
    const val TIME_12_H = "hh:mm"

    //Navigation locations
    const val DIALYSIS = "dialysis"
    const val MEDICATION_LIST = "medication_list"
    const val FLUID_BALANCE = "fluid_balance"
    const val SETTINGS = "settings"

    //Color
    const val YELLOW = "#FFFF00"
    const val GREEN = "#008000"


    //Intent extras
    const val TITLE = "TITLE"
    const val TEXT = "TEXT"
    const val SMALL_ICON = "SMALL_ICON"
    const val ID = "ID"


    //Log tags
    const val DIALYSIS_FIREBASE = "dialysis_firebase"
}
