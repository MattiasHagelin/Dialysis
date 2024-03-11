package com.math3249.dialysis.authentication.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.identity.Identity
import com.math3249.dialysis.R
import com.math3249.dialysis.authentication.GoogleAuthUiClient
import com.math3249.dialysis.authentication.SignInEvent
import com.math3249.dialysis.authentication.SignInState
import com.math3249.dialysis.ui.components.DialysisAppBar

@Composable
fun FirstTimeSignInScreen(
    state: SignInState,
    modifier: Modifier = Modifier,
    onEvent: (SignInEvent) -> Unit = {}
) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        DialysisAppBar(
            canNavigateBack = false,
            navigateUp = { /*TODO*/ },
            title = stringResource(R.string.create_required_data),
            saveAction = {
                val context = LocalContext.current
                val googleAuthUiClient by lazy {
                    GoogleAuthUiClient(
                        context = context,
                        oneTapClient = Identity.getSignInClient(context)
                    )
                }
                IconButton(
                    onClick = {
                        onEvent(SignInEvent.CreateRequiredData(googleAuthUiClient))
                    }
                ) {
                    Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = null)
                }
            }
        )
        TextField (
            value = state.shareDataToken,
            label = {
                Text(stringResource(R.string.share_data_token))
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background
            ),
            onValueChange = {
                onEvent(SignInEvent.UpdateShareDataToken(it))
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
        )
        Text(
            text = stringResource(R.string.share_data_token_info),
            fontSize = 10.sp,
            modifier = modifier
                .padding(horizontal = 20.dp)
        )
    }
}
@Composable
@Preview
fun FirstTimeSignInScreenPreview() {
    FirstTimeSignInScreen(
        state = SignInState(
            shareDataToken = "123abc"
        )
    )
}