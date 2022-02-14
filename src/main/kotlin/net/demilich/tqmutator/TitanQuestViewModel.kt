package net.demilich.tqmutator

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class TitanQuestViewModel {

    val filename: MutableState<String> = mutableStateOf("<please load a Titan Quest character file>")
    val dirty: MutableState<Boolean> = mutableStateOf(false)
    val showDialog: MutableState<Boolean> = mutableStateOf(false)
    val showSaveSuccess: MutableState<Boolean> = mutableStateOf(false)
    val name: MutableState<String> = mutableStateOf("")
    val money: MutableState<String> = mutableStateOf("0")
    val level: MutableState<String> = mutableStateOf("0")
    val availableAttributePoints: MutableState<String> = mutableStateOf("0")
    val availableSkillpoints: MutableState<String> = mutableStateOf("0")
    val numberOfKills: MutableState<String> = mutableStateOf("0")
    val numberOfDeaths: MutableState<String> = mutableStateOf("0")

    fun update(data: TitanQuestCharacterFile) {
        filename.value = data.file.path
        dirty.value = false
        name.value = data.characterName
        money.value = data.money.toString()
        level.value = data.level.toString()
        availableAttributePoints.value = data.attributePoints.toString()
        availableSkillpoints.value = data.skillPoints.toString()
        numberOfKills.value = data.numberOfKills.toString()
        numberOfDeaths.value = data.numberOfDeaths.toString()
    }
}

fun getDummyData() : TitanQuestViewModel {
    return TitanQuestViewModel()
}