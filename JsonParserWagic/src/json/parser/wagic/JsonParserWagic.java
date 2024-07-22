package json.parser.wagic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
<<<<<<< HEAD
=======
import java.nio.charset.StandardCharsets;
>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// @author Eduardo
public class JsonParserWagic {

<<<<<<< HEAD
    private static final String setCode = "ONE";
    private static String filePath = "C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + setCode;
=======
    private static final String setCode = "BLB";
    private static final String filePath = "C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + setCode;
>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa

    public static String getFilePath() {
        return filePath;
    }

<<<<<<< HEAD
    public static void setFilePath(String aFilePath) {
        filePath = aFilePath;
    }

    public static void main(String[] args) {

        boolean createCardsDat = false;

        File directorio = new File(getFilePath());
        directorio.mkdir();

        try {
            FileReader reader = new FileReader(getFilePath() + ".json");
            File myObj = new File(getFilePath() + "\\_cards.dat");
            myObj.createNewFile();
            FileWriter myWriter;
            myWriter = new FileWriter(myObj.getCanonicalPath());
            FileWriter myWriterImages;
            myWriterImages = new FileWriter("C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\image.cvs", true);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray cards = (JSONArray) data.get("cards");

            // Metadata header
            if (createCardsDat) {
                Metadata.printMetadata(data.get("name"), data.get("releaseDate"), data.get("totalSetSize"), myWriter);
            }

            for (Object o : cards) {
                JSONObject card = (JSONObject) o;

                JSONObject identifiers = (JSONObject) card.get("identifiers");
                String primitiveCardName;
                String primitiveRarity;
                String side = "front/";

                primitiveCardName = (String) card.get("faceName") != null ? (String) card.get("faceName") : (String) card.get("name");
                primitiveRarity = card.get("side") != null && "b".equals(card.get("side").toString()) ? "T" : (String) card.get("rarity");
                if (createCardsDat && identifiers.get("multiverseId") != null) {

                    CardDat.generateCardDat(primitiveCardName, identifiers.get("multiverseId"), primitiveRarity, myWriter);
                    CardDat.generateCSV((String) card.get("setCode"), identifiers.get("multiverseId"), (String) identifiers.get("scryfallId"), myWriterImages, side);

                    continue;
                }
                // If card is a reprint, skip it                
=======
    public static void main(String[] args) {

        File directorio = new File(getFilePath());
        directorio.mkdir();

        try ( FileReader reader = new FileReader(getFilePath() + ".json", StandardCharsets.UTF_8);  FileWriter myWriter = new FileWriter(getFilePath() + "\\_cards.dat");  FileWriter myWriterImages = new FileWriter("C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\image.cvs", true)) {

            JSONObject jsonObject = (JSONObject) new JSONParser().parse(reader);
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray cards = (JSONArray) data.get("cards");
            JSONArray tokens = (JSONArray) data.get("tokens");
            JSONObject card;
            JSONArray subtypes;

            JSONObject identifiers;
            String primitiveCardName;
            String primitiveRarity;
            String side;
            // Metadata header
            Metadata.printMetadata(data.get("name"), data.get("releaseDate"), data.get("totalSetSize"), myWriter);

//            for (Object tkn : tokens) {
//                JSONObject token = (JSONObject) tkn;
//                JSONArray reverseRelated = (JSONArray) token.get("reverseRelated");
//                for (Object related : reverseRelated) {
//
//                    for (Object o : cards) {
//                        card = (JSONObject) o;
//                        identifiers = (JSONObject) card.get("identifiers");
//                        JSONObject tokenIdentifiers = (JSONObject) token.get("identifiers");
//                        primitiveCardName = (String) card.get("faceName") != null ? (String) card.get("faceName") : (String) card.get("name");
//                        if (primitiveCardName.equals(related.toString()) && !token.get("name").equals("Copy") && !token.get("name").equals("Energy Reserve") && !token.get("name").equals("Plot") && identifiers.get("multiverseId") != null) {
//                            CardDat.generateCSV(setCode, identifiers.get("multiverseId") + "t", (String) tokenIdentifiers.get("scryfallId"), myWriterImages, "front/");
//                        }
//                    }
//                }
//            }
            for (Object o : cards) {

                card = (JSONObject) o;
                subtypes = (JSONArray) card.get("subtypes");

                identifiers = (JSONObject) card.get("identifiers");
                primitiveCardName = (String) card.get("faceName") != null ? (String) card.get("faceName") : (String) card.get("name");
                //primitiveRarity = card.get("side") != null && "b".equals(card.get("side").toString()) ? "T" : (String) card.get("rarity");
                primitiveRarity = (String) card.get("rarity");
                side = card.get("side") != null && "b".equals(card.get("side").toString()) ? "back/" : "front/";
                if (identifiers.get("multiverseId") != null) {
                    CardDat.generateCardDat(primitiveCardName, identifiers.get("multiverseId"), primitiveRarity, myWriter);
                    //CardDat.generateCSV(setCode, identifiers.get("multiverseId"), (String) identifiers.get("scryfallId"), myWriterImages, side);
                }

                // If card is a reprint, skip it
>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa
                if (card.get("isReprint") != null) {
                    continue;
                }
                String nameHeader = "name=" + primitiveCardName;
                String cardName = primitiveCardName;
<<<<<<< HEAD
                String oracleText = card.get("text").toString();
=======
                String oracleText = null;
                if (card.get("text") != null) {
                    oracleText = card.get("text").toString();
                }
>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa
                JSONArray keywords = (JSONArray) card.get("keywords");
                String manaCost = (String) card.get("manaCost");
                String mana = "mana=" + manaCost;
                String type = "type=";
                String subtype = "";
                String power = "";
                String toughness = "";
                String loyalty = "";
<<<<<<< HEAD
=======
                String colorIndicator = "";
>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa

                if (card.get("supertypes") != null) {
                    JSONArray supertypes = (JSONArray) card.get("supertypes");
                    Iterator supertypesIter = supertypes.iterator();
                    while (supertypesIter.hasNext()) {
                        String supertype = (String) supertypesIter.next();
                        type += supertype + " ";
                    }
                }

                JSONArray types = (JSONArray) card.get("types");
                Iterator typesIter = types.iterator();
                while (typesIter.hasNext()) {
                    String typeStr = (String) typesIter.next();
                    type += typeStr + " ";
                }

<<<<<<< HEAD
                if (card.get("subtypes") != null) {
                    JSONArray subtypes = (JSONArray) card.get("subtypes");
                    if (!subtypes.isEmpty()) {
                        subtype = "subtype=";
                        Iterator subtypesIter = subtypes.iterator();
                        while (subtypesIter.hasNext()) {
                            String subtypeStr = (String) subtypesIter.next();
                            subtype += subtypeStr + " ";
                        }
=======
                if (!subtypes.isEmpty()) {
                    subtype = "subtype=";
                    Iterator subtypesIter = subtypes.iterator();
                    while (subtypesIter.hasNext()) {
                        String subtypeStr = (String) subtypesIter.next();
                        subtype += subtypeStr + " ";
>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa
                    }
                }

                if (card.get("power") != null) {
                    power = "power=" + card.get("power");
                    toughness = "toughness=" + card.get("toughness");
                }

                if (card.get("loyalty") != null) {
                    loyalty = "auto=counter(0/0," + card.get("loyalty") + ",loyalty)";
                }

<<<<<<< HEAD
=======
                if (card.get("colorIndicator") != null) {
                    colorIndicator = "color=" + card.get("colorIndicator");
                }

>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa
                // CARD TAG
                System.out.println("[card]");
                System.out.println(nameHeader);

                if (type.contains("Planeswalker")) {
                    System.out.println(loyalty);
                }
                // ORACLE TEXT
                if (oracleText != null) {
                    OracleTextToWagic.parseOracleText(keywords, oracleText, cardName, type, subtype, (String) card.get("power"), manaCost);
                    System.out.println("text=" + oracleText.replace("\n", " -- "));
                }
<<<<<<< HEAD
                if (!type.contains("Land")) {
                    mana = mana.replace("/", "");
                    System.out.println(mana);
                }
=======
                if (manaCost != null) {
                    mana = mana.replace("/", "");
                    System.out.println(mana);
                }
                if (!colorIndicator.isEmpty()) {
                    colorIndicator = colorIndicator.replace("W", "white")
                            .replace("U", "blue").replace("B", "black")
                            .replace("R", "red").replace("G", "green")
                            .replace("[", "").replace("]", "")
                            .replace("\"", "");

                    System.out.println(colorIndicator);
                }
>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa
                System.out.println(type.trim());
                if (!subtype.isEmpty()) {
                    System.out.println(subtype.trim());
                }
                if (!power.isEmpty()) {
                    System.out.println(power);
                    System.out.println(toughness);
                }
                System.out.println("[/card]\n");
            }
<<<<<<< HEAD
            myWriter.close();
            myWriterImages.close();
=======
>>>>>>> 4ad7065fc5e7b314f4e8a53857e551d8bfa5d7aa

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
        } catch (ParseException | NullPointerException ex) {
            System.out.println("NullPointerException " + ex.getMessage());
        }
    }
}
