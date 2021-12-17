package net.demilich.tqmutator

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.unit.dp
import java.awt.FileDialog
import java.io.File

@Composable
fun Header(state: TitanQuestMutatorState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = {
                val selectedFile = openFileDialog(ComposeWindow(), "Open 'Titan Quest' Character File", listOf(".chr"))
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

private fun openFileDialog(window: ComposeWindow, title: String, allowedExtensions: List<String>): File? {
    val files = FileDialog(window, title, FileDialog.LOAD).apply {
        // windows
        file = allowedExtensions.joinToString(";") { "*$it" } // e.g. '*.jpg'

        // linux
        setFilenameFilter { _, name ->
            allowedExtensions.any {
                name.endsWith(it)
            }
        }

        isVisible = true
    }.files
    if (files.isEmpty()) {
        return null
    }
    return files[0]
}
