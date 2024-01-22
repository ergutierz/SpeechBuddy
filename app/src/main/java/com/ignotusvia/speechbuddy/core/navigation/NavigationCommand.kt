package com.ignotusvia.speechbuddy.core.navigation

import androidx.navigation.NamedNavArgument

/**
 * This class is meant to define the requirements
 * for a navigation event.
 */
interface NavigationCommand {

    val arguments: List<NamedNavArgument>

    val destination: String

}