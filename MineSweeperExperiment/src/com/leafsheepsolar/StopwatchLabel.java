package com.leafsheepsolar;

import javax.swing.JLabel;
import javax.swing.Timer;

public class StopwatchLabel extends JLabel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int time;            // 0–999 (seconds)
    private final Timer timer;

    public StopwatchLabel() {
        time = 0;
        setText(formatTime());

        // Fires every 1000 ms (1 second)
        timer = new Timer(1000, e -> {
            if (time < 999) {
                time++;
                setText(formatTime());
            }
        });
    }

    // Starts or resumes the timer
    public void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    // Pauses the timer
    public void pause() {
        timer.stop();
    }

    // Resets time to 0 and updates display
    public void reset() {
        timer.stop();
        time = 0;
        setText(formatTime());
    }

    // Returns the current time value
    public int getTime() {
        return time;
    }

    // Formats time as 3 digits (000–999)
    private String formatTime() {
        return String.format("%03d", time);
    }
}
