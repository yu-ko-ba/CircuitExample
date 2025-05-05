package dev.yuyuyuyuyu.circuitexample

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import kotlinx.browser.document
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()

    ComposeViewport(document.body!!) {
        KoinContext {
            val circuit = Circuit.Builder()
                .addPresenterFactory(InboxPresenter.Factory(koinInject()))
                .addUi<InboxScreen, InboxScreen.State> { state, modifier -> Inbox(state, modifier) }
                .addPresenterFactory(DetailPresenter.Factory(koinInject()))
                .addUi<DetailScreen, DetailScreen.State> { state, modifier -> EmailDetail(state, modifier) }
                .build()

            val backStack = rememberSaveableBackStack(root = InboxScreen)
            val navigator = rememberCircuitNavigator(backStack) {
            }

            CircuitCompositionLocals(circuit) {
                NavigableCircuitContent(navigator = navigator, backStack = backStack)
            }
        }
    }
}
