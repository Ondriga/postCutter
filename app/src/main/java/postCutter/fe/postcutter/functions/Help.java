package postCutter.fe.postcutter.functions;

public class Help {
    public static final String SHARED_PREFS = "sharedPrefsHelp";
    public static final String DETAIL_HELP = "detailSharedPrefsHelp";
    public static final String CROP_HELP = "cropSharedPrefsHelp";
    public static final String REMOVE_HELP = "removeSharedPrefsHelp";

    private final String text;
    private final int image;

    public Help(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public int getImage() {
        return image;
    }
}
