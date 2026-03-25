import com.fazecast.jSerialComm.SerialPort

fun main() {
    val eye = Spotlight(DmxAddress(1))
    val universe = DmxUniverse(listOf(eye))
    universe.pan(0, 90f)

}