import com.fazecast.jSerialComm.SerialPort

class DmxUniverse(spotlights: Iterable<Spotlight>) {
    val spotlights: List<Spotlight> = spotlights.toList()

    init {
        val usedAddresses = mutableSetOf<Int>()

        for (device in this.spotlights) {
            for (address in device.occupiedAddresses) {
                require(usedAddresses.add(address)) {
                    "Address conflict at DMX address $address"
                }
            }
        }
    }

    val dmxData = ByteArray(DmxAddress.MAX_VALUE)

    fun pan(spotlightIndex: Int, targetPanDeg: Float) {
        val spotlight = spotlights[spotlightIndex]
        editDmxData(spotlight.panAddress, DmxValue(targetPanDeg.toInt()))
        sendDmxPacket()
    }

    fun tilt(spotlightIndex: Int, targetTiltDeg: Float) {
        val spotlight = spotlights[spotlightIndex]
        editDmxData(spotlight.tiltAddress, DmxValue(targetTiltDeg.toInt()))
        sendDmxPacket()
    }

    private fun editDmxData(dmxAddress: DmxAddress, dmxValue: DmxValue) {
        dmxData[dmxAddress.value] = dmxValue.value.toByte()
        println("DMX data edited")
    }

    private fun buildEnttecDmxPacket(dmxData: ByteArray): ByteArray {
        val start: Byte = 0x7E
        val label: Byte = 0x06

        val length = dmxData.size
        val lsb = (length and 0xFF).toByte()
        val msb = ((length shr 8) and 0xFF).toByte()

        val end: Byte = 0xE7.toByte()

        println("Enttec DMX packet built")
        return byteArrayOf(start, label, lsb, msb) + dmxData + byteArrayOf(end)
    }

    private fun sendDmxPacket() {
        val dmxVendorId = 1027
        val dmxProductId = 24577
        var port = findPortById(dmxVendorId, dmxProductId)
        if (port != null) {
            port.baudRate = 115200
            port.openPort()
            println("Port opened")
            port.writeBytes(buildEnttecDmxPacket(dmxData), dmxData.size)
            println("DMX packet sent")
            port.closePort()
            println("Port closed")
        }

    }

    private fun findPortById(vendorId: Int, productId: Int): SerialPort? {
        val ports = SerialPort.getCommPorts()

        if (ports.isEmpty()) {
            return null
        }

        ports.forEach { port ->
            if (port.vendorID == vendorId && port.productID == productId) {
                return port
                print("Found port: ${port.systemPortName}")
            }
        }

        return null
    }
}