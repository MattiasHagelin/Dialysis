package com.math3249.dialysis.authentication

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.identity.Identity
import com.math3249.dialysis.BaseApp
import com.math3249.dialysis.R
import com.math3249.dialysis.session.SessionCache
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun SignOut(
    onEvent: (StateFlow<Boolean>) -> Unit
) {
    val context = LocalContext.current
    val signOut = remember {
        MutableStateFlow(false)
    }
    LaunchedEffect(signOut.collectAsStateWithLifecycle().value) {
        if (signOut.value) {
            signOut(context, BaseApp.sessionCache)

        }
    }
    DropdownMenuItem(
        text = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.icon_sign_out),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(5.dp)
                )
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(5.dp)
                )
            }
        },
        onClick = {
            onEvent(signOut.asStateFlow())
        },
        modifier = Modifier
            .sizeIn(
                minWidth = 175.dp,
                maxWidth = 175.dp
            )
    )
}

private suspend fun signOut(
    context: Context,
    sessionCache: SessionCache
) {
    val googleAuthUiClient = GoogleAuthUiClient(context, Identity.getSignInClient(context))
    googleAuthUiClient.signOut()
    sessionCache.clearSession()
}