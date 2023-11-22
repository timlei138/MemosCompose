package com.lc.memos.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lc.memos.data.User
import com.lc.memos.data.db.MemosNote
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@Composable
fun MemosStat(user: User?,memosList: List<MemosNote>,tags: List<String>) {

    val days = remember(user, LocalDate.now()) {
        user?.let {
            ChronoUnit.DAYS.between(
                LocalDateTime.ofEpochSecond(
                    user.createdTs,
                    0,
                    OffsetDateTime.now().offset
                ).toLocalDate(), LocalDate.now()
            )
        } ?: 0
    }

    Row(
        Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = memosList.count().toString(), style = MaterialTheme.typography.headlineSmall)
            Text(text = "Memo", style = MaterialTheme.typography.labelMedium,color = MaterialTheme.colorScheme.outline)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = tags.count().toString(), style = MaterialTheme.typography.headlineSmall)
            Text(text = "Tag", style = MaterialTheme.typography.labelMedium,color = MaterialTheme.colorScheme.outline)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = days.toString(), style = MaterialTheme.typography.headlineSmall)
            Text(text = "Day", style = MaterialTheme.typography.labelMedium,color = MaterialTheme.colorScheme.outline)
        }

    }
}