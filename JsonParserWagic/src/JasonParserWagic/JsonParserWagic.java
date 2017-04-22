package JasonParserWagic;

/**
 * @author Eduardo
 */
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParserWagic {

    private static String filePath = "C:\\Users\\Eduardo\\Downloads\\AKH.json";

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String aFilePath) {
        filePath = aFilePath;
    }

    public static void main(String[] args) {

        try {
            FileReader reader = new FileReader(getFilePath());
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

            JSONArray cards = (JSONArray) jsonObject.get("cards");

            Iterator i = cards.iterator();

            // take each value from the json array separately
            while (i.hasNext()) {
                JSONObject innerObj = (JSONObject) i.next();

                boolean generateID = false;
                if (generateID) {
                    CardDat.generateCardDat(innerObj.get("name"), innerObj.get("multiverseid"), innerObj.get("rarity"));
                    continue;
                }
                if (BasicOrReprint.isReprintOrBasic((String) innerObj.get("name"))) {
                    continue;
                }

                String nameHeader = "name=" + innerObj.get("name");
                String cardName = innerObj.get("name").toString();
                String oracleText = (String) innerObj.get("text");

                // Evergreen mechanics                
                String combatDamage = AutoWagicLine.processOracleTextCombatDamage(oracleText);
                String etb = AutoWagicLine.processOracleTextETB(oracleText, cardName);
                String etbTapped = AutoWagicLine.processOracleTextETBTapped(oracleText, cardName);
                String firebreating = AutoWagicLine.processOracleTextFirebreting(oracleText, cardName);
                String oppCasts = AutoWagicLine.processOracleTextOppCasts(oracleText);
                String regenerate = AutoWagicLine.processOracleTextRegenerate(oracleText);
                String thisDies = AutoWagicLine.processOracleTextThisDies(oracleText, cardName);
                String targeted = AutoWagicLine.processOracleTextTargeted(oracleText, cardName);
                String transforms = AutoWagicLine.processOracleTextTransforms(oracleText, cardName);
                String upkeep = AutoWagicLine.processOracleTextUpkeep(oracleText);
                String weak = AutoWagicLine.processOracleTextWeak(oracleText);
                String prowess = AutoWagicLine.processOracleTextProwess(oracleText);
                String attacks = AutoWagicLine.processOracleTextAttacks(oracleText, cardName);
                
                String cycling = AutoWagicLine.processOracleTextCycling(oracleText);     

                // DTK
//                String bolster = AutoWagicLine.processOracleTextBolster(oracleText);
//                String dash = AutoWagicLine.processOracleTextDash(oracleText);
//                String exploit = AutoWagicLine.processOracleTextExploit(oracleText);
//                String faceUp = AutoWagicLine.processOracleTextFaceUp(oracleText);
//                String morph = AutoWagicLine.processOracleTextMorph(oracleText);

                String abilities = "";
                String mana = "mana=" + innerObj.get("manaCost");
                String type = "type=";
                String subtype = "";
                String power = "";
                String toughness = "";

                if (innerObj.get("supertypes") != null) {
                    JSONArray supertypes = (JSONArray) innerObj.get("supertypes");
                    Iterator supertypesIter = supertypes.iterator();
                    while (supertypesIter.hasNext()) {
                        String supertype = (String) supertypesIter.next();
                        type += supertype + " ";
                    }
                }

                JSONArray types = (JSONArray) innerObj.get("types");
                Iterator typesIter = types.iterator();
                while (typesIter.hasNext()) {
                    String typeStr = (String) typesIter.next();
                    type += typeStr + " ";
                }

                if (type.contains("Creature")) {
                    abilities = Abilities.processOracleText(oracleText);
                }

                if (innerObj.get("subtypes") != null) {
                    subtype = "subtype=";
                    JSONArray subtypes = (JSONArray) innerObj.get("subtypes");
                    Iterator subtypesIter = subtypes.iterator();
                    while (subtypesIter.hasNext()) {
                        String subtypeStr = (String) subtypesIter.next();
                        subtype += subtypeStr + " ";
                    }
                }

                if (innerObj.get("power") != null) {
                    power = "power=" + innerObj.get("power");
                    toughness = "toughness=" + innerObj.get("toughness");
                }

                // CARD TAG
                System.out.println("[card]");
                System.out.println(nameHeader);
                if (!abilities.equals("")) {
                    System.out.println("abilities=" + abilities.trim());
                }
//                if (!bolster.equals("")) {
//                    System.out.println(bolster);
//                }
//                if (!dash.equals("")) {
//                    System.out.println(dash);
//                }
//                if (!exploit.equals("")) {
//                    System.out.println(exploit);
//                }
//                if (!morph.equals("")) {
//                    System.out.println(morph);
//                }
//                if (!faceUp.equals("")) {
//                    System.out.println(faceUp);
//                }
                if (!combatDamage.equals("")) {
                    System.out.println(combatDamage);
                }                
                if (!etb.equals("")) {
                    System.out.println(etb);
                }
                if (!etbTapped.equals("")) {
                    System.out.println(etbTapped);
                }                
                if (!firebreating.equals("")) {
                    System.out.println(firebreating);
                }                
                if (!oppCasts.equals("")) {
                    System.out.println(oppCasts);
                }
                if (!prowess.equals("")) {
                    System.out.println(prowess);
                }
                if (!regenerate.equals("")) {
                    System.out.println(regenerate);
                }
                if (!thisDies.equals("")) {
                    System.out.println(thisDies);
                }
                if (!targeted.equals("")) {
                    System.out.println(targeted);
                }
                if (!transforms.equals("")) {
                    System.out.println(transforms);
                }
                if (!upkeep.equals("")) {
                    System.out.println(upkeep);
                }
                if (!weak.equals("")) {
                    System.out.println(weak);
                }
                if (!attacks.equals("")) {
                    System.out.println(attacks);
                }
                if (!cycling.equals("")) {
                    System.out.println(cycling);
                }
                if (subtype.contains("Aura")) {
                    System.out.println(AutoWagicLine.processOracleTextAura(oracleText));
                }
                if (subtype.contains("Equipment")) {
                    System.out.println(AutoWagicLine.processOracleTextEquip(oracleText));
                }
//              System.out.println("auto=");
                if (oracleText != null) {
                    System.out.println("text=" + oracleText.replace("\n", " -- "));
                }
                if (mana != null && !type.contains("Land")) {
                    System.out.println(mana);
                }
                System.out.println(type.trim());
                if (!subtype.equals("")) {
                    System.out.println(subtype.trim());
                }
                if (!power.equals("")) {
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
