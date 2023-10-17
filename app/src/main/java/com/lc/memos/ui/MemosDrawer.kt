package com.lc.memos.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.lc.memos.R
import com.lc.memos.viewmodel.UserStateViewModel
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.ui.widget.dpTopx
import com.lc.memos.viewmodel.UserState
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
    viewModel: UserStateViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {

    val userState by viewModel.userState.collectAsStateWithLifecycle()

    ModalDrawerSheet(modifier) {

        DrawerHeader(userState)

        Spacer(modifier = Modifier.fillMaxWidth().height(1.dp).padding(0.dp,1.dp).background(MaterialTheme.colorScheme.secondary))
        
        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.navigate_home)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_home_24),
                    contentDescription = null
                )
            },
            selected = currentRoute == MemosDestinations.HOME_ROUTE,
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
            selected = currentRoute == MemosDestinations.EXPLORE_ROUTE,
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
            selected = currentRoute == MemosDestinations.RESOURCE_ROUTE,
            onClick = {
                navigateToResource()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        NavigationDrawerItem(
            label = { Text(text = stringResource(id = R.string.navigate_home)) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigate_collect_24),
                    contentDescription = null
                )
            },
            selected = currentRoute == MemosDestinations.COLLECT_ROUTE,
            onClick = {
                navigateToCollect()
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp))

        DrawerLabels()

        DrawerFooter()

    }
}

@Composable
private fun DrawerHeader(user: UserState? = null) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
    ) {

        val painter = if (user?.user?.avatarIcon?.isEmpty() == null) rememberVectorPainter(Icons.Filled.AccountCircle) else {
            rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalContext.current).data(user.user.avatarIcon)
                    .size(dpTopx(db = 64.dp)).transformations(CircleCropTransformation())
                    .build()
            )
        }

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
        )

        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(text = user?.user?.username ?: "userName")
            Text(
                text = user?.profile?.version ?: "unknown",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


@Composable
private fun DrawerLabels() {
    Box(modifier = Modifier.padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)) {
        Text(text = "label")
    }

}

@Composable
private fun DrawerFooter() {
    Box(
        modifier = Modifier
            .padding(start = 16.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Column() {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "5")
                    Text(text = "灵感")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "1")
                    Text(text = "便签")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "5")
                    Text(text = "天")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {

                Canvas(modifier = Modifier.fillMaxWidth(), onDraw = {
                    val radius = 20f
                    drawCircle(Color.Blue, radius = radius)
                })
            }
        }
    }
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
                currentRoute = MemosDestinations.HOME_ROUTE,
                navigateToHome = { /*TODO*/ },
                navigateToExplore = { /*TODO*/ },
                navigateToResource = { /*TODO*/ },
                navigateToCollect = { /*TODO*/ },
                navigateToSetting = { /*TODO*/ },
                closeDrawer = { /*TODO*/ })
        }
    }
}