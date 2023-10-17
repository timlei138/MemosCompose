package com.lc.memos.util

import android.icu.text.DateFormat


fun Int.getTimeStampFormat() = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(this)