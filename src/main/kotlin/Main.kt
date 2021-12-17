import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.FileDialog
import java.io.File

@Composable
fun App(state: TitanQuestMutatorState) {
    CharacterTab(state.viewModel)
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
            MenuBar {
                Menu("File") {
                    Item("Open", onClick = {
                        val selectedFile =
                            openFileDialog(ComposeWindow(), "Select Titan Quest Character File", listOf(".chr"))
                                ?: return@Item
                        println("Selected file: $selectedFile")
                        state.data = loadCharacterFile(selectedFile)
                        state.viewModel.update(state.data!!)
                    })
                    Separator()
                    Item("Exit", onClick = { exitApplication() })
                }
            }

            App(state)
        }

    }
}

fun openFileDialog(window: ComposeWindow, title: String, allowedExtensions: List<String>): File? {
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

