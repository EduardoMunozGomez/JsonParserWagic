package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class BasicOrReprint {

    protected static boolean isReprintOrBasic(String name) {
        String[] reprints
                = {"Ancient Crab",
                    "Aven Mindcensor",
                    "Brute Strength",
                    "Cancel",
                    "Essence Scatter",
                    "Evolving Wilds",
                    "Fling",
                    "Giant Spider",
                    "Gravedigger",
                    "Impeccable Timing",
                    "Mighty Leap",
                    "Magma Spray",
                    "Renewed Faith",
                    "Spidery Grasp",
                    "Tormenting Voice",
                    "Unburden"};

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
