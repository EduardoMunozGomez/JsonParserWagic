package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class BasicOrReprint {

    protected static boolean isReprintOrBasic(String name) {
        String[] reprints = {
            "Colossal Dreadmaw",
            "Legion Conquistador",
            "Raptor Companion",
            "Sailor of Means",
            "Aggressive Urge",
            "Divine Verdict",
            "Evolving Wilds",
            "Hunt the Weak",
            "Naturalize",
            "Negate",
            "Plummet",
            "Recover",
            "Shatter",
            "Silvergill Adept",
            "Strider Harness",
            "Traveler's Amulet",
            "Forsaken Sanctuary",
            "Highland Lake",
            "Foul Orchard",
            "Stone Quarry",
            "Woodland Stream"
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
