package com.lc.memos.ui.component

import android.widget.ImageButton
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.lc.memos.R
import com.lc.memos.data.db.MemosNote
import com.lc.memos.ui.string
import com.lc.memos.ui.theme.MemosComposeTheme
import io.noties.markwon.Markwon
import io.noties.markwon.ext.tasklist.TaskListPlugin
import timber.log.Timber

@Composable
fun MemosInfoCard(
    note: MemosNote,
    head: @Composable () -> Unit,
    showActions: Boolean = true,
    modifier: Modifier = Modifier
) {

    Card(modifier = Modifier.padding(0.dp, 6.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
            head()
            Spacer(modifier = Modifier.weight(1f))
            if (showActions) {
                MemosInfoCardActions(note)
            }
        }

        Timber.d("text=>${note.content}")

        AndroidView(factory = { context ->
            TextView(context).apply {
                val markdown =
                    Markwon.builder(context).usePlugin(TaskListPlugin.create(context)).build()

                markdown.setMarkdown(this, note.content ?: "")
            }
        }, modifier = modifier)
    }

}


@Composable
fun MemosInfoCardActions(note: MemosNote, modifier: Modifier = Modifier) {

    var expandEd = remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { expandEd.value = true }) {
            Icon(
                Icons.Filled.MoreVert, contentDescription = "Action"
            )
        }

        DropdownMenu(expanded = expandEd.value, onDismissRequest = { expandEd.value = false }) {
            if (note.pinned) {
                DropDownItem(R.string.action_unpin.string(), Icons.Filled.PinDrop) {
                    expandEd.value = false
                }
            } else {
                DropDownItem(R.string.action_pin.string(), Icons.Filled.PushPin) {
                    expandEd.value = false
                }
            }

            DropDownItem(R.string.action_edit.string(), Icons.Filled.Edit) {
                expandEd.value = false
            }

            DropDownItem(R.string.action_share.string(), Icons.Filled.Share) {
                expandEd.value = false
            }

            DropDownItem(R.string.action_archive.string(), Icons.Filled.Archive) {
                expandEd.value = false
            }

            DropDownItem(
                R.string.action_delete.string(),
                Icons.Filled.Delete,
                color = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.error,
                    leadingIconColor = MaterialTheme.colorScheme.error
                )
            ) {
                expandEd.value = false
            }
        }
    }


}

@Composable
fun DropDownItem(
    text: String,
    icon: ImageVector,
    color: MenuItemColors = MenuDefaults.itemColors(),
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text = text, style = MaterialTheme.typography.labelMedium) },
        onClick = onClick,
        leadingIcon = {
            Icon(icon, contentDescription = "")
        }, colors = color
    )
}


@Composable
@Preview
fun PreviewMemosCard() {
    MemosComposeTheme {


        val info = MemosNote(content = "Test")

        MemosInfoCard(note = info, head = {

            Text(text = "1222")

        })

    }
}