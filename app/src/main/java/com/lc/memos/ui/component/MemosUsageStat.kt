package com.lc.memos.ui.component

import android.app.usage.UsageStats
import android.icu.util.LocaleData
import android.util.ArrayMap
import androidx.annotation.IntRange
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText

import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lc.memos.data.db.MemosNote
import timber.log.Timber
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalTextApi::class)
@Composable
fun MemosUsageStat(notes: List<MemosNote>, modifier: Modifier) {


    val textMeasurer = rememberTextMeasurer()

    val countMap = ArrayMap<LocalDate, Int>()

    for (info in notes) {
        val date = LocalDateTime.ofEpochSecond(info.createdTs, 0, OffsetDateTime.now().offset)
            .toLocalDate()
        countMap[date] = (countMap[date] ?: 0) + 1
    }

    val weekDays = DateFormatSymbols.getInstance().shortWeekdays


    Box(modifier = modifier
        .fillMaxWidth()
        .drawWithContent {
            val itemPadding = 5.dp.value
            val cellSize = size.height / 7 - itemPadding
            val maxColumns = (size.width / (cellSize + itemPadding)).roundToInt()
            val dayUsages = DailyUsageStat.getUsageDaily((maxColumns - 2) * 7, countMap)

            var index = 0
            for (column in 0 until maxColumns) {
                for (row in 0 until 7) {
                    val dayUsage = if (index >= dayUsages.size) DailyUsageStat(
                        LocalDate
                            .now()
                            .plusDays(index.toLong() - dayUsages.size)
                    ) else dayUsages[index]
                    if (column < maxColumns - 2) {
                        val color = when (dayUsage.count) {
                            0 -> Color(0xffeaeaea)
                            1 -> Color(0xff9be9a8)
                            2 -> Color(0xff40c463)
                            in 3..5 -> Color(0xff30a14e)
                            else -> Color(0xff216e39)
                        }

                        if (dayUsage.date.isAfter(LocalDate.now())) continue

                        drawRoundRect(color,
                            Offset((column * cellSize) + (column * itemPadding), (row * cellSize) + (row * itemPadding)),
                            Size(cellSize, cellSize),
                            CornerRadius(5.dp.value),
                            style = Fill
                        )
                    } else if (column == maxColumns - 2 && row % 2 == 0) {
                        drawText(
                            textMeasurer,
                            weekDays[row + 1],
                            Offset(
                                (column * cellSize) + (column * itemPadding),
                                (row * cellSize) + (row * itemPadding)
                            ),
                            style = TextStyle(color = Color.Gray, fontSize = 11.sp),
                            maxLines = 1,
                        )
                    }
                    index++
                }
            }

        })
}


data class DailyUsageStat(val date: LocalDate, val count: Int = 0) {
    companion object {
        fun getUsageDaily(
            @IntRange(0, 366) days: Int,
            countMap: ArrayMap<LocalDate, Int>
        ): List<DailyUsageStat> {
            var now = LocalDate.now()
            val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
            val firstDayOfThisWeek =
                LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
            val pastDays = ChronoUnit.DAYS.between(firstDayOfThisWeek, LocalDate.now())
            if (pastDays != 0L) {
                now = now.plusDays(6 - pastDays)
            }
            return (0 until days.toLong()).map { day ->
                val d = now.minusDays(day)
                DailyUsageStat(date = d, count = countMap.get(d) ?: 0)
            }.reversed()

        }

        val initMatrix: List<DailyUsageStat> by lazy {
            val now = LocalDate.now()
            (1..now.lengthOfYear()).map { day ->
                DailyUsageStat(date = now.minusDays(day - 1L))
            }.reversed()
        }
    }
}