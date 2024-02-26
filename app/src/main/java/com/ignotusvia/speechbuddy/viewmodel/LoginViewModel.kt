package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ignotusvia.speechbuddy.core.ConsumableEvent
import com.ignotusvia.speechbuddy.core.ModelStore
import com.ignotusvia.speechbuddy.core.StateFlowModelStore
import com.ignotusvia.speechbuddy.core.navigation.NavigationCommandManager
import com.ignotusvia.speechbuddy.remote.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository,
    private val navigationCommandManager: NavigationCommandManager,
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
            is Action.Login -> performLogin()
            is Action.UpdateEmail -> updateEmail(action.email)
            is Action.UpdatePassword -> updatePassword(action.password)
            is Action.NavigateToRegister -> navigateToRegister()
        }
    }

    private fun updateEmail(email: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    email = email,
                    isFormValid = email.isNotEmpty() && oldState.password.isNotEmpty()
                )
            }
        }
    }

    private fun updatePassword(password: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    password = password,
                    isFormValid = password.isNotEmpty() && oldState.email.isNotEmpty()
                )
            }
        }
    }

    private fun navigateToRegister() {
        navigationCommandManager.navigate(NavigationCommandManager.registerDirection)
    }

    private fun performLogin() {
        setLoadingState(true)
        val email = "sample@mail.com"
        val password = "password1"
        authRepository.login(email, password) { firebaseUser: FirebaseUser?, exception: Exception? ->
            if (exception == null && firebaseUser != null) {
                navigateToDashboard()
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

    private fun navigateToDashboard() {
        navigationCommandManager.navigate(NavigationCommandManager.dashboardDirection)
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
        val isFormValid: Boolean = false,
        val consumableEvent: ConsumableEvent<Event> = ConsumableEvent(),
    )

    sealed interface Action {
        data class UpdateEmail(val email: String) : Action
        data class UpdatePassword(val password: String) : Action
        data object NavigateToRegister : Action
        data object Login : Action
    }

    sealed interface Event {
        data class Error(val message: String) : Event
    }
}
