package com.lc.memos.data

import java.time.LocalDate

data class DailyUsageStat(val date: LocalDate,val count: Int = 0) {
    companion object{
        val initMatrix: List<DailyUsageStat> by lazy {
            val now = LocalDate.now()
            (1.. now.lengthOfYear()).map { day ->
                DailyUsageStat(date = now.minusDays(day - 1L))
            }.reversed()
        }
    }
}