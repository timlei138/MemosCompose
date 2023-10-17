package com.lc.memos.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.lc.memos.viewmodel.UserStateViewModel


@ExperimentalMaterial3Api
@Composable
fun SettingScreen(signOut: () -> Unit, model: UserStateViewModel = hiltViewModel()) {

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            OutlinedButton(onClick = { /*TODO*/ }) {

            }

        }
    }
}