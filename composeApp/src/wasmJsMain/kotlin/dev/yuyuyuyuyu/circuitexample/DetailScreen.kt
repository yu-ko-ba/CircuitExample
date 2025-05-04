package dev.yuyuyuyuyu.circuitexample

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen

data class DetailScreen(val emailId: String) : Screen {
    data class State(
        val email: Email,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState

    sealed class Event : CircuitUiEvent {
        data object BackClicked : Event()
    }
}

class DetailPresenter(
    private val screen: DetailScreen,
    private val navigator: Navigator,
    private val emailRepository: EmailRepository,
) : Presenter<DetailScreen.State> {
    @Composable
    override fun present(): DetailScreen.State {
        val email = emailRepository.getEmail(screen.emailId)
        return DetailScreen.State(email) { event ->
            when (event) {
                DetailScreen.Event.BackClicked -> navigator.pop()
            }
        }
    }

    class Factory(private val emailRepository: EmailRepository) : Presenter.Factory {
        override fun create(screen: Screen, navigator: Navigator, context: CircuitContext): Presenter<*>? {
            return when (screen) {
                is DetailScreen -> DetailPresenter(screen, navigator, emailRepository)
                else -> null
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailDetail(state: DetailScreen.State, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(state.email.subject) },
                navigationIcon = {
                    IconButton(onClick = { state.eventSink(DetailScreen.Event.BackClicked) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding), verticalArrangement = spacedBy(16.dp)) {
            EmailDetailContent(state.email)
        }
    }
}

@Composable
fun EmailDetailContent(email: Email, modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Image(
                Icons.Default.Person,
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.Magenta).padding(4.dp),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = null,
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row {
                    Text(
                        text = email.sender,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = email.timestamp,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.alpha(0.5f),
                    )
                }
                Text(text = email.subject, style = MaterialTheme.typography.labelMedium)
                Row {
                    Text("To: ", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = email.recipients.joinToString(","),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.alpha(0.5f),
                    )
                }
            }
        }
        @Suppress("DEPRECATION") // Deprecated in Android but not yet available in CM
        Divider(modifier = Modifier.padding(vertical = 16.dp))
        Text(text = email.body, style = MaterialTheme.typography.bodyMedium)
    }
}
