package JasonParserWagic;

// @author Eduardo
public class Reprint {

    protected static boolean isReprint(String name) {

        String[] reprints = {
            "Plains", "Island", "Swamp", "Mountain", "Forest",
            };

        for (String reprint : reprints) {
            if (name.equals(reprint)) {
                return true;
            }
        }

        return false;
    }
}