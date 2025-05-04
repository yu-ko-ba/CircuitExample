package dev.yuyuyuyuyu.circuitexample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val emailRepository = EmailRepository()
    val circuit = Circuit.Builder()
        .addPresenterFactory(InboxPresenter.Factory(emailRepository))
        .addUi<InboxScreen, InboxScreen.State> { state, modifier -> Inbox(state, modifier) }
        .addPresenterFactory(DetailPresenter.Factory(emailRepository))
        .addUi<DetailScreen, DetailScreen.State> { state, modifier -> EmailDetail(state, modifier) }
        .build()

    ComposeViewport(document.body!!) {
        val backStack = rememberSaveableBackStack(root = InboxScreen)
        val navigator = rememberCircuitNavigator(backStack) {
        }

        CircuitCompositionLocals(circuit) {
            NavigableCircuitContent(navigator = navigator, backStack = backStack)
        }
    }
}