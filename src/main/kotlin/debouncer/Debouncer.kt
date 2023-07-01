package debouncer

import timer.Timer

/**
 * Since debouncers have memory, use a separate instance for each input stream.
 * @param debounceTimeMilliseconds - The amount of time the value has to stay
 *                                   the same in order for the output to change.
 */
class Debouncer(debounceTimeMilliseconds: Double) {
    var debounceTimeMilliseconds = debounceTimeMilliseconds
        set(value) {
            require(value >= 0.0) {"debounceTimeMilliseconds cannot be negative"}
            field = value
        }
    private val timer = Timer()
    private var previousValue: Boolean? = null
    private var output: Boolean? = null

    /** Debounce both edges (false to true and true to false). */
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

    /** Debounce rising edge (false to true) only. */
    fun debounceRisingEdge(newValue: Boolean): Boolean {
        output = if(newValue) debounce(true) else false
        previousValue = newValue
        return output!!
    }

    /** Debounce falling edge (true to false) only. */
    fun debounceFallingEdge(newValue: Boolean): Boolean {
        output = if(!newValue) debounce(false) else true
        previousValue = newValue
        return output!!
    }

    fun reset(newValue: Boolean) {
        previousValue = newValue
        timer.reset()
    }

    override fun toString(): String {
        return ("Last value: " + previousValue + "\n" +
                "Time ms since change: " + (timer.getMillis()) + "\n" +
                "Debounce time ms: " + debounceTimeMilliseconds)
    }
}