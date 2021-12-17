package net.demilich.tqmutator

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.lang.Exception

@Composable
fun Footer(state: TitanQuestMutatorState) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.weight(1.0f))
        Button(
            onClick = { onSave(state) }, modifier = Modifier.padding(8.dp)
        ) {
            Text("Save character")
        }
    }
}

fun onSave(state: TitanQuestMutatorState) {
    val data = state.data
    if (data == null) {
        logger.error("Saving failed; no savegame data loaded")
        return
    }
    val viewModel = state.viewModel
    try {
        data.characterName = viewModel.name.value
        data.money = viewModel.money.value.toInt()
        data.skillPoints = viewModel.availableSkillpoints.value.toInt()
    } catch (e: Exception) {
        logger.error("Saving failed; user entered data is invalid")
    }
    saveCharacterFile(data)
}
