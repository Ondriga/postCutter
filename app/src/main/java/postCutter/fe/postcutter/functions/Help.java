/*
 * Source code for the frontend of Bachelor thesis.
 * Help class
 *
 * (C) Patrik Ondriga (xondri08)
 */

package postCutter.fe.postcutter.functions;

/**
 * Representing help information in tutorial.
 */
public class Help {
    /// Constant of key for access to store values.
    public static final String SHARED_PREFS = "sharedPrefsHelp";
    /// Constant of key for flag if detail help was showed at least one.
    public static final String DETAIL_HELP = "detailSharedPrefsHelp";
    /// Constant of key for flag if crop help was showed at least one.
    public static final String CROP_HELP = "cropSharedPrefsHelp";
    /// Constant of key for flag if replace help was showed at least one.
    public static final String REMOVE_HELP = "removeSharedPrefsHelp";

    /// Text of help.
    private final String text;
    /// Gif image of help.
    private final int image;

    /**
     * Constructor.
     *
     * @param text  help text.
     * @param image help gif image.
     */
    public Help(String text, int image) {
        this.text = text;
        this.image = image;
    }

    /**
     * Getter for help text.
     *
     * @return help text.
     */
    public String getText() {
        return text;
    }

    /**
     * Getter for help gif image.
     *
     * @return help gif image.
     */
    public int getImage() {
        return image;
    }
}
