package com.lc.memos.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lc.memos.R
import com.lc.memos.ui.component.MemosStat
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.viewmodel.MemosViewModel
import com.lc.memos.viewmodel.localMemosViewModel
import com.lc.memos.viewmodel.localUserState
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.roundToInt

@ExperimentalMaterial3Api
@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToExplore: () -> Unit,
    navigateToResource: () -> Unit,
    navigateToCollect: () -> Unit,
    navigateToSetting: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {

    val viewModel = localMemosViewModel.current.uiState.collectAsStateWithLifecycle()

    Timber.d("MemosViewModel=>1 $viewModel")

    ModalDrawerSheet(modifier) {

        MemosStat(localUserState.current.currUser, viewModel.value.items,viewModel.value.tags)

        DrawerCalendar(
            modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(10.dp, 5.dp, 10.dp, 5.dp)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .padding(0.dp, 1.dp)
                .background(MaterialTheme.colorScheme.secondary)
        )

        Text(
            text = "Memos",
            modifier.padding(16.dp),
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
        )

        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.navigate_home)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_home_24),
                    contentDescription = null
                )
            },
            selected = currentRoute == MemosDestinations.ROUTE_HOME,
            onClick = {
                navigateToHome()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.navigate_explore)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_explore_24),
                    contentDescription = null
                )
            },
            selected = currentRoute == MemosDestinations.ROUTE_EXPLORE,
            onClick = {
                navigateToExplore()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.navigate_resource)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_media_24),
                    contentDescription = null
                )
            },
            selected = currentRoute == MemosDestinations.ROUTE_RESOURCE,
            onClick = {
                navigateToResource()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.navigate_collect)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_collect_24),
                    contentDescription = null
                )
            },
            selected = currentRoute == MemosDestinations.ROUTE_COLLECT,
            onClick = {
                navigateToCollect()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.navigate_setting)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_settings_24),
                    contentDescription = null
                )
            },
            selected = currentRoute == MemosDestinations.ROUTE_SETTING,
            onClick = {
                navigateToSetting()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 10.dp)
                .fillMaxWidth()
                .background(Color.Gray)
                .height(1.dp)
        )


        Text(
            text = "Tags",
            modifier.padding(start = 16.dp, bottom = 12.dp),
            style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold)
        )

        DrawerLabels(viewModel.value.tags,modifier.padding(start = 16.dp, end = 16.dp))

    }
}

//@Composable
//private fun DrawerHeader(user: UserState? = null) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier.padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
//    ) {
//
//        val painter = if (user?.user?.avatarIcon?.isEmpty() == null) rememberVectorPainter(Icons.Filled.AccountCircle) else {
//            rememberAsyncImagePainter(
//                model = ImageRequest.Builder(LocalContext.current).data(user.user.avatarIcon)
//                    .size(dpTopx(db = 64.dp)).transformations(CircleCropTransformation())
//                    .build()
//            )
//        }
//
//        Image(
//            painter = painter,
//            contentDescription = null,
//            modifier = Modifier
//                .size(64.dp)
//                .clip(CircleShape)
//        )
//
//        Column(modifier = Modifier.padding(start = 12.dp)) {
//            Text(text = user?.user?.username ?: "userName")
//            Text(
//                text = user?.profile?.version ?: "unknown",
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//    }
//}


@Composable
private fun DrawerLabels(tags: List<String>,modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(tags){ tag ->
            Text(text = tag,modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp), style = TextStyle(fontSize = 18.sp))
        }
    }

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun DrawerCalendar(modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = modifier
            .drawWithContent {
                val itemPadding = 5.dp.value
                val calendar = Calendar.getInstance()
                val today = calendar.get(Calendar.DAY_OF_YEAR)
                calendar.add(Calendar.DAY_OF_YEAR, 7 - calendar.get(Calendar.DAY_OF_WEEK))

                Timber.d("first day ${calendar.firstDayOfWeek}, ${calendar.get(Calendar.DAY_OF_WEEK)},${calendar.timeInMillis}")

                val rectSize = size.height / 7 - itemPadding
                val maxColumns = (size.width / (rectSize + itemPadding)).roundToInt()

                calendar.add(Calendar.DAY_OF_YEAR, -((maxColumns - 2) * 7))

                var count = 0
                for (i in 0 until maxColumns) {
                    for (j in 0 until 7) {
                        count++
                        calendar.add(Calendar.DAY_OF_YEAR, 1)
                        Timber.d("day ${calendar.timeInMillis},${calendar.get(Calendar.DAY_OF_YEAR)},today $today ${calendar.time.toLocaleString()}")
                        if (i < maxColumns - 2) {
                            var style: DrawStyle = Stroke()
                            if (calendar.get(Calendar.DAY_OF_YEAR) == today) {
                                style = Fill
                            } else if (calendar.get(Calendar.DAY_OF_YEAR) > today) {

                            } else {

                            }

                            drawRoundRect(
                                Color.Blue,
                                Offset(
                                    (i * rectSize) + (i * itemPadding),
                                    (j * rectSize) + (j * itemPadding)
                                ),
                                Size(rectSize, rectSize),
                                CornerRadius(5.dp.value), style = style
                            )
                        } else if (i == maxColumns - 2 && j % 2 == 0) {
                            val text = SimpleDateFormat("EE").format(calendar.timeInMillis)
                            drawText(
                                textMeasurer,
                                text,
                                Offset(
                                    (i * rectSize) + (i * itemPadding),
                                    (j * rectSize) + (j * itemPadding)
                                ),
                                style = TextStyle(color = Color.Gray, fontSize = 12.sp),
                                maxLines = 1,
                            )
                        }
                    }
                }
            })
}


private fun isToday(stamp: Long, today: Long): Boolean {
    return true;
}

private fun isFutureDay(stamp: Long, today: Long): Boolean {
    return true
}


//@Composable
//private fun DrawerButton(
//    painter: Painter,
//    label: String,
//    isSelected: Boolean,
//    action: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//
//    val tintColor =
//        if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface.copy(
//            alpha = 0.6f
//        )
//
//    TextButton(
//        onClick = action, modifier = modifier
//            .fillMaxWidth()
//            .padding(
//                horizontal = dimensionResource(
//                    id = R.dimen.drawer_item_margin
//                )
//            )
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Icon(painter = painter, contentDescription = null, tint = tintColor)
//            Spacer(modifier = Modifier.width(16.dp))
//            Text(text = label, style = MaterialTheme.typography.labelMedium, color = tintColor)
//
//        }
//    }
//
//}

@ExperimentalMaterial3Api
@Preview
@Composable
fun PreviewAppDrawer() {
    MemosComposeTheme {
        Surface {
            AppDrawer(
                currentRoute = MemosDestinations.ROUTE_HOME,
                navigateToHome = { /*TODO*/ },
                navigateToExplore = { /*TODO*/ },
                navigateToResource = { /*TODO*/ },
                navigateToCollect = { /*TODO*/ },
                navigateToSetting = { /*TODO*/ },
                closeDrawer = { /*TODO*/ })
        }
    }
}