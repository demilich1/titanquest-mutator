package net.demilich.tqmutator

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class TitanQuestViewModel {

    var filename: MutableState<String> = mutableStateOf("<please load a Titan Quest character file>")
    var name: MutableState<String> = mutableStateOf("")
    var money: MutableState<String> = mutableStateOf("0")
    var level: MutableState<String> = mutableStateOf("0")
    var availableSkillpoints: MutableState<String> = mutableStateOf("0")

    fun update(data: TitanQuestCharacterFile) {
        filename.value = data.file.path
        name.value = data.characterName
        money.value = data.money.toString()
        level.value = data.level.toString()
        availableSkillpoints.value = data.skillPoints.toString()
    }
}

fun getDummyData() : TitanQuestViewModel {
    return TitanQuestViewModel()
}