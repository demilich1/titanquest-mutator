package net.demilich.tqmutator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import net.demilich.kisslog.LoggerFactory

val logger = LoggerFactory.getDefaultLogger()

@Composable
fun App(state: TitanQuestMutatorState) {
    Box {
        Column(modifier = Modifier.padding(16.dp)) {
            Header(state)
            CharacterTab(state.viewModel)
            Footer(state)
        }
        if (state.viewModel.showDialog.value) {
            Surface(modifier = Modifier.fillMaxSize(1.0f), color = Color(0, 0, 0, 127)) {}
        }
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


