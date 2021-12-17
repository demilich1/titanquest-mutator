import org.apache.logging.log4j.kotlin.logger
import java.io.File
import java.lang.Integer.min
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.Charset

val logger = logger("TitanQuestCharacterFileKt")

class TitanQuestCharacterFile(
    val file: File,
    var headerVersion: Int,
    var characterName: String,
    var level: Int,
    var money: Int,
    var skillPoints: Int
) {

}

fun loadCharacterFile(file: File): TitanQuestCharacterFile {
    val timeStart = System.currentTimeMillis()
    val bytes = file.readBytes()
    val buffer = ByteBuffer.wrap(bytes)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    val headerVersion = readByte(buffer, "headerVersion", 64)
    val playerLevel = readByte(buffer, "playerLevel", 256)
    val playerName = readUTF16(buffer, "myPlayerName", 256)
    val money = readInt(buffer, "money", Short.MAX_VALUE.toInt())
    val skillPoints = readInt(buffer, "skillPoints", Short.MAX_VALUE.toInt())
    println("headerVersion: $headerVersion")
    println("playerName: $playerName")
    println("playerLevel: $playerLevel")
    println("money: $money")
    println("skillPoints: $skillPoints")
    val duration = System.currentTimeMillis() - timeStart
    logger.info ("Loading character file was successful, took $duration ms" )
    return TitanQuestCharacterFile(file, headerVersion, playerName, playerLevel, money, skillPoints)
}

fun saveCharacterFile(characterFile: TitanQuestCharacterFile) {

}

fun seekMarker(stream: ByteBuffer, marker: String, lookAhead: Int = 1024) {
    val pos = stream.position()
    //TODO: this is very inperformant, always reading the whole buffer when looking for one marker
    val bufferSize = min(lookAhead, stream.remaining())
    val bytes = ByteArray(bufferSize)
    stream.get(bytes)

    val markerBytes = marker.toByteArray()
    var index = indexOf(bytes, markerBytes)
    if (index == -1) {
        println("Marker '$marker' could not be found")
    }
    index += markerBytes.size
    stream.position(pos + index)
}

fun readByte(stream: ByteBuffer, marker: String, lookAhead: Int = 1024): Int {
    seekMarker(stream, marker, lookAhead)
    return stream.get().toInt()
}

fun readInt(stream: ByteBuffer, marker: String, lookAhead: Int = 1024): Int {
    seekMarker(stream, marker, lookAhead)
    return stream.getInt()
}

fun readUTF16(stream: ByteBuffer, marker: String, lookAhead: Int = 1024): String {
    seekMarker(stream, marker, lookAhead)
    val len = stream.getInt() * 2
    val bytes = ByteArray(len)
    stream.get(bytes)
    return String(bytes, Charset.forName("UTF-16LE"))
}

