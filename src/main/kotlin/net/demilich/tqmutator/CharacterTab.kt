package net.demilich.tqmutator

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CharacterTab(viewModel: TitanQuestViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(8.dp)
    ) {
        StringValueRow("Name:", viewModel, viewModel.name)
        StringValueRow("Level:", viewModel, viewModel.level, true)
        StringValueRow("Money:", viewModel, viewModel.money, false, InputValidation.INTEGER)
        StringValueRow(
            "Available attribute points:",
            viewModel,
            viewModel.availableAttributePoints,
            false,
            InputValidation.INTEGER
        )
        StringValueRow(
            "Available skillpoints:",
            viewModel,
            viewModel.availableSkillpoints,
            false,
            InputValidation.INTEGER
        )
    }
}

@Composable
private fun StringValueRow(
    prefix: String,
    viewModel: TitanQuestViewModel,
    state: MutableState<String>,
    readOnly: Boolean = false,
    validation: InputValidation = InputValidation.NONE
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Text(prefix, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(0.5f))
        TextField(
            value = state.value,
            onValueChange = {
                when (validation) {
                    InputValidation.NONE -> {
                        state.value = it
                        viewModel.dirty.value = true
                    }
                    InputValidation.INTEGER -> {
                        val text = validateInputInteger(it, state.value)
                        if (text != state.value) {
                            viewModel.dirty.value = true
                        }
                        state.value = text
                    }
                }
            },
            singleLine = true,
            readOnly = readOnly,
            enabled = !readOnly
        )
    }
}

enum class InputValidation {
    NONE,
    INTEGER
}

fun validateInputInteger(input: String, oldValue: String): String {
    if (input.isEmpty()) {
        return "0"
    }
    try {
        val number = input.toInt()
    } catch (e: Exception) {
        return oldValue
    }
    return input
}