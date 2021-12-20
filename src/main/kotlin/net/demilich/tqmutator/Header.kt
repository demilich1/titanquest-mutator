package net.demilich.tqmutator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.awt.FileDialog
import java.io.File

@Composable
fun Header(state: TitanQuestMutatorState) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.disabled) {
            Text("Version: $version", fontSize = 14.sp)
        }
    }
    LoadFile(state)
}

@Composable
private fun LoadFile(state: TitanQuestMutatorState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = {
                val startDirectory = if (state.data != null) state.data!!.file.parent else null
                val selectedFile = openFileDialog(
                    ComposeWindow(),
                    "Open 'Titan Quest' Character File",
                    listOf(".chr"),
                    startDirectory
                )
                    ?: return@Button
                logger.info("Selected file: $selectedFile")
                state.data = loadCharacterFile(selectedFile)
                state.viewModel.update(state.data!!)

            }, modifier = Modifier.padding(8.dp)
        ) {
            Text("Load character")
        }
        Text(text = state.viewModel.filename.value, modifier = Modifier.padding(8.dp))
    }
}

private fun openFileDialog(
    window: ComposeWindow,
    title: String,
    allowedExtensions: List<String>,
    startDirectory: String?
): File? {
    val files = FileDialog(window, title, FileDialog.LOAD).apply {
        // windows
        file = allowedExtensions.joinToString(";") { "*$it" } // e.g. '*.jpg'

        // linux
        setFilenameFilter { _, name ->
            allowedExtensions.any {
                name.endsWith(it)
            }
        }

        if (startDirectory != null) {
            directory = startDirectory
        }

        isVisible = true
    }.files
    if (files.isEmpty()) {
        return null
    }
    return files[0]
}
