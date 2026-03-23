package com.leafsheepsolar;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class StopwatchLabel extends JLabel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int time;            // 0–999 (seconds)
    private final Timer timer;

    
	/**
	 * <p> JLabel with an automatic stopwatch. Check methods for more info. 
	 * 
	 * <p> There is preset GUI, with the exeption of bounds. 
	*/
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
        
        //One for display seconds, the other for miliseconds when reporting the final time. 
        
        configure();
    }

    //configures gui elements 
	private void configure() {
		
		Font configureFont;
	    try {
	    	configureFont = Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream("/alarm clock.ttf"));
	    	configureFont = configureFont.deriveFont(Font.BOLD,28f);
	    }
	    catch (FontFormatException | IOException e){
	    	e.printStackTrace();
	    	configureFont = new Font("Arial", Font.PLAIN, 28);
	    }
		
		setHorizontalAlignment(SwingConstants.TRAILING);
		setFont(configureFont);
		setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setBackground(new Color(128,128,128));
	}

	/**
	 *  Starts or resumes the timer
	 */
    public void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

	/**
	 *  Pauses the timer
	 */    
    public void pause() {
        timer.stop();
    }

	/**
	 *  Resets time to 0 and updates display
	 */
    public void reset() {
        timer.stop();
        time = 0;
        setText(formatTime());
    }

	/**
	 *  Returns the current time field
	 */
	 public int getTime() {
        return time;
    }

    // Formats time as 3 digits (000–999)
    private String formatTime() {
        return String.format("%03d", time);
    }
}
