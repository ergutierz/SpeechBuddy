package com.ignotusvia.speechbuddy.core.navigation

import androidx.navigation.NamedNavArgument
import com.ignotusvia.speechbuddy.core.ModelStore
import com.ignotusvia.speechbuddy.core.Reducer
import com.ignotusvia.speechbuddy.core.StateFlowModelStore
import com.ignotusvia.speechbuddy.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationCommandManager @Inject constructor(
    @ApplicationScope private val coroutineScope: CoroutineScope,
) : CoroutineScope by coroutineScope {

    private val commandStore: ModelStore<NavigationCommand> = StateFlowModelStore(defaultDirection, this)
    val commands = commandStore.modelState()

    fun navigate(directions: NavigationCommand) {
        launch {
            commandStore.process(NavigationCommandReducer(directions))
        }
    }

    fun clearNavigationCommand() {
        launch {
            commandStore.process(NavigationCommandReducer())
        }
    }

    class NavigationCommandReducer(
        private val command: NavigationCommand = defaultDirection
    ) : Reducer<NavigationCommand>() {
        override fun reduce(oldState: NavigationCommand): NavigationCommand {
            return command
        }

    }

    companion object {
        val defaultDirection = object : NavigationCommand {
            override val arguments: List<NamedNavArgument>
                get() = emptyList()
            override val destination: String
                get() = ""
        }
    }
}
