package net.demilich.tqmutator

import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

class TitanQuestCharacterFile(
    val file: File,
    val markers: Map<String, Int>,
    var headerVersion: Int,
    var characterName: String,
    var level: Int,
    var money: Int,
    var skillPoints: Int,
) {

}

fun loadCharacterFile(file: File): TitanQuestCharacterFile {
    val timeStart = System.currentTimeMillis()
    val bytes = file.readBytes()
    val markers = findMarkers(
        bytes, listOf(
            "headerVersion",
            "playerLevel",
            "myPlayerName",
            "money",
            "skillPoints"
        )
    )
    val buffer = ByteBuffer.wrap(bytes)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    val headerVersion = readByte(buffer, markers["headerVersion"]!!)
    val playerLevel = readByte(buffer, markers["playerLevel"]!!)
    val playerName = readUTF16(buffer, markers["myPlayerName"]!!)
    val money = readInt(buffer, markers["money"]!!)
    val skillPoints = readInt(buffer, markers["skillPoints"]!!)
    val duration = System.currentTimeMillis() - timeStart
    logger.info("LOADING character file was successful, took $duration ms")

    return TitanQuestCharacterFile(file, markers, headerVersion, playerName, playerLevel, money, skillPoints)
}

fun saveCharacterFile(characterFile: TitanQuestCharacterFile) {
    val timeStart = System.currentTimeMillis()
    //TODO: implement
    val duration = System.currentTimeMillis() - timeStart
    logger.info("SAVING character file was successful, took $duration ms")
}

private fun findMarkers(bytes: ByteArray, markers: List<String>): Map<String, Int> {
    val markerMap = mutableMapOf<String, Int>()
    for (marker in markers) {
        val markerBytes = marker.toByteArray()
        val index = indexOf(bytes, markerBytes)
        if (index == -1) {
            logger.error("Expected marker '$marker' could not be found in savefile")
            continue
        }
        val offset = index + markerBytes.size
        markerMap[marker] = offset

    }
    return markerMap
}

private fun readByte(stream: ByteBuffer, offset: Int): Int {
    stream.position(offset)
    return stream.get().toInt()
}

private fun readInt(stream: ByteBuffer, offset: Int): Int {
    stream.position(offset)
    return stream.getInt()
}

private fun readUTF16(stream: ByteBuffer, offset: Int): String {
    stream.position(offset)
    val len = stream.getInt() * 2 // each character is two bytes
    val bytes = ByteArray(len)
    stream.get(bytes)
    return String(bytes, Charset.forName("UTF-16LE"))
}

