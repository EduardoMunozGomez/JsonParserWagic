package JasonParserWagic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// @author Eduardo
public class JsonParserWagic {

    private static String filePath = "C:\\Users\\Eduardo\\Downloads\\MTGJSON\\med.json";

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String aFilePath) {
        filePath = aFilePath;
    }

    public static void main(String[] args) {

        boolean createCardsDat = true;

        try {
            FileReader reader = new FileReader(getFilePath());
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray cards = (JSONArray) data.get("cards");

            // Metadata header
            if (createCardsDat) {
                Metadata.printMetadata(data.get("name"), data.get("releaseDate"), data.get("baseSetSize"));
            }

            Iterator i = cards.iterator();
            // take each value from the json array separately as a card
            while (i.hasNext()) {
                JSONObject card = (JSONObject) i.next();
                JSONObject identifiers = (JSONObject) card.get("identifiers");

                if (createCardsDat) {
                    CardDat.generateCardDat((String) card.get("name"), identifiers.get("multiverseId"), (String) card.get("rarity"));
                    continue;
                }

                // If card is a reprint, skip it
                JSONArray printingsArray = (JSONArray) card.get("printings");
                if (printingsArray.size() > 3) {
                    continue;
                }
                if (Reprint.isReprint((String) card.get("name"))) {
                    continue;
                }

                String nameHeader = "name=" + card.get("name");
                String cardName = card.get("name").toString();
                String oracleText = (String) card.get("text");
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

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
        } catch (ParseException | NullPointerException ex) {
            System.out.println("NullPointerException " + ex.getMessage());
        }
    }
}