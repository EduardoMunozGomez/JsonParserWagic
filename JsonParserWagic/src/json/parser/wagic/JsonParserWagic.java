package json.parser.wagic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// @author Eduardo
public class JsonParserWagic {

    private static final String setCode = "MOM";
    private static final String filePath = "C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + setCode;

    public static String getFilePath() {
        return filePath;
    }

    public static void main(String[] args) {

        boolean createCardsDat = true;

        File directorio = new File(getFilePath());
        directorio.mkdir();

        try {
            FileReader reader = new FileReader(getFilePath() + ".json", StandardCharsets.UTF_8);
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
                JSONArray subtypes = (JSONArray) card.get("subtypes");

                JSONObject identifiers = (JSONObject) card.get("identifiers");
                String primitiveCardName;
                String primitiveRarity;
                String side;

                primitiveCardName = (String) card.get("faceName") != null ? (String) card.get("faceName") : (String) card.get("name");
                primitiveRarity = card.get("side") != null && "b".equals(card.get("side").toString()) ? "T" : (String) card.get("rarity");
                side = card.get("side") != null && "b".equals(card.get("side").toString()) ? "back/" : "front/";
                if (createCardsDat && identifiers.get("multiverseId") != null) {
                    CardDat.generateCardDat(primitiveCardName, identifiers.get("multiverseId"), primitiveRarity, myWriter);
                    CardDat.generateCSV((String) card.get("setCode"), identifiers.get("multiverseId"), (String) identifiers.get("scryfallId"), myWriterImages, side);
                }
                // If card is a reprint, skip it                
                if (card.get("isReprint") != null) {
                    continue;
                }
                String nameHeader = "name=" + primitiveCardName;
                String cardName = primitiveCardName;
                String oracleText = null;
                if (card.get("text") != null) {
                    oracleText = card.get("text").toString();
                }
                JSONArray keywords = (JSONArray) card.get("keywords");
                String manaCost = (String) card.get("manaCost");
                String mana = "mana=" + manaCost;
                String type = "type=";
                String subtype = "";
                String power = "";
                String toughness = "";
                String loyalty = "";
                String colorIndicator = "";

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

                if (!subtypes.isEmpty()) {
                    subtype = "subtype=";
                    Iterator subtypesIter = subtypes.iterator();
                    while (subtypesIter.hasNext()) {
                        String subtypeStr = (String) subtypesIter.next();
                        subtype += subtypeStr + " ";
                    }
                }

                if (card.get("power") != null) {
                    power = "power=" + card.get("power");
                    toughness = "toughness=" + card.get("toughness");
                }

                if (card.get("loyalty") != null) {
                    loyalty = "auto=counter(0/0," + card.get("loyalty") + ",loyalty)";
                }

                if (card.get("colorIndicator") != null) {
                    colorIndicator = "color=" + card.get("colorIndicator");
                }

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
                if (manaCost != null) {
                    mana = mana.replace("/", "");
                    System.out.println(mana);
                }
                if (!colorIndicator.isEmpty()) {
                    colorIndicator = colorIndicator.replace("W", "white");
                    colorIndicator = colorIndicator.replace("U", "blue");
                    colorIndicator = colorIndicator.replace("B", "black");
                    colorIndicator = colorIndicator.replace("R", "red");
                    colorIndicator = colorIndicator.replace("G", "green");
                    colorIndicator = colorIndicator.replace("[", "");
                    colorIndicator = colorIndicator.replace("]", "");
                    colorIndicator = colorIndicator.replace("\"", "");

                    System.out.println(colorIndicator);
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
