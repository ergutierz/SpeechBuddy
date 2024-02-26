package com.ignotusvia.speechbuddy.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ignotusvia.speechbuddy.core.handleEvent
import com.ignotusvia.speechbuddy.view.component.ErrorDialog
import com.ignotusvia.speechbuddy.view.component.InputField
import com.ignotusvia.speechbuddy.view.component.LoadingBar
import com.ignotusvia.speechbuddy.viewmodel.LoginViewModel

@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = hiltViewModel()
    val viewState: LoginViewModel.ViewState by viewModel.viewState.collectAsState()
    viewModel.onAction(LoginViewModel.Action.Login)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        InputField(onValueChange = {
            viewModel.onAction(LoginViewModel.Action.UpdateEmail(it))
        }, label = "Email", modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(8.dp))

        InputField(onValueChange = {
            viewModel.onAction(LoginViewModel.Action.UpdatePassword(it))
        }, label = "Password", modifier = Modifier.fillMaxWidth(), isPassword = true)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = viewState.isFormValid,
            onClick = {
                viewModel.onAction(LoginViewModel.Action.Login)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

        TextButton(onClick = { /* Handle forgot password */ }) {
            Text("Forgot Password?")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?")
            TextButton(onClick = { viewModel.onAction(LoginViewModel.Action.NavigateToRegister) }) {
                Text("Sign Up", fontWeight = FontWeight.Bold)
            }
        }
    }
    ConsumeEvent(viewState)
    if (viewState.isLoading) LoadingBar()
}

@Composable
private fun ConsumeEvent(viewState: LoginViewModel.ViewState) = with(viewState) {
    consumableEvent.handleEvent { event ->
        when (event) {
            is LoginViewModel.Event.Error -> ErrorDialog()
        }
    }
}

