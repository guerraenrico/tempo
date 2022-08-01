package com.enricog.ui.components.bottomSheet.navigator

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.FloatingWindow
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.NavigatorState
import com.enricog.ui.components.bottomSheet.layout.ModalBottomSheetState
import com.enricog.ui.components.bottomSheet.layout.ModalBottomSheetValue
import com.enricog.ui.components.bottomSheet.layout.rememberModalBottomSheetState
import com.google.accompanist.navigation.material.BottomSheetNavigator as MaterialBottomSheetNavigator

@Navigator.Name("TempoBottomSheetNavigator")
class BottomSheetNavigator(
    val sheetState: ModalBottomSheetState
) : Navigator<BottomSheetNavigator.Destination>() {

    private val materialBottomSheetNavigator = MaterialBottomSheetNavigator(
        sheetState = sheetState.materialState
    )

    val sheetContent: @Composable ColumnScope.() -> Unit = materialBottomSheetNavigator.sheetContent

    override fun onAttach(state: NavigatorState) {
        super.onAttach(state)
        materialBottomSheetNavigator.onAttach(state = state)
    }

    override fun createDestination(): Destination {
        return Destination(navigator = this, content = {})
    }

    override fun navigate(
        entries: List<NavBackStackEntry>,
        navOptions: NavOptions?,
        navigatorExtras: Extras?
    ) {
        materialBottomSheetNavigator.navigate(entries, navOptions, navigatorExtras)
    }

    override fun popBackStack(popUpTo: NavBackStackEntry, savedState: Boolean) {
        materialBottomSheetNavigator.popBackStack(popUpTo, savedState)
    }

    @NavDestination.ClassType(Composable::class)
    class Destination(
        navigator: BottomSheetNavigator,
        internal val content: @Composable ColumnScope.(NavBackStackEntry) -> Unit
    ) : NavDestination(navigator), FloatingWindow
}

@Composable
fun rememberBottomSheetNavigator(
    animationSpec: AnimationSpec<Float> = SpringSpec()
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = animationSpec
    )
    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}
