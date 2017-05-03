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

    private static String filePath = "C:\\Users\\Eduardo\\Downloads\\akh.json";

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
            // innerObj is a card
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

                if (oracleText != null) {

                    if (type.contains("Creature")) {
                        abilities = Abilities.processAbilities(oracleText);
                    }
                    if (type.contains("Enchantment")) {
                        abilities = Abilities.processEnchantmentAbilities(oracleText);
                    }
                    if (type.contains("Land")) {
                        abilities = Abilities.processLandAbilities(oracleText);
                    }

                    // Evergreen mechanics
                    String cast = AutoLine.processOracleTextCast(oracleText);
                    String manaAbility = AutoLine.processOracleTextManaAbility(oracleText, subtype);
                    String combatDamage = AutoLine.processOracleTextCombatDamage(oracleText);
                    String etb = AutoLine.processOracleTextETB(oracleText, cardName);
                    String firebreating = AutoLine.processOracleTextFirebreting(oracleText, cardName);
                    String oppCasts = AutoLine.processOracleTextOppCasts(oracleText);
                    String regenerate = AutoLine.processOracleTextRegenerate(oracleText);
                    String dies = AutoLine.processOracleTextThisDies(oracleText, cardName);
                    String targeted = AutoLine.processOracleTextTargeted(oracleText, cardName);
                    String transforms = AutoLine.processOracleTextTransforms(oracleText, cardName);
                    String upkeep = AutoLine.processOracleTextUpkeep(oracleText);
                    String weak = AutoLine.processOracleTextWeak(oracleText);
                    String prowess = AutoLine.processOracleTextProwess(oracleText);
                    String attacks = AutoLine.processOracleTextAttacks(oracleText, cardName);
                    String create = AutoLine.processOracleTextCreate(oracleText);
                    String discard = AutoLine.processOracleTextDiscard(oracleText);
                    String takeControl = AutoLine.processOracleTextTakeControl(oracleText);

                    String cycling = AutoLine.processOracleTextCycling(oracleText);
                    String embalm = AutoLine.processOracleTextEmbalm(oracleText);
                    String exert = AutoLine.processOracleTextExert(oracleText);

                    if (!abilities.isEmpty()) {
                        System.out.println("abilities=" + abilities.trim());
                    }

                    if (type.contains("Instant") || type.contains("Sorcery")) {
                        String target = AutoLine.processOracleTextMyTarget(oracleText, "instantOrSorcery");
                        String exileDestroyDamage = AutoLine.processOracleTextExileDestroyDamage(oracleText);
                        if (!target.isEmpty()) {
                            System.out.println(target);
                        }
                        if (!exileDestroyDamage.isEmpty()) {
                            System.out.println(exileDestroyDamage);
                        } else {
                            System.out.println("auto=");
                        }
                    }
                    if (subtype.contains("Aura")) {
                        System.out.println(AutoLine.processOracleTextMyTarget(oracleText, "aura"));
                        String auraEquipBonus = AutoLine.processOracleTextAuraEquipBonus(oracleText);
                        if (!auraEquipBonus.equals("")) {
                            System.out.println(auraEquipBonus);
                        }
                    }

                    if (!cast.equals("")) {
                        System.out.println(cast);
                    }
                    if (!etb.equals("")) {
                        System.out.println(etb);
                    }
                    if (!manaAbility.equals("")) {
                        System.out.println(manaAbility);
                    }
                    if (!combatDamage.equals("")) {
                        System.out.println(combatDamage);
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
                    if (!dies.equals("")) {
                        System.out.println(dies);
                    }
                    if (!takeControl.equals("")) {
                        System.out.println(takeControl);
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
                    if (!create.equals("")) {
                        System.out.println(create);
                    }
                    if (!discard.equals("")) {
                        System.out.println(discard);
                    }
                    if (!cycling.equals("")) {
                        System.out.println(cycling);
                    }
                    if (!embalm.equals("")) {
                        System.out.println(embalm);
                    }
                    if (!exert.equals("")) {
                        System.out.println(exert);
                    }
                    if (subtype.contains("Equipment")) {
                        System.out.println(AutoLine.processOracleTextAuraEquipBonus(oracleText));
                        System.out.println(AutoLine.processOracleTextEquipCost(oracleText));
                    }
                    System.out.println("text=" + oracleText.replace("\n", " -- "));
                }
                if (mana != null && !type.contains("Land")) {
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
            ex.printStackTrace();
        }
    }
}
