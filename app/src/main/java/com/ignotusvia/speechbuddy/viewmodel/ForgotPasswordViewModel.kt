package com.ignotusvia.speechbuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ignotusvia.speechbuddy.core.ConsumableEvent
import com.ignotusvia.speechbuddy.core.ModelStore
import com.ignotusvia.speechbuddy.core.StateFlowModelStore
import com.ignotusvia.speechbuddy.remote.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _modelStore: ModelStore<ViewState> =
        StateFlowModelStore(ViewState(), viewModelScope)
    val viewState: StateFlow<ViewState> = _modelStore.modelState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ViewState()
    )

    fun onAction(action: Action) {
        when (action) {
            is Action.UpdateEmail -> updateEmail(action.email)
            is Action.SendPasswordReset -> sendPasswordReset()
        }
    }

    private fun updateEmail(email: String) {
        viewModelScope.launch {
            _modelStore.process { oldState ->
                oldState.copy(
                    email = email,
                    isEmailValid = email.isNotBlank()
                )
            }
        }
    }

    private fun sendPasswordReset() {
        setLoadingState(true)
        val email = _modelStore.value.email
        viewModelScope.launch {
            authRepository.sendPasswordReset(email) { exception ->
                viewModelScope.launch {
                    _modelStore.process { oldState ->
                        oldState.copy(
                            isLoading = false,
                            isResetSuccess = exception == null,
                            consumableEvent = if (exception != null)
                                ConsumableEvent.create(Event.Error(exception.message ?: "Error sending reset email"))
                            else ConsumableEvent()
                        )
                    }
                }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        viewModelScope.launch {
            _modelStore.process { oldState -> oldState.copy(isLoading = isLoading) }
        }
    }

    data class ViewState(
        val isLoading: Boolean = false,
        val email: String = "",
        val isEmailValid: Boolean = false,
        val isResetSuccess: Boolean = false,
        val consumableEvent: ConsumableEvent<Event> = ConsumableEvent()
    )

    sealed interface Action {
        data class UpdateEmail(val email: String) : Action
        data object SendPasswordReset : Action
    }

    sealed interface Event {
        data class Error(val message: String) : Event
    }
}
