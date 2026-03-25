@JvmInline
value class DmxValue(val value: Int) {
    init {
        require(value in VALID_RANGE) {
            "Invalid DMX value: $value. Valid range is $VALID_RANGE"
        }
    }

    companion object {
        const val MIN_VALUE = 0
        const val MAX_VALUE = 255
        val VALID_RANGE = MIN_VALUE..MAX_VALUE
    }
}