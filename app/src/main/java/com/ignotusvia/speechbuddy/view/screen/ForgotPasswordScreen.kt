package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.core.handleEvent
import com.ignotusvia.speechbuddy.view.component.ErrorDialog
import com.ignotusvia.speechbuddy.view.component.InputField
import com.ignotusvia.speechbuddy.viewmodel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordScreen() {
    val viewModel: ForgotPasswordViewModel = hiltViewModel()
    val viewState: ForgotPasswordViewModel.ViewState by viewModel.viewState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            onValueChange = { viewModel.onAction(ForgotPasswordViewModel.Action.UpdateEmail(it)) },
            label = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = viewState.isEmailValid,
            onClick = { viewModel.onAction(ForgotPasswordViewModel.Action.SendPasswordReset)  },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Reset Email")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewState.isResetSuccess) {
            Text("Password reset email sent! Check your inbox.")
        }
    }
    ConsumeEvent(viewState)
    if (viewState.isLoading) LoadingBar()
}

@Composable
private fun ConsumeEvent(viewState: ForgotPasswordViewModel.ViewState) = with(viewState) {
    consumableEvent.handleEvent { event ->
        when (event) {
            is ForgotPasswordViewModel.Event.Error -> ErrorDialog()
        }
    }
}

@Composable
private fun LoadingBar() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        CircularProgressIndicator()
    }
}