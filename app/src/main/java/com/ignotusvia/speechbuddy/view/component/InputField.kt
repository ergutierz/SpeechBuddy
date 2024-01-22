package com.ignotusvia.speechbuddy.view.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun InputField(
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    val text = remember { mutableStateOf("") }
    val isPasswordVisible = remember { mutableStateOf(false) }
    OutlinedTextField(
        value = text.value,
        onValueChange = {
            text.value = it
            onValueChange(it)
        },
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        visualTransformation = if (isPassword && !isPasswordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                val image = if (isPasswordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (isPasswordVisible.value) "Hide password" else "Show password"
                IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors()
    )
}
