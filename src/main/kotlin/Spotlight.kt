class Spotlight (
    val startAddress: DmxAddress
) {
    init {
        require(startAddress.value <= DmxAddress.MAX_VALUE - CHANNEL_COUNT) {
            "Start address ${startAddress.value} is too high. Max start address is ${DmxAddress.MAX_VALUE - CHANNEL_COUNT}"
        }
    }

    val panAddress: DmxAddress
        get() = DmxAddress(startAddress.value + PAN_CHANNEL - 1)

    val tiltAddress: DmxAddress
        get() = DmxAddress(startAddress.value + TILT_CHANNEL - 1)

    val occupiedAddresses: IntRange
        get() = startAddress.value until (startAddress.value + CHANNEL_COUNT)

    companion object {
        const val DEVICE_NAME = "PHW-750E PRO"
        const val CHANNEL_COUNT = 16
        const val PAN_CHANNEL = 15
        const val TILT_CHANNEL = 16
        const val PAN_MAX_DEG = 630
        const val TILT_MAX_DEG = 265
    }
}

