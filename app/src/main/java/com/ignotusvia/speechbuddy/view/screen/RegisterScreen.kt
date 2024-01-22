package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.core.handleEvent
import com.ignotusvia.speechbuddy.view.component.ErrorDialog
import com.ignotusvia.speechbuddy.view.component.InputField
import com.ignotusvia.speechbuddy.view.component.LoadingBar
import com.ignotusvia.speechbuddy.viewmodel.RegistrationViewModel

@Composable
fun RegisterScreen() {
    val viewModel: RegistrationViewModel = hiltViewModel()
    val viewState: RegistrationViewModel.ViewState by viewModel.viewState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            onValueChange = {
                viewModel.onAction(RegistrationViewModel.Action.UpdateEmail(it))
            }, label = "Email",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        InputField(onValueChange = {
            viewModel.onAction(RegistrationViewModel.Action.UpdatePassword(it))
        }, label = "Password", modifier = Modifier.fillMaxWidth(), isPassword = true)

        Spacer(modifier = Modifier.height(8.dp))

        InputField(onValueChange = {
            viewModel.onAction(RegistrationViewModel.Action.UpdateConfirmPassword(it))
        }, label = "Confirm Password", modifier = Modifier.fillMaxWidth(), isPassword = true)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = viewState.isFormValid,
            onClick = {
                viewModel.onAction(RegistrationViewModel.Action.Register)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
    ConsumeEvent(viewState)
    if (viewState.isLoading) LoadingBar()
}

@Composable
private fun ConsumeEvent(viewState: RegistrationViewModel.ViewState) = with(viewState) {
    consumableEvent.handleEvent { event ->
        when (event) {
            is RegistrationViewModel.Event.Error -> ErrorDialog()
        }
    }
}