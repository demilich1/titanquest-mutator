package net.demilich.tqmutator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.concurrent.schedule


@Composable
fun Footer(state: TitanQuestMutatorState) {
    val viewModel = state.viewModel
    ConfirmDialog(viewModel.showDialog, state)
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1.0f))
            Button(
                onClick = { onSave(viewModel.showDialog) }, modifier = Modifier.padding(8.dp),
                enabled = viewModel.dirty.value
            ) {
                Text("Save character")
            }
        }
        AnimatedVisibility(visible = viewModel.showSaveSuccess.value, modifier = Modifier.align(Alignment.End)) {
            Text(
                "Save successful",
                color = Color(0, 100, 0),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ConfirmDialog(showDialog: MutableState<Boolean>, state: TitanQuestMutatorState) {
    if (showDialog.value) {
        AlertDialog(onDismissRequest = {},
            shape = MaterialTheme.shapes.large,
            title = { Text("Are you sure?") },
            confirmButton = {
                Button(onClick = {
                    showDialog.value = false
                    doSave(state)
                }) {
                    Text("Accept")
                }
            },
            modifier = Modifier.defaultMinSize(400.dp),
            dismissButton = {
                OutlinedButton(onClick = {
                    showDialog.value = false
                }) {
                    Text("Cancel")
                }
            },
            text = {
                Row {
                    Icon(Icons.Filled.Warning, "warning", modifier = Modifier.size(32.dp), tint = Color(246, 190, 0))
                    Text(
                        "Do you really want to change the values of the selected character? This will permanently alter your save file!",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            })
    }
}

private fun onSave(showDialog: MutableState<Boolean>) {
    showDialog.value = true
}

private fun doSave(state: TitanQuestMutatorState) {
    val data = state.data
    if (data == null) {
        logger.error("Saving failed; no savegame data loaded")
        return
    }
    val viewModel = state.viewModel
    try {
        data.characterName = viewModel.name.value
        data.money = viewModel.money.value.toInt()
        data.attributePoints = viewModel.availableAttributePoints.value.toInt()
        data.skillPoints = viewModel.availableSkillpoints.value.toInt()
    } catch (e: Exception) {
        logger.error("Saving failed; user entered data is invalid")
    }
    val newData = saveCharacterFile(data)
    if (newData != data) {
        state.data = newData
        viewModel.update(newData)
    }
    viewModel.showSaveSuccess.value = true
    Timer("HideSuccessMessage", false).schedule(3000) {
        viewModel.showSaveSuccess.value = false
    }
}
