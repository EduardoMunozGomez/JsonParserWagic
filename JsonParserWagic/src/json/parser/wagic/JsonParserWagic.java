package json.parser.wagic;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// @author Eduardo
public class JsonParserWagic {

    // If commander or complemmentary set, else the set code is the same on both
    private static final String SET_CODE = "AKR";
    // For sets like commander expansions to obtain the tokens from the main set
    private static final String MAIN_SET_CODE = "AKH";
    private static final String FILE_PATH = "C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + SET_CODE;
    private static final String MAIN_SET_PATH = "C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\" + MAIN_SET_CODE;
    private static final Boolean ALL_RESOURCES = true;

    public static String getFilePath() {
        return FILE_PATH;
    }

    public static String getMainSetPath() {
        return MAIN_SET_PATH;
    }

    public static void main(String[] args) {

        File directorio = new File(getFilePath());
        directorio.mkdir();

        try (FileReader reader = new FileReader(getFilePath() + ".json", StandardCharsets.UTF_8); FileReader readerMain = new FileReader(getMainSetPath() + ".json", StandardCharsets.UTF_8); FileWriter myWriter = new FileWriter(getFilePath() + "\\_cards.dat"); FileWriter myWriterImages = new FileWriter("C:\\Users\\Eduardo_\\Downloads\\MTGJSON\\image.cvs", true)) {

            JSONObject jsonObject = (JSONObject) new JSONParser().parse(reader);
            JSONObject jsonObjectMainSet = (JSONObject) new JSONParser().parse(readerMain);
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONObject dataMainSet = (JSONObject) jsonObjectMainSet.get("data");
            JSONArray cards = (JSONArray) data.get("cards");
            JSONArray tokens = (JSONArray) data.get("tokens");
            JSONArray tokensMainSet = (JSONArray) dataMainSet.get("tokens");
            JSONObject card = null;
            JSONArray subtypes;

            List<String> skipLayouts = Arrays.asList("adventure", "aftermath", "split");
            JSONObject identifiers = null;
            int multiverseId;
            String primitiveCardName = null;
            String primitiveRarity;
            String side;

            if (ALL_RESOURCES) {
                //Metadata header
                Metadata.printMetadata(data.get("name"), data.get("releaseDate"), data.get("totalSetSize"), myWriter);
                Tokens.obtainTokens(cards, tokensMainSet, card, identifiers, primitiveCardName, SET_CODE, myWriterImages);
                Tokens.obtainTokens(cards, tokens, card, identifiers, primitiveCardName, SET_CODE, myWriterImages);
            }
            for (Object o : cards) {
                card = (JSONObject) o;
                subtypes = (JSONArray) card.get("subtypes");

                identifiers = (JSONObject) card.get("identifiers");

                String layout = card.get("layout").toString();
                String faceName = (String) card.get("faceName");
                String name = (String) card.get("name");                

                primitiveCardName = (!skipLayouts.contains(layout) && faceName != null) ? faceName : name;
                primitiveRarity = card.get("side") != null && "b".equals(card.get("side").toString()) ? "T" : (String) card.get("rarity");
                //primitiveRarity = (String) card.get("rarity");
                side = card.get("side") != null && "b".equals(card.get("side").toString()) ? "back/" : "front/";
                // Avoid split cards duplicate
                if (skipLayouts.contains(card.get("layout").toString()) && side.equals("back/")) {
                    continue;
                }
                if (ALL_RESOURCES && identifiers.get("multiverseId") != null) {
                    multiverseId = Integer.parseInt(identifiers.get("multiverseId").toString());
                    if (side.equals("back/")) {
                        multiverseId = ++multiverseId;
                    }
                    CardDat.generateCardDat(primitiveCardName, (Object) multiverseId, primitiveRarity, myWriter);
                    CardDat.generateCSV(SET_CODE, (Object) multiverseId, (String) identifiers.get("scryfallId"), myWriterImages, side);
                }
                // If card is a reprint, skip it
//                if (card.get("isReprint") != null) {
//                    continue;
//                }
                String nameHeader = "name=" + primitiveCardName;
                String oracleText = null;
                if (card.get("text") != null) {
                    oracleText = card.get("text").toString();
                    // Substitute special characters
                    oracleText = oracleText.replace("−", "-");
                    oracleText = oracleText.replace("—", "-");
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
                    OracleTextToWagic.parseOracleText(keywords, oracleText, primitiveCardName, type, subtype, (String) card.get("power"), manaCost);
                    oracleText = oracleText.replace("•", "-");
                    System.out.println("text=" + oracleText.replace("\n", " -- "));
                }
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
