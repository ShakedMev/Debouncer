package timer

/**
 * A modified version of WPILib's Timer.java in edu.wpi.first.wpilibj,
 * that is both RoboRIO and PC friendly! (and is in Kotlin)
 *
 * To do so, it relies on `System.currentTimeMillis`. If you're writing
 * code for a robot and need an accuracy of microseconds for some reason
 * (maybe things moving really fast?) use WPIlib's timer instead, it
 * relies on `RobotController.getFPGATimeStamp`.
 */
class Timer() {
    private var startTimeMillis = 0L
    private var accumulatedTimeMillis = 0L
    private var running = false

    /**
     * Starts the timer if it isn't running already.
     */
    fun start() {
        if(!running) {
            startTimeMillis = System.currentTimeMillis()
            running = true
        }
    }

    fun getMillis(): Long {
        return if(running) {
            System.currentTimeMillis() - startTimeMillis + accumulatedTimeMillis
        } else accumulatedTimeMillis
    }

    fun getSeconds(): Double = getMillis().toDouble()

    fun reset() {
        startTimeMillis = System.currentTimeMillis()
        accumulatedTimeMillis = 0L
    }

    fun resetAndStart() {
        reset()
        start()
    }

    fun stopAndReset() {
        stop()
        reset()
    }

    /**
     * Stops the timer. Calls to getMillis or getSeconds will return the time as of now.
     */
    fun stop() {
        if(running) {
            running = false
            accumulatedTimeMillis = getMillis()
        }
    }

    fun hasElapsed(millis: Long) = getMillis() >= millis

    fun hasElapsed(millis: Double) = getMillis().toDouble() >= millis
}
