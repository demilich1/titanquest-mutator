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

val MARKER_LIST = listOf(
    MARKER_HEADER_VERSION,
    MARKER_PLAYER_LEVEL,
    MARKER_PLAYER_NAME,
    MARKER_MONEY,
    MARKER_ATTRIBUTE_POINTS,
    MARKER_SKILLPOINTS
)

class TitanQuestCharacterFile(
    val file: File,
    val markers: Map<String, Int>,
    val headerVersion: Int,
    var characterName: String,
    var level: Int,
    var money: Int,
    var attributePoints: Int,
    var skillPoints: Int,
) {
    val oldName: String = characterName
}

fun loadCharacterFile(file: File): TitanQuestCharacterFile {
    val timeStart = System.currentTimeMillis()
    val bytes = file.readBytes()
    val markers = findMarkers(bytes, MARKER_LIST)
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

fun saveCharacterFile(saveData: TitanQuestCharacterFile): TitanQuestCharacterFile {
    val timeStart = System.currentTimeMillis()
    val bytes = saveData.file.readBytes()
    var buffer = ByteBuffer.wrap(bytes)
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

    // write name last, as this changes file length and thus invalidates all markers
    val nameIsDirty = saveData.oldName != saveData.characterName
    if (nameIsDirty) {
        val nameOffset = markers[MARKER_PLAYER_NAME]!!
        val currentNameLen = readInt(buffer, nameOffset) * 2
        val allBytes = buffer.array()
        val bytesPart1 = ByteArray(nameOffset)
        val part2Len = allBytes.size - nameOffset - currentNameLen - 4
        val bytesPart2 = ByteArray(part2Len)
        System.arraycopy(allBytes, 0, bytesPart1, 0, nameOffset)
        System.arraycopy(allBytes, nameOffset + 4 + currentNameLen, bytesPart2, 0, part2Len)
        val newName = saveData.characterName
        val newNameArray = ByteArray(4 + newName.length * 2)
        val newNameBuffer = ByteBuffer.wrap(newNameArray)
        newNameBuffer.order(ByteOrder.LITTLE_ENDIAN)
        newNameBuffer.putInt(newName.length)
        newNameBuffer.put(newName.toByteArray(Charset.forName("UTF-16LE")))
        val result = bytesPart1 + newNameBuffer.array() + bytesPart2
        buffer = ByteBuffer.wrap(result)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
    }

    saveData.file.writeBytes(buffer.array())
    if (nameIsDirty) {
        val parent = saveData.file.parentFile
        if (parent != null && parent.isDirectory && parent.name == "_" + saveData.oldName) {
            val newDir = File(parent.parentFile.path + File.separator + "_" + saveData.characterName + File.separator)
            val newDirName = newDir.path
            logger.info("Renaming parent directory to match character name to $newDirName")
            if (!parent.renameTo(newDir)) {
                logger.error("Could not rename directory to'$newDirName'")
            }
            val newFile = File(newDirName + File.separator + saveData.file.name)
            logger.info("Path changed, reloading character file from $newDirName")
            return loadCharacterFile(newFile)
        } else {
            logger.info("Parent directory name does not match, standalone savefile? Skip renaming of parent directory")
        }
    }

    val duration = System.currentTimeMillis() - timeStart
    logger.info("SAVING character file was successful, took $duration ms")
    return saveData
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
    return buffer.get(offset).toInt()
}

private fun readInt(buffer: ByteBuffer, offset: Int): Int {
    return buffer.getInt(offset)
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

