package debouncer

import timer.Timer

/**
 * A debouncer is useful to get rid of unwanted fast on-off cycles of a digital
 * signal. These fast cycles ("bounces") are often due to sensor errors like vibration
 * or signal interference, and are not what the sensor is actually trying to record.
 *
 * Since debouncers have memory, use a separate instance for each input stream.
 *
 * @param debounceTimeMilliseconds - The amount of time the input has to stay
 *                                   the same in order for the output to change.
 */
class Debouncer(debounceTimeMilliseconds: Double) {
    var debounceTimeMilliseconds = debounceTimeMilliseconds
        set(value) {
            require(value >= 0.0) {"debounceTimeMilliseconds cannot be negative."}
            field = value
        }
    private val timer = Timer()
    private var previousValue: Boolean? = null
    private var output: Boolean? = null

    /**
     * Debounce both edges (false to true and true to false).
     *
     * This function must be called periodically with the updated value.
     */
    fun debounce(newValue: Boolean): Boolean {
        require(debounceTimeMilliseconds >= 0.0) {"Debounce time cannot be negative."}

        if(previousValue == null) { // Will only be null on first call to debounce
            previousValue = newValue
            output = newValue
            timer.start()
        }
        else if(previousValue != newValue) timer.reset()
        else if(timer.hasElapsed(debounceTimeMilliseconds)) output = newValue

        previousValue = newValue
        return output!!
    }

    /**
     * Debounce rising edge (false to true) only.
     *
     * This function must be called periodically with the updated value.
     */
    fun debounceRisingEdge(newValue: Boolean): Boolean {
        output = if(newValue) debounce(true) else false
        previousValue = newValue
        return output!!
    }

    /**
     * Debounce falling edge (true to false) only.
     *
     * This function must be called periodically with the updated value.
     */
    fun debounceFallingEdge(newValue: Boolean): Boolean {
        output = if(!newValue) debounce(false) else true
        previousValue = newValue
        return output!!
    }

    /**
     * Discards memory of the previous value and sets the output to the new one.
     */
    fun reset(newValue: Boolean) {
        previousValue = newValue
        output = newValue
        timer.reset()
    }

    override fun toString(): String {
        return ("Last value: $previousValue\n" +
                "Time ms since change: $timer.millis\n" +
                "Debounce time ms: $debounceTimeMilliseconds")
    }
}