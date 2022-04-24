package postCutter.fe.postcutter.functions;

public class Help {
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
