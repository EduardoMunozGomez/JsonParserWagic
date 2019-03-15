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

    private static String filePath = "C:\\Users\\Eduardo\\Downloads\\grn.json";

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
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONArray cards = (JSONArray) jsonObject.get("cards");

            // Metadata
            if (createCardsDat) {
                Metadata.printMetadata(jsonObject.get("name"), jsonObject.get("releaseDate"), jsonObject.get("totalSetSize"));
            }

            Iterator i = cards.iterator();
            // take each value from the json array separately
            // innerObj is a card
            while (i.hasNext()) {
                JSONObject card = (JSONObject) i.next();

                if (createCardsDat) {
                    CardDat.generateCardDat((String) card.get("name"), card.get("multiverseId"),(String) card.get("rarity"));
                    continue;
                }

                JSONArray printingsArray = (JSONArray) card.get("printings");
                if (printingsArray.size() > 3) {
                    continue;
                }

                if (BasicOrReprint.isReprintOrBasic((String) card.get("name"))) {
                    continue;
                }

                String nameHeader = "name=" + card.get("name");
                String cardName = card.get("name").toString();
                String oracleText = (String) card.get("text");
                String abilities = "";
                String manaCost = (String) card.get("manaCost");
                String mana = "mana=" + manaCost;
                String type = "type=";
                String subtype = "";
                String power = "";
                String toughness = "";

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

                // CARD TAG
                System.out.println("[card]");
                System.out.println(nameHeader);

                if (oracleText != null) {
                    // Keywords
                    if (type.contains("Creature")) {
                        abilities = Abilities.processAbilities(oracleText);
                    }
                    if (type.contains("Enchantment")) {
                        abilities = Abilities.processEnchantmentAbilities(oracleText);
                    }
                    if (type.contains("Instant")||type.contains("Sorcery")) {
                        abilities = Abilities.processInstantOrSorceryAbilities(oracleText);
                    }
                    if (type.contains("Land")) {
                        abilities = Abilities.processLandAbilities(oracleText);
                    }

                    // Evergreen mechanics
                    String cast = AutoLine.processOracleTextCast(oracleText);
                    String activatedAbility = AutoLine.processOracleActivatedAbilityCost(oracleText, cardName);
                    String manaAbility = AutoLine.processOracleTextManaAbility(oracleText, subtype);
                    String combatDamage = AutoLine.processOracleTextCombatDamage(oracleText);
                    String etb = AutoLine.processOracleTextETB(oracleText, cardName);
                    String firebreating = AutoLine.processOracleTextFirebreting(oracleText, cardName);
                    String oppCasts = AutoLine.processOracleTextOppCasts(oracleText);
                    String dies = AutoLine.processOracleTextThisDies(oracleText, cardName);
                    String targeted = AutoLine.processOracleTextTargeted(oracleText, cardName);
                    String upkeep = AutoLine.processOracleTextUpkeep(oracleText);
                    String weak = AutoLine.processOracleTextWeak(oracleText);
                    String attacks = AutoLine.processOracleTextAttacks(oracleText, cardName, (String) card.get("power"));
                    String draw = AutoLine.processOracleDraw(oracleText);
                    String discard = AutoLine.processOracleDiscarded(oracleText);
                    String takeControl = AutoLine.processOracleTextTakeControl(oracleText);
                    String scry = AutoLine.processOracleScry(oracleText);
                    String cantBeBlockedBy = AutoLine.processOracleCantBeBlockedBy(oracleText);
                    String lord = AutoLine.processOracleLord(oracleText, type);     
                    String asLongAsIsMyTurn = AutoLine.processasLongAsIsMyTurn(oracleText, type);     

                    String convoke = AutoLineGRN.processOracleConvoke(oracleText);
                    String surveil = AutoLineGRN.processOracleSurveil(oracleText);
                    String undergrowth = AutoLineGRN.processOracleUndergrowth(oracleText);
                    
                    String addendum = AutoLineGRN.processOracleAddendum(oracleText);
                    String riot = AutoLineGRN.processOracleRiot(oracleText);
                    String spectacle = AutoLineGRN.processOracleSpectacle(oracleText);
                    String kicker = AutoLineGRN.processOracleKicker(oracleText);
                    //String prowess = AutoLine.processOracleTextProwess(oracleText);
                    //String transforms = AutoLine.processOracleTextTransforms(oracleText, cardName);
                    //String regenerate = AutoLine.processOracleTextRegenerate(oracleText);
                    //String enrage = AutoLine.processOracleEnraged(oracleText);
                    //String cycling = AutoLine.processOracleTextCycling(oracleText);
                    //String embalm = AutoLine.processOracleTextEmbalm(oracleText);
                    //String exert = AutoLine.processOracleTextExert(oracleText);
                    //String afflict = AutoLine.processOracleTextAfflict(oracleText, cardName);
                    //String eternalize = AutoLine.processOracleTextEternalize(oracleText);
                    //String raid = AutoLine.processOracleTextRaid(oracleText);
                    if (!abilities.isEmpty()) {
                        System.out.println("abilities=" + abilities.trim());
                    }

                    //INSTANT, SORCERY
                    if (type.contains("Instant") || type.contains("Sorcery")) {
                        String target = AutoLine.processOracleTextMyTarget(oracleText, "InstantOrSorcery");
                        String exileDestroyDamage = AutoLine.processOracleTextExileDestroyDamage(oracleText);
                        String gains = AutoLine.processOracleTextAuraEquipBonus(oracleText, "InstantOrSorcery");
                        String create = AutoLine.processOracleCreate(oracleText);
                        String gainLife = AutoLine.processOracleGainLife(oracleText);
                        String exiledeath = AutoLine.processOracleExileDeath(oracleText);                         
                        String putA = AutoLine.processOraclePutA(oracleText, type);

                        String jumpStart = AutoLineGRN.processOracleJumpStart(oracleText, manaCost);
                        
                        if (!target.isEmpty()) {
                            System.out.println(target);
                        }
                        if (!exileDestroyDamage.isEmpty()) {
                            System.out.println(exileDestroyDamage);
                        }
                        if (!gains.isEmpty()) {
                            System.out.println(gains);
                        }                        
                        if (!create.isEmpty()) {
                            System.out.println("auto=" + create);
                        }
                        if (!gainLife.isEmpty()) {
                            System.out.println("auto=" + gainLife);
                        }
                        if (!exiledeath.isEmpty()) {
                            System.out.println("auto=" + exiledeath);
                        }
                        if (!putA.isEmpty()) {
                            System.out.println("auto=" + putA);
                        }
                        if (!jumpStart.isEmpty()) {
                            System.out.println(jumpStart);
                        }
                        //else {
                          //System.out.println("auto=");
                        //}
                    }
                    // AURA
                    if (subtype.contains("Aura")) {
                        System.out.println(AutoLine.processOracleTextMyTarget(oracleText, "Aura"));
                        String auraEquipBonus = AutoLine.processOracleTextAuraEquipBonus(oracleText, "Aura");
                        if (!auraEquipBonus.isEmpty()) {
                            System.out.println(auraEquipBonus);
                        }
                    }
                    // EQUIPMENT
                    if (subtype.contains("Equipment")) {
                        System.out.println(AutoLine.processOracleTextAuraEquipBonus(oracleText, "Equipment"));
                        System.out.println(AutoLine.processOracleTextEquipCost(oracleText));
                    }
                    
                    if (kicker.length() > 0) {
                        System.out.println(kicker);
                    }
                    if (addendum.length() > 0) {
                        System.out.println(addendum);
                    }
                    if (cast.length() > 0) {
                        System.out.println(cast);
                    }
                    if (etb.length() > 0) {
                        System.out.println(etb);
                    }
                    if (riot.length() > 0) {
                        System.out.println(riot);
                    }
                    if (activatedAbility.length() > 0) {
                        System.out.println(activatedAbility);
                    }
                    if (manaAbility.length() > 0) {
                        System.out.println(manaAbility);
                    }
                    if (combatDamage.length() > 0) {
                        System.out.println(combatDamage);
                    }
                    if (firebreating.length() > 0) {
                        System.out.println(firebreating);
                    }
                    if (oppCasts.length() > 0) {
                        System.out.println(oppCasts);
                    }
                    
//                    if (regenerate.length() > 0) {
//                        System.out.println(regenerate);
//                    }
                    if (dies.length() > 0) {
                        System.out.println(dies);
                    }
                    if (takeControl.length() > 0) {
                        System.out.println(takeControl);
                    }
                    if (targeted.length() > 0) {
                        System.out.println(targeted);
                    }
//                    if (transforms.length() > 0) {
//                        System.out.println(transforms);
//                    }
                    if (upkeep.length() > 0) {
                        System.out.println(upkeep);
                    }
                    if (weak.length() > 0) {
                        System.out.println(weak);
                    }
                    if (attacks.length() > 0) {
                        System.out.println(attacks);
                    }
                    if (lord.length() > 0) {
                        System.out.println(lord);
                    }
                    if (cantBeBlockedBy.length() > 0) {
                        System.out.println(cantBeBlockedBy);
                    }
                    if (discard.length() > 0) {
                        System.out.println(discard);
                    }
                    if (draw.length() > 0) {
                        System.out.println(draw);
                    }
                    if (scry.length() > 0) {
                        System.out.println(scry);
                    }
                    if (asLongAsIsMyTurn.length() > 0) {
                        System.out.println(asLongAsIsMyTurn);
                    }                    
                    if (surveil.length() > 0) {
                        System.out.println(surveil);
                    }
                    if (undergrowth.length() > 0) {
                        System.out.println(undergrowth);
                    }
                    /*
                    if (cycling.length() > 0) {
                        System.out.println(cycling);
                    }
                    if (embalm.length() > 0) {
                        System.out.println(embalm);
                    }
                    if (exert.length() > 0) {
                        System.out.println(exert);
                    }
                    if (afflict.length() > 0) {
                        System.out.println(afflict);
                    }
                    if (eternalize.length() > 0) {
                        System.out.println(eternalize);
                    }
                    if (enrage.length() > 0) {
                        System.out.println(enrage);
                    }
                    if (raid.length() > 0) {
                        System.out.println(raid);
                    }
                     */
                                        
                    System.out.println("text=" + oracleText.replace("\n", " -- "));

                    if (!convoke.isEmpty()) {
                      System.out.println(convoke);
                    }
                    if (!spectacle.isEmpty()) {
                        System.out.println(spectacle);
                    }
                }
                //if (mana != null && !type.contains("Land")) {
                if (!type.contains("Land")) {
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
