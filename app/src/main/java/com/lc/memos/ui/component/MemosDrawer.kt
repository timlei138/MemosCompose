package com.lc.memos.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lc.memos.R
import com.lc.memos.ui.MemosDestinations
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.viewmodel.localMemosViewModel
import com.lc.memos.viewmodel.localUserModel
import timber.log.Timber

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

        MemosStat(localUserModel.current.currUser, viewModel.value.items, viewModel.value.tags)

//        DrawerCalendar(
//            modifier
//                .fillMaxWidth()
//                .height(120.dp)
//                .padding(10.dp, 5.dp, 10.dp, 5.dp)
//        )
        if (viewModel.value.items.isNotEmpty()) {
            MemosUsageStat(
                viewModel.value.items, modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(10.dp, 5.dp, 10.dp, 5.dp)
            )
        }

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

        DrawerLabels(viewModel.value.tags, modifier.padding(start = 16.dp, end = 16.dp))

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DrawerLabels(tags: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {

        items(tags) { tag ->
            NavigationDrawerItem(label = { Text(text = tag) }, icon = {
                Icon(Icons.Outlined.Tag, contentDescription = "TAG")
            }, onClick = {}, selected = false)

        }
    }

}

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