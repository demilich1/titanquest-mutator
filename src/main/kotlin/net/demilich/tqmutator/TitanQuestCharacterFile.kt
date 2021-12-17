package net.demilich.tqmutator

import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

const val MARKER_HEADER_VERSION = "headerVersion"
const val MARKER_PLAYER_LEVEL = "playerLevel"
const val MARKER_PLAYER_NAME = "myPlayerName"
const val MARKER_MONEY = "money"
const val MARKER_ATTRIBUTE_POINTS = "modifierPoints"
const val MARKER_SKILLPOINTS = "skillPoints"

class TitanQuestCharacterFile(
    val file: File,
    val markers: Map<String, Int>,
    var headerVersion: Int,
    var characterName: String,
    var level: Int,
    var money: Int,
    var attributePoints: Int,
    var skillPoints: Int,
)

fun loadCharacterFile(file: File): TitanQuestCharacterFile {
    val timeStart = System.currentTimeMillis()
    val bytes = file.readBytes()
    val markers = findMarkers(
        bytes, listOf(
            MARKER_HEADER_VERSION,
            MARKER_PLAYER_LEVEL,
            MARKER_PLAYER_NAME,
            MARKER_MONEY,
            MARKER_ATTRIBUTE_POINTS,
            MARKER_SKILLPOINTS
        )
    )
    val buffer = ByteBuffer.wrap(bytes)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    val headerVersion = readByte(buffer, markers[MARKER_HEADER_VERSION]!!)
    val playerLevel = readByte(buffer, markers[MARKER_PLAYER_LEVEL]!!)
    val playerName = readUTF16(buffer, markers[MARKER_PLAYER_NAME]!!)
    val money = readInt(buffer, markers[MARKER_MONEY]!!)
    val attributePoints = readInt(buffer, markers[MARKER_ATTRIBUTE_POINTS]!!)
    val skillPoints = readInt(buffer, markers[MARKER_SKILLPOINTS]!!)
    val duration = System.currentTimeMillis() - timeStart
    logger.info("LOADING character file was successful, took $duration ms")

    return TitanQuestCharacterFile(
        file,
        markers,
        headerVersion,
        playerName,
        playerLevel,
        money,
        attributePoints,
        skillPoints
    )
}

fun saveCharacterFile(saveData: TitanQuestCharacterFile) {
    val timeStart = System.currentTimeMillis()
    val bytes = saveData.file.readBytes()
    val buffer = ByteBuffer.wrap(bytes)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    val markers = saveData.markers

    if (markers.containsKey(MARKER_PLAYER_LEVEL)) {
        writeInt(buffer, markers[MARKER_PLAYER_LEVEL]!!, saveData.level)
    }
    if (markers.containsKey(MARKER_MONEY)) {
        writeInt(buffer, markers[MARKER_MONEY]!!, saveData.money)
    }
    if (markers.containsKey(MARKER_ATTRIBUTE_POINTS)) {
        writeInt(buffer, markers[MARKER_ATTRIBUTE_POINTS]!!, saveData.attributePoints)
    }
    if (markers.containsKey(MARKER_SKILLPOINTS)) {
        writeInt(buffer, markers[MARKER_SKILLPOINTS]!!, saveData.skillPoints)
    }
    //TODO: write name last, as this changes file length and thus invalidates all markers

    saveData.file.writeBytes(buffer.array())
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

private fun readByte(buffer: ByteBuffer, offset: Int): Int {
    buffer.position(offset)
    return buffer.get().toInt()
}

private fun readInt(buffer: ByteBuffer, offset: Int): Int {
    buffer.position(offset)
    return buffer.getInt()
}

private fun readUTF16(buffer: ByteBuffer, offset: Int): String {
    buffer.position(offset)
    val len = buffer.getInt() * 2 // each character is two bytes
    val bytes = ByteArray(len)
    buffer.get(bytes)
    return String(bytes, Charset.forName("UTF-16LE"))
}

private fun writeInt(buffer: ByteBuffer, offset: Int, value: Int) {
    buffer.putInt(offset, value)
}

