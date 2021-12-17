package net.demilich.tqmutator

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CharacterTab(viewModel: TitanQuestViewModel) {
    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            StringValueRow("Name:", viewModel.name)
            StringValueRow("Level:", viewModel.level, true)
            StringValueRow("Money:", viewModel.money, false, InputValidation.INTEGER)
            StringValueRow("Available skillpoints:", viewModel.availableSkillpoints, false, InputValidation.INTEGER)
        }
    }
}

@Composable
private fun StringValueRow(
    prefix: String,
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
                    InputValidation.NONE -> state.value = it
                    InputValidation.INTEGER -> {
                        val text = validateNumberInput(it, state.value)
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

fun validateNumberInput(input: String, oldValue: String): String {
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