package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ignotusvia.speechbuddy.core.ConsumableEvent
import com.ignotusvia.speechbuddy.core.ModelStore
import com.ignotusvia.speechbuddy.core.StateFlowModelStore
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import com.ignotusvia.speechbuddy.manager.FirebaseAuthenticationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val authManager: FirebaseAuthenticationManager,
    private val navigationCommandManager: NavigationCommandManager
) : ViewModel() {

    private val _modelStore: ModelStore<ViewState> =
        StateFlowModelStore(ViewState(), viewModelScope)
    val viewState: StateFlow<ViewState>
        get() = _modelStore.modelState().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState()
        )

    fun onAction(action: Action) {
        when (action) {
            is Action.Register -> performRegistration()
            is Action.UpdateEmail -> updateEmail(action.email)
            is Action.UpdatePassword -> updatePassword(action.password)
            is Action.UpdateConfirmPassword -> updateConfirmPassword(action.confirmPassword)
        }
    }

    private fun performRegistration() {
        setLoadingState(true)
        val email = _modelStore.value.email
        val password = _modelStore.value.password
        authManager.signUp(email, password) { firebaseUser: FirebaseUser?, exception: Exception? ->
            if (exception == null && firebaseUser != null) {
                navigateToLogin()
            } else {
                viewModelScope.launch {
                    _modelStore.process { oldState ->
                        oldState.copy(
                            isLoading = false,
                            consumableEvent = ConsumableEvent.create(
                                Event.Error(exception?.message ?: "Unknown error")
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateEmail(email: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    email = email,
                    isFormValid = validateInput(email, oldState.password, oldState.confirmPassword)
                )
            }
        }
    }

    private fun updatePassword(password: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    password = password,
                    isFormValid = validateInput(oldState.email, password, oldState.confirmPassword)
                )
            }
        }
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    confirmPassword = confirmPassword,
                    isFormValid = validateInput(oldState.email, oldState.password, confirmPassword)
                )
            }
        }
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword
    }

    private fun navigateToLogin() {
        navigationCommandManager.navigate(NavigationCommandManager.loginDirection)
    }

    private fun setLoadingState(isLoading: Boolean) {
        viewModelScope.launch {
            _modelStore.process { oldState -> oldState.copy(isLoading = isLoading) }
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val isFormValid: Boolean = false,
        val consumableEvent: ConsumableEvent<Event> = ConsumableEvent()
    )

    sealed interface Action {
        data class UpdateEmail(val email: String) : Action
        data class UpdatePassword(val password: String) : Action
        data class UpdateConfirmPassword(val confirmPassword: String) : Action
        data object Register : Action
    }

    sealed interface Event {
        data class Error(val message: String) : Event
    }
}
