package com.lc.memos.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lc.memos.R
import com.lc.memos.ui.widget.MemoSnackBarHost
import com.lc.memos.viewmodel.UserStateViewModel
import com.lc.memos.viewmodel.SignMethod


private var host by mutableStateOf("http://82.156.120.42:8090")
private var userName by mutableStateOf("stoneslc")
private var pwd by mutableStateOf("Kotlin1991&")
private var token by mutableStateOf("")
private var loginMethod by mutableStateOf(SignMethod.USERNAME_AND_PASSWORD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSigned: () -> Unit,
    viewModel: UserStateViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    var menuExpanded by remember { mutableStateOf(false) }

    var snackBarHostState = remember { SnackbarHostState() }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .imePadding(), snackbarHost = {
        MemoSnackBarHost(snackBarHostState)
    }, bottomBar = {
        BottomAppBar(actions = {
            Box() {
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    properties = PopupProperties(focusable = false)
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.login_with_username)) },
                        onClick = {
                            loginMethod = SignMethod.USERNAME_AND_PASSWORD;menuExpanded = false
                        }, trailingIcon = {
                            if (loginMethod == SignMethod.USERNAME_AND_PASSWORD)
                                Icon(Icons.Rounded.Check, contentDescription = "")
                        })
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.login_with_token)) },
                        onClick = { loginMethod = SignMethod.OPEN_API;menuExpanded = false },
                        trailingIcon = {
                            if (loginMethod == SignMethod.OPEN_API)
                                Icon(Icons.Rounded.Check, contentDescription = "")
                        })
                }
                TextButton(onClick = { menuExpanded = true }) {
                    Text(text = stringResource(id = R.string.login_changed_type))
                }
            }
        }, floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.signIn(host, userName, pwd) },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            ) {
                Icon(
                    Icons.Rounded.ExitToApp,
                    contentDescription = stringResource(id = R.string.login)
                )
                Text(text = stringResource(id = R.string.login))
            }
        })

    }) { paddingValues ->

        val uiState by viewModel.loginUiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState, scaffoldState) {
            if (uiState.msg?.isNotEmpty() == true)
                snackBarHostState.showSnackbar(uiState.msg!!)
            if (uiState.user != null)
                onSigned.invoke()
        }

        LoginContent(
            loading = uiState.loading,
            modifier = Modifier.padding(paddingValues)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    loading: Boolean,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {

        if (loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .width(5.dp)
            )
        }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Memos Android Client",
                modifier = Modifier
                    .fillMaxWidth(), textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = host,
                onValueChange = { host = it },
                label = { Text(text = stringResource(id = R.string.login_host)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_host),
                        contentDescription = ""
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next
                )
            )

            if (loginMethod == SignMethod.USERNAME_AND_PASSWORD) {

                OutlinedTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    label = { Text(text = stringResource(R.string.login_username)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_user),
                            contentDescription = ""
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )

                OutlinedTextField(
                    value = pwd,
                    onValueChange = { pwd = it },
                    label = { Text(text = stringResource(id = R.string.login_pwd)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_password),
                            contentDescription = ""
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(onGo = {})
                )
            } else {
                OutlinedTextField(
                    value = token,
                    onValueChange = { token = it },
                    label = { Text(text = stringResource(id = R.string.login_token)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_host),
                            contentDescription = ""
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(onGo = {})
                )
            }
        }
    }
}