package com.lc.memos.ui.page.resource

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.compose.AsyncImage
import com.lc.memos.R
import com.lc.memos.data.api.Resource
import com.lc.memos.ui.component.LoadingContent
import com.lc.memos.ui.string
import com.lc.memos.ui.theme.MemosComposeTheme
import com.lc.memos.viewmodel.ResourceViewModel
import com.lc.memos.viewmodel.localUserModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ResourceScreen(
    modifier: Modifier = Modifier,
    viewModel: ResourceViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Timber.d("data size=>${uiState.value.data.size}")

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = R.string.navigate_resource.string()) }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->

        ResourceContent(
            isLoading = uiState.value.isLoading,
            data = uiState.value.data,
            modifier = modifier.padding(paddingValues)
        )

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResourceContent(
    isLoading: Boolean,
    data: List<Resource>,
    modifier: Modifier = Modifier
) {

    LoadingContent(
        loading = isLoading,
        empty = data.isNullOrEmpty(),
        emptyContent = { /*TODO*/ },
        onRefresh = { /*TODO*/ }) {


        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalItemSpacing = 10.dp,
        ) {

            items(data, key = { it.id }) {
                val url = it.toFileUri(localUserModel.current.host).toString()
                //Timber.d("resource=>$url")
                AsyncImage(
                    model = url,
                    imageLoader = ImageLoader.Builder(LocalContext.current).okHttpClient(
                        localUserModel.current.okHttpClient).build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "",
                    onError = {
                        Timber.d("onError ${it.result.throwable}")
                    }
                )
            }

        }
    }
}

@Preview
@Composable
fun PreviewResourceList() {
    MemosComposeTheme {

        /**
         * {
         *     "id": 5,
         *     "creatorId": 1,
         *     "createdTs": 1701136635,
         *     "updatedTs": 1701136635,
         *     "filename": "pi.jpg",
         *     "externalLink": "",
         *     "type": "image/jpeg",
         *     "size": 200870
         *   },
         */

        val data = Resource(
            id = 5,
            creatorId = 1,
            createdTs = 1701136635,
            updatedTs = 1701136635,
            filename = "pi.jpg",
            externalLink = "",
            type = "image/jpeg",
            size = 200807,
            linkedMemoAmount = 0
        )

        ResourceContent(isLoading = false, data = arrayListOf<Resource>(data))
    }
}