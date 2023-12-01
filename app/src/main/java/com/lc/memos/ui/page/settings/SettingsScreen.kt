package com.lc.memos.ui.page.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NoAccounts
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.lc.memos.R
import com.lc.memos.data.Profile
import com.lc.memos.data.Status
import com.lc.memos.data.User
import com.lc.memos.ui.dpTopx
import com.lc.memos.ui.string
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.viewmodel.UserStateViewModel
import com.lc.memos.viewmodel.localUserModel
import com.lc.mini.call.suspendOnSuccess
import timber.log.Timber
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    user: User? = localUserModel.current.currUser,
    status: Status? = localUserModel.current.status,
    modifier: Modifier = Modifier

) {

    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        TopAppBar(title = { Text(text = R.string.navigate_setting.string()) }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }, scrollBehavior = scrollBehavior)
    }) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val painter = if (user?.avatarUrl.isNullOrEmpty()) {
                        rememberVectorPainter(Icons.Filled.AccountCircle)
                    } else {
                        rememberAsyncImagePainter(
                            model = ImageRequest.Builder(
                                LocalContext.current
                            ).data(user?.avatarIcon).size(dpTopx(64.dp))
                                .transformations(CircleCropTransformation()).build()
                        )

                    }

                    Image(
                        painter = painter,
                        modifier = modifier
                            .size(64.dp)
                            .clip(CircleShape),
                        contentDescription = "",
                    )
                    Column(modifier = modifier.padding(8.dp, 4.dp)) {
                        Text(
                            text = user?.username ?: "Please Login",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = status?.profile?.version ?: "",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewSettings() {
    MemosComposeTheme {
        SettingsScreen(
            onBack = { /*TODO*/ },
            user = User(username = "Stoneslc"),
            status = Status(Profile(version = "0.1.1"))
        )
    }

}