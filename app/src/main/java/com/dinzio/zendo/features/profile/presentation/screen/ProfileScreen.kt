package com.dinzio.zendo.features.profile.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons as MaterialIcons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.dinzio.zendo.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dinzio.zendo.core.presentation.components.ZenDoTopBar
import com.dinzio.zendo.core.presentation.components.ZenDoButton
import com.dinzio.zendo.core.presentation.components.ZenDoConfirmDialog
import com.dinzio.zendo.core.util.isLandscape
import com.dinzio.zendo.features.auth.domain.model.UserModel
import coil.compose.AsyncImage
import com.dinzio.zendo.features.auth.presentation.component.triggerGoogleOneTap
import com.dinzio.zendo.features.auth.presentation.viewModel.AuthEvent
import com.dinzio.zendo.features.auth.presentation.viewModel.AuthViewModel
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

    // Dialog state
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

@Composable
fun ProfileContent(
    user: UserModel?,
    onGoogleClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(
            targetState = (user == null || user.isAnonymous),
            animationSpec = tween(500),
            label = "ProfileStateTransition"
        ) { isGuest ->
            if (isGuest) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary
                                    )
                                )
                            )
                            .padding(vertical = 40.dp, horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(MaterialTheme.colorScheme.onPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                MaterialIcons.Rounded.Person,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            stringResource(R.string.join_with_zendo),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            stringResource(R.string.leave_the_guest_account_behind_sign_in_with_your_google_account_now_to_start_building_better_focus_habits),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        ZenDoButton(
                            text = stringResource(R.string.continue_with_google),
                            onClick = onGoogleClick,
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    )
                                )
                                .padding(vertical = 40.dp, horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val initial = user?.displayName?.firstOrNull()?.toString() ?: "Z"
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .background(MaterialTheme.colorScheme.onPrimary, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (user?.photoUrl != null) {
                                    AsyncImage(
                                        model = user.photoUrl,
                                        contentDescription = stringResource(R.string.profile_picture),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(
                                        initial.uppercase(),
                                        style = MaterialTheme.typography.displaySmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                user?.displayName ?: "User",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                user?.email ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(32.dp))

                            Row(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(MaterialIcons.Rounded.VerifiedUser, null, tint = MaterialTheme.colorScheme.onPrimary)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    stringResource(R.string.linked_google_account),
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedButton(
                        onClick = onLogoutClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .height(46.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(MaterialIcons.AutoMirrored.Rounded.Logout, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(stringResource(R.string.log_out_of_account), fontSize = 16.sp)
                    }
                }
            }
        }
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