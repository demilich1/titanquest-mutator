package net.demilich.tqmutator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.apache.logging.log4j.kotlin.logger

val logger = logger("TitanQuestMutator")

@Composable
fun App(state: TitanQuestMutatorState) {
    Column(modifier = Modifier.padding(16.dp)) {
        Header(state)
        CharacterTab(state.viewModel)
        Footer(state)
    }
}

fun main() = application {
    val state = remember {
        val viewModel = getDummyData()
        TitanQuestMutatorState(viewModel, null)
    }
    MaterialTheme {
        Window(
            title = "Titan Quest AE Mutator",
            onCloseRequest = ::exitApplication
        ) {
            App(state)
        }
    }
}


