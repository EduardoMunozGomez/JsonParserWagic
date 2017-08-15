package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class BasicOrReprint {

    protected static boolean isReprintOrBasic(String name) {
        String[] reprints = {
            "Sandblast",
            "Unsummon",
            "Kindled Fury",
            "Manalith",
            "Traveler's Amulet"
        };

        String[] basics = {"Plains", "Island", "Swamp", "Mountain", "Forest"};

        for (String reprint : reprints) {
            if (name.equals(reprint)) {
                return true;
            }
        }

        for (String basic : basics) {
            if (name.equals(basic)) {
                return true;
            }
        }

        return false;
    }
}
