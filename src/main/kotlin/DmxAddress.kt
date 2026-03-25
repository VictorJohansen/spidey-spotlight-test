@JvmInline
value class DmxAddress(val value: Int) {
    init {
        require(value in MIN_VALUE..MAX_VALUE) {
            "Invalid DMX address: $value. Valid range is $VALID_RANGE"
        }
    }

    companion object {
        const val MIN_VALUE = 1
        const val MAX_VALUE = 512

        val VALID_RANGE = MIN_VALUE..MAX_VALUE
    }
}