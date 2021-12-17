import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class TitanQuestViewModel() {

    var name: MutableState<String> = mutableStateOf("<unknown>")
    var money: MutableState<String> = mutableStateOf("0")
    var level: MutableState<String> = mutableStateOf("0")
    var availableSkillpoints: MutableState<String> = mutableStateOf("0")

    fun update(data: TitanQuestCharacterFile) {
        name.value = data.characterName
        money.value = data.money.toString()
        level.value = data.level.toString()
        availableSkillpoints.value = data.skillPoints.toString()
    }
}

fun getDummyData() : TitanQuestViewModel {
    return TitanQuestViewModel()
}