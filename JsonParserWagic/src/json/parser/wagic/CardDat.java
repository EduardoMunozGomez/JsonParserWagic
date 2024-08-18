package json.parser.wagic;

// @author Eduardo
import java.io.FileWriter;
import java.io.IOException;

public class CardDat {

    // Constants for rarities
    private static final String COMMON = "C";
    private static final String UNCOMMON = "U";
    private static final String RARE = "R";
    private static final String MYTHIC = "M";
    private static final String SPECIAL = "S";
    private static final String LAND = "L";

    // Constant for image URL base
    private static final String SCRYFALL_URL = "https://cards.scryfall.io/large/";

    // Array of basic lands
    private static final String[] BASIC_LANDS = {"Plains", "Island", "Swamp", "Mountain", "Forest"};

    public static void generateCardDat(String primitive, Object id, String rarity, FileWriter myWriter) {
        rarity = getRarityCode(rarity);

        if (isBasicLand(primitive)) {
            rarity = LAND;
        }

        try {
            myWriter.write("[card]\n");
            myWriter.write("primitive=" + primitive + "\n");
            myWriter.write("id=" + id + "\n");
            myWriter.write("rarity=" + rarity + "\n");
            myWriter.write("[/card]\n");
        } catch (IOException e) {
            System.err.println("Error writing card data: " + e.getMessage());
        }
    }

    private static String getRarityCode(String rarity) {
        switch (rarity.toLowerCase()) {
            case "common":
                return COMMON;
            case "uncommon":
                return UNCOMMON;
            case "rare":
                return RARE;
            case "mythic":
                return MYTHIC;
            case "special":
                return SPECIAL;
            default:
                return rarity; // Return original value if no match
        }
    }

    private static boolean isBasicLand(String primitive) {
        for (String land : BASIC_LANDS) {
            if (primitive.equals(land)) {
                return true;
            }
        }
        return false;
    }

    public static void generateCSV(String primitive, Object id, String scryfallId, FileWriter myWriterImages, String side) {
        try {
            String url = SCRYFALL_URL + side + scryfallId.substring(0, 1) + "/" + scryfallId.substring(1, 2) + "/" + scryfallId + ".jpg";
            myWriterImages.write(primitive + ";" + id + ";" + url + "\n");
        } catch (IOException e) {
            System.err.println("Error in generateCSV: " + e.getMessage());
        }
    }
}
