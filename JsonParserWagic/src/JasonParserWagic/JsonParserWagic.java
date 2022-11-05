package JasonParserWagic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// @author Eduardo
public class JsonParserWagic {

    private static final String setCode = "DMC";
    private static String filePath = "C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + setCode + ".json";

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String aFilePath) {
        filePath = aFilePath;
    }

    public static void main(String[] args) {

        boolean createCardsDat = false;

        try {
            FileReader reader = new FileReader(getFilePath());
            File directorio = new File("C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + setCode);
            directorio.mkdir();
            File myObj = new File("C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + setCode + "\\_cards.dat");
            myObj.createNewFile();
            FileWriter myWriter;
            myWriter = new FileWriter("C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + setCode + "\\_cards.dat");
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

            Iterator i = cards.iterator();
            // take each value from the json array separately as a card
            while (i.hasNext()) {
                JSONObject card = (JSONObject) i.next();
                JSONObject identifiers = (JSONObject) card.get("identifiers");
                String primitiveCardName;
                String primitiveRarity;
                String side = "front/";

                if (card.get("faceName") != null) {
                    primitiveCardName = (String) card.get("faceName");
                } else {
                    primitiveCardName = (String) card.get("name");
                }
                if (card.get("side") != null && "b".equals(card.get("side").toString())) {
                    primitiveRarity = "T";
                    side = "back/";
                } else {
                    primitiveRarity = (String) card.get("rarity");
                }

                if (createCardsDat && identifiers.get("multiverseId") != null) {

                    CardDat.generateCardDat(primitiveCardName, identifiers.get("multiverseId"), primitiveRarity, myWriter);
                    CardDat.generateCSV((String) card.get("setCode"), identifiers.get("multiverseId"), (String) identifiers.get("scryfallId"), myWriterImages, side);

                    continue;
                }

                // If card is a reprint, skip it                
                if (card.get("isReprint") != null) {
                    continue;
                }

                String nameHeader = "name=" + primitiveCardName;
                String cardName = primitiveCardName;
                String oracleText = card.get("text").toString();
                String manaCost = (String) card.get("manaCost");
                String mana = "mana=" + manaCost;
                String type = "type=";
                String subtype = "";
                String power = "";
                String toughness = "";
                String loyalty = "";

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

                if (card.get("subtypes") != null) {
                    JSONArray subtypes = (JSONArray) card.get("subtypes");
                    if (!subtypes.isEmpty()) {
                        subtype = "subtype=";
                        Iterator subtypesIter = subtypes.iterator();
                        while (subtypesIter.hasNext()) {
                            String subtypeStr = (String) subtypesIter.next();
                            subtype += subtypeStr + " ";
                        }
                    }
                }

                if (card.get("power") != null) {
                    power = "power=" + card.get("power");
                    toughness = "toughness=" + card.get("toughness");
                }

                if (card.get("loyalty") != null) {
                    loyalty = "auto=counter(0/0," + card.get("loyalty") + ",loyalty)";
                }

                // CARD TAG
                System.out.println("[card]");
                System.out.println(nameHeader);

                if (type.contains("Planeswalker")) {
                    System.out.println(loyalty);
                }
                // ORACLE TEXT
                if (oracleText != null) {
                    OracleTextToWagic.parseOracleText(oracleText, cardName, type, subtype, (String) card.get("power"), manaCost);
                    System.out.println("text=" + oracleText.replace("\n", " -- "));
                }
                if (!type.contains("Land")) {
                    mana = mana.replace("/", "");
                    System.out.println(mana);
                }
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
            myWriter.close();
            myWriterImages.close();

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
        } catch (ParseException | NullPointerException ex) {
            System.out.println("NullPointerException " + ex.getMessage());
        }
    }
}
