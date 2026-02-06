package com.leafsheepsolar;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class IconRegistry {

    /* ─────────────────────────────────────────────────────────
     * ICON ROOT DIRECTORY
     * ───────────────────────────────────────────────────────── */
    private static final String ICON_ROOT = "/com/leafsheepsolar/Icons/";

    /* ─────────────────────────────────────────────────────────
     * CANONICAL ICON SIZE (use for scaled)
     * ───────────────────────────────────────────────────────── */
    private static final Map<String, Rectangle> ICON_SIZES = new HashMap<>();
    
    static {
    	//Icon type size
	    ICON_SIZES.put("CELL", new Rectangle(24,24));
	    ICON_SIZES.put("TIMER", new Rectangle(60,45));
	    ICON_SIZES.put("GAME_INDICATOR", new Rectangle(45,45));
	    
	    /*
	     * To add a new icon size:
	     * ICON_SIZES.put("KEY", new Rectangle(width,height));
	     */ 
    }
     /* ─────────────────────────────────────────────────────────
     * ICON FILE REGISTRY
     * ADD NEW ICON REFERENCES HERE
     * ───────────────────────────────────────────────────────── */
    private static final Map<String, String> ICON_FILES = new HashMap<>();

    static {
        // JButton number icons
        ICON_FILES.put("ONE", "1Icon.png");
        ICON_FILES.put("TWO", "2Icon.png");
        ICON_FILES.put("THREE", "3Icon.png");
        ICON_FILES.put("FOUR", "4Icon.png");
        ICON_FILES.put("FIVE", "5Icon.png");
        ICON_FILES.put("SIX", "6Icon.png");
        ICON_FILES.put("SEVEN", "7Icon.png");
        ICON_FILES.put("EIGHT", "8Icon.png");

        // Cell state icons
        ICON_FILES.put("BLANK", "blankIcon.png");
        ICON_FILES.put("FLAG", "flagIcon.png");
        ICON_FILES.put("PRESSED_MINE", "pressedMineIcon.png");
        ICON_FILES.put("REVEALED_MINE", "revealedMineIcon.png");

        // Game indicator icons
        ICON_FILES.put("GAME_LOST", "gameLostIcon.png");
        ICON_FILES.put("GAME_WON", "gameWonIcon.png");
        ICON_FILES.put("NEUTRAL", "neutralIcon.png");

        /*
         * To add a new icon:
         * ICON_FILES.put("KEY", "fileName.png");
         */
    }

    /* ─────────────────────────────────────────────────────────
     * ICON CACHES
     * ───────────────────────────────────────────────────────── */
    private static final Map<String, ImageIcon> BASE_ICON_CACHE = new HashMap<>();
    private static final Map<String, ImageIcon> SCALED_ICON_CACHE = new HashMap<>();

    private IconRegistry() {
        // Prevent instantiation
    }

    /* ─────────────────────────────────────────────────────────
     * PUBLIC API
     * ───────────────────────────────────────────────────────── */

    /**
     * Returns the unscaled icon (original PNG size).
     */
    public static ImageIcon get(String key) {
        return BASE_ICON_CACHE.computeIfAbsent(key, IconRegistry::loadBaseIcon);
    }

    /**
     * Returns a cached, scaled icon using ICON_SIZES.
     * Intended for component usage.
     */
    public static ImageIcon getScaled(String fileKey, String typeKey) {
        return SCALED_ICON_CACHE.computeIfAbsent(
            fileKey,
            k -> scale(get(k), typeKey)
        );
    }

    /* ─────────────────────────────────────────────────────────
     * INTERNAL HELPERS
     * ───────────────────────────────────────────────────────── */

    private static ImageIcon loadBaseIcon(String key) {
        String fileName = ICON_FILES.get(key);

        if (fileName == null) {
            throw new IllegalArgumentException("Unknown icon key: " + key);
        }

        URL url = IconRegistry.class.getResource(ICON_ROOT + fileName);

        if (url == null) {
            throw new IllegalStateException(
                "Icon file not found: " + ICON_ROOT + fileName
            );
        }

        return new ImageIcon(url);
    }

    private static ImageIcon scale(ImageIcon icon, String typeKey) {
    	Rectangle rect = ICON_SIZES.get(typeKey);
    	
        Image scaled = icon.getImage()
                           .getScaledInstance(
                               (int)rect.getWidth(),
                               (int)rect.getHeight(),
                               Image.SCALE_SMOOTH
                           );
        return new ImageIcon(scaled);
    }
}
