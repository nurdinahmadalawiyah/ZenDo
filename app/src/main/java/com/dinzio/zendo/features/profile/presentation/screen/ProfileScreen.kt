package com.dinzio.zendo.features.profile.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.dinzio.zendo.R
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.presentation.components.ZenDoConfirmDialog
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.features.auth.domain.model.UserModel
import com.dinzio.zendo.features.auth.presentation.component.triggerGoogleOneTap
import com.dinzio.zendo.features.auth.presentation.viewModel.AuthEvent
import com.dinzio.zendo.features.auth.presentation.viewModel.AuthViewModel
import com.dinzio.zendo.features.profile.presentation.component.ProfileContent
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val isLandscapeMode = isLandscape()
    val authState by authViewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val webClientId = stringResource(R.string.default_web_client_id)

    val onGoogleAction: () -> Unit = {
        scope.launch {
            triggerGoogleOneTap(
                context = context,
                webClientId = webClientId,
                onTokenReceived = { token ->
                    authViewModel.onEvent(AuthEvent.OnLinkGoogleAccount(token))
                },
                onError = { errorLog ->
                    Toast.makeText(context, errorLog, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        ZenDoConfirmDialog(
            title = stringResource(R.string.logout_from_account),
            message = stringResource(R.string.are_you_sure_you_want_to_log_out_you_ll_need_to_login_back_in_later_to_back_up_your_profile_data),
            confirmText = stringResource(R.string.logout),
            dismissText = stringResource(R.string.cancel),
            onConfirm = {
                showLogoutDialog = false
                authViewModel.onEvent(AuthEvent.OnSignOut)
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }

    if (isLandscapeMode) {
        ProfileTabletLayout(
            navController = navController,
            user = authState.user,
            onGoogleClick = onGoogleAction,
            onLogoutClick = { showLogoutDialog = true }
        )
    } else {
        ProfilePhoneLayout(
            navController = navController,
            user = authState.user,
            onGoogleClick = onGoogleAction,
            onLogoutClick = { showLogoutDialog = true }
        )
    }
}

@Composable
fun ProfilePhoneLayout(
    navController: NavController,
    user: UserModel?,
    onGoogleClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        ZenDoTopBar(
            title = stringResource(R.string.profile),
            isOnPrimaryBackground = true,
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProfileContent(
            user = user,
            onGoogleClick = onGoogleClick,
            onLogoutClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ProfileTabletLayout(
    navController: NavController,
    user: UserModel?,
    onGoogleClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        ZenDoTopBar(
            title = stringResource(R.string.profile),
            isOnPrimaryBackground = true,
            onBackClick = { navController.popBackStack() }
        )
        Spacer(modifier = Modifier.height(48.dp))

        ProfileContent(
            user = user,
            onGoogleClick = onGoogleClick,
            onLogoutClick = onLogoutClick,
            modifier = Modifier.widthIn(max = 500.dp)
        )
    }
}

@Preview(name = "Phone", showBackground = true, device = Devices.PIXEL_4)
@Composable
fun PreviewProfilePhone() {
    ProfilePhoneLayout(
        navController = rememberNavController(),
        user = null,
        onGoogleClick = {},
        onLogoutClick = {}
    )
}

@Preview(name = "Tablet", showBackground = true, device = Devices.PIXEL_C)
@Composable
fun PreviewProfileTablet() {
    ProfileTabletLayout(
        navController = rememberNavController(),
        user = null,
        onGoogleClick = {},
        onLogoutClick = {}
    )
}