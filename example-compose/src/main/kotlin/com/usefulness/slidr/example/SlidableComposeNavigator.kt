package com.usefulness.slidr.example

import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.get

fun NavGraphBuilder.slidableComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    addDestination(
        SlidableComposeNavigator.Destination(provider[SlidableComposeNavigator::class], content).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        },
    )
}

@Navigator.Name("slidableComposable")
class SlidableComposeNavigator : Navigator<SlidableComposeNavigator.Destination>() {

    /**
     * Get the map of transitions currently in progress from the [state].
     */
    internal val transitionsInProgress get() = state.transitionsInProgress

    /**
     * Get the back stack from the [state].
     */
    internal val backStack get() = state.backStack

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?,
    ) {
        entries.forEach { entry ->
            state.pushWithTransition(entry)
        }
    }

    override fun createDestination(): Destination {
        return Destination(this) { }
    }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        state.popWithTransition(popUpTo, savedState)
    }

    /**
     * Callback that removes the given [NavBackStackEntry] from the [map of the transitions in
     * progress][transitionsInProgress]. This should be called in conjunction with [navigate] and
     * [popBackStack] as those call are responsible for adding entries to [transitionsInProgress].
     *
     * Failing to call this method could result in entries being prevented from reaching their
     * final [Lifecycle.State]}.
     */
    internal fun onTransitionComplete(entry: NavBackStackEntry) {
        state.markTransitionComplete(entry)
    }

    /**
     * NavDestination specific to [ComposeNavigator]
     */
    @NavDestination.ClassType(Composable::class)
    class Destination(
        navigator: SlidableComposeNavigator,
        val content: @Composable (NavBackStackEntry) -> Unit,
    ) : NavDestination(navigator)

    internal companion object {
        internal const val NAME = "slidableComposable"
    }
}
