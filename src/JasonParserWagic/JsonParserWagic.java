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

    private static String filePath = "C:\\Temp\\test.txt";

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

//                boolean generateID = false;
//                if (generateID) {
//                    generateCardDat(innerObj.get("name"), innerObj.get("multiverseid"), innerObj.get("rarity"));
//                    continue;
//                }
                if (isReprintOrBasic((String) innerObj.get("name"))) {
                    continue;
                }

                String name = "name=" + innerObj.get("name");
                String oracleText = (String) innerObj.get("text");

                String bolster = processOracleTextBolster(oracleText);
                String combatDamage = processOracleTextCombatDamage(oracleText);
                String dash = processOracleTextDash(oracleText);
                String entersBattlefield = processOracleTextEntersBattlefield(oracleText,(String) innerObj.get("name"));
                String entersTapped = processOracleTextEntersTapped(oracleText,(String) innerObj.get("name"));
                String exploit = processOracleTextExploit(oracleText);
                String morph = processOracleTextMorph(oracleText);
                String prowess = processOracleTextProwess(oracleText);
                String oppCasts = processOracleTextOppCasts(oracleText);
                String regenerate = processOracleTextRegenerate(oracleText);
                String thisDies = processOracleTextThisDies(oracleText, (String) innerObj.get("name"));
                String transforms = processOracleTextTransforms(oracleText, (String) innerObj.get("name"));
                String upkeep = processOracleTextUpkeep(oracleText);

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
                    abilities = processOracleText(oracleText);
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
                System.out.println(name);
                if (!abilities.equals("")) {
                    System.out.println("abilities=" + abilities.trim());
                }
                if (!bolster.equals("")) {
                    System.out.println(bolster);
                }                
                if (!combatDamage.equals("")) {
                    System.out.println(combatDamage);
                }
                if (!dash.equals("")) {
                    System.out.println(dash);
                }                
                if (!entersBattlefield.equals("")) {
                    System.out.println(entersBattlefield);
                }
                if (!entersTapped.equals("")) {
                    System.out.println(entersTapped);
                }
                if (!exploit.equals("")) {
                    System.out.println(exploit);
                }
                if (!morph.equals("")) {
                    System.out.println(morph);
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
                if (!transforms.equals("")) {
                    System.out.println(transforms);
                }
                if (!upkeep.equals("")) {
                    System.out.println(upkeep);
                }
                if (subtype.contains("Aura")) {
                    System.out.println(processOracleTextAura(oracleText));
                }
                if (subtype.contains("Equipment")) {
                    System.out.println(processOracleTextEquip(oracleText));
                }
//              System.out.println("auto=");
                if (oracleText != null) {
                    System.out.println("text=" + oracleText);
                }
                System.out.println(mana);
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
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    //[card]
    //primitive=Abomination of Gudul 
    //id=386463
    //rarity=C
    //[/card]
    private static void generateCardDat(Object primitive, Object id, Object rare) {
        String rarity = (String) rare;

        switch (rarity) {
            case "Common":
                rarity = "C";
                break;
            case "Uncommon":
                rarity = "U";
                break;
            case "Rare":
                rarity = "R";
                break;
            case "Basic Land":
                rarity = "L";
                break;
            default:
                rarity = "M";
                break;
        }

        System.out.println("[card]");
        System.out.println("primitive=" + (String) primitive);
        System.out.println("id=" + Long.toString((Long) id));
        System.out.println("rarity=" + rarity);
        System.out.println("[/card]");
    }

    //name=Azorius Keyrune
    //auto={T}:Add{W}
    //auto={T}:Add{U}
    //auto={W}{U}:transforms((Bird Artifact Creature,setpower=2,settoughness=2,blue,white,flying)) ueot
    //text={T}: Add {W} or {U} to your mana pool. -- {W}{U}: Azorius Keyrune becomes a 2/2 white and blue Bird artifact creature with flying until end of turn.
    //mana={3}
    //type=Artifact
    //name=Atarka Monument
    //auto=
    //text={T}: Add {R} or {G} to your mana pool.
    //{4}{R}{G}: Atarka Monument becomes a 4/4 red and green Dragon artifact creature with flying until end of turn.
    //mana={3}
    //type=Artifact
    private static String processOracleTextTransforms(String oracleText, String name) {
        String transforms = "";
        String transformCost;
        String pow;
        String tho;
        String color;
        String color2;

        try {
            if (oracleText.contains("becomes a")) {
                transformCost = oracleText.substring(oracleText.indexOf("{4"), oracleText.indexOf(name));
                pow = oracleText.substring(oracleText.indexOf("/") - 1, oracleText.indexOf("/"));
                tho = oracleText.substring(oracleText.indexOf("/") + 1, oracleText.indexOf("/") + 2);
                color = oracleText.substring(oracleText.indexOf("/") + 3, oracleText.indexOf(" and"));
                color2 = oracleText.substring(oracleText.indexOf("and") + 4, oracleText.indexOf(" Dragon"));
                transforms = "auto=" + transformCost.trim() + "transforms((Dragon Creature,setpower=" + pow + ",settoughness=" + tho + "," + color + "," + color2 + ",flying)) ueot";
            }
        } catch (Exception ex) {

        }
        return transforms;
    }

    //name(Bolster) notatarget(creature[toughness=toughness:lowest:creature:mybattlefield]) counter(1/1,1)
    private static String processOracleTextBolster(String oracleText) {
        String bolster = "";
        String bolsterCounters;
        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("bolster")) {
                bolsterCounters = oracleText.substring(oracleText.indexOf("bolster") + 8, oracleText.indexOf("bolster") + 9);
                bolster = "auto=name(Bolster) notatarget(creature[toughness=toughness:lowest:creature:mybattlefield]) counter(1/1," + bolsterCounters + ")";
            }
        } catch (Exception ex) {

        }
        return bolster;
    }
    
    private static String processOracleTextCombatDamage(String oracleText) {
        String combatDamage = "";
        try {
            if (oracleText.contains("deals combat damage to a player,")) {
                combatDamage = "auto=@combatdamaged(player) from(this):";
            }
        } catch (Exception ex) {

        }
        return combatDamage;
    }

    //facedown={3}
    //autofacedown={3}{W}{W}:morph
    //autofaceup=counter(1/1,5)
    private static String processOracleTextMorph(String oracleText) {
        String morph = "";
        String megamorphCost;
        try {
            if (oracleText.contains("Megamorph")) {
                megamorphCost = oracleText.substring(oracleText.indexOf("Megamorph") + 10);
                megamorphCost = megamorphCost.substring(0, megamorphCost.indexOf("(") - 1);
                morph = "facedown={3}\n"
                        + "autofacedown=" + megamorphCost + ":morph\n"
                        + "autofaceup=counter(1/1,1)";
            }
        } catch (Exception ex) {

        }
        return morph;
    }

    private static String processOracleTextAura(String oracleText) {
        String aura = "";
        String target;
        try {
            target = oracleText.substring(oracleText.indexOf("Enchant ") + 8);
            target = target.substring(0, target.indexOf("\n"));
            aura = "target=" + target;

        } catch (Exception ex) {

        }
        return aura;
    }

//    other={3}{B}{R} name(Dash)
//    auto=if paid(alternative) then transforms((,newability[haste],newability[phaseaction[endofturn sourceinplay]
//    moveto(ownerhand) all(this)])) forever
    private static String processOracleTextEquip(String oracleText) {
        String equip = "";
        String equipCost;
        try {
            equipCost = oracleText.substring(oracleText.indexOf("Equip ") + 6);
            equipCost = equipCost.substring(0, equipCost.indexOf("}") + 1);
            equip = "auto=" + equipCost + ":equip";

        } catch (Exception ex) {

        }
        return equip;
    }

    //    other={3}{B}{R} name(Dash)
    //    auto=if paid(alternative) then transforms((,newability[haste],newability[phaseaction[endofturn sourceinplay] moveto(ownerhand) all(this)])) forever
    private static String processOracleTextDash(String oracleText) {
        String dash = "";
        String dashCost;
        try {
            if (oracleText.contains("Dash")) {
                dashCost = oracleText.substring(oracleText.indexOf("Dash") + 5);
                dashCost = dashCost.substring(0, dashCost.indexOf("(") - 1);
                dash = "other=" + dashCost + " name(Dash)\n"
                        + "auto=if paid(alternative) then transforms((,newability[haste],newability[phaseaction[endofturn sourceinplay] moveto(ownerhand) all(this)])) forever";
            }

        } catch (Exception ex) {

        }
        return dash;
    }
    
    //enters the battlefield,
    private static String processOracleTextEntersBattlefield(String oracleText, String name) {
        String enters = "";
        try {
            if (oracleText.contains(name + " enters the battlefield, you may ")) {
                enters = "auto=may";               
            }
//            else if (oracleText.contains(name + " enters the battlefield")) {
//                enters = "auto=";        
//            }
        } catch (Exception ex) {

        }
        return enters;
    }
    
        //enters the battlefield tapped,
    private static String processOracleTextEntersTapped(String oracleText, String name) {
        String enters = "";
        try {
            if (oracleText.contains(name + " enters the battlefield tapped")) {
                enters = "auto=tap";               
            }
        } catch (Exception ex) {

        }
        return enters;
    }

    // auto=may choice notatarget(creature|mybattlefield) sacrifice && NEW ABILITY
    private static String processOracleTextExploit(String oracleText) {
        String exploit = "";
        try {
            if (oracleText.contains("Exploit")) {
                exploit = "auto=may choice notatarget(creature|mybattlefield) sacrifice &&";
            }

        } catch (Exception ex) {

        }
        return exploit;
    }

    //auto={G}:regenerate
    private static String processOracleTextProwess(String oracleText) {
        String prowess = "";
        String prowessTrigger;
        try {
            if (oracleText.contains("Whenever you cast a ")) {
                //prowessTrigger = oracleText.substring(oracleText.indexOf("Whenever you cast a "), oracleText.indexOf(","));
                prowessTrigger = "*[-creature]";
                prowess = "auto=@movedTo(" + prowessTrigger + "|mystack):";
            }
        } catch (Exception ex) {

        }
        return prowess;
    }

    // Whenever an opponent casts a
    private static String processOracleTextOppCasts(String oracleText) {
        String oppCasts = "";
        String oppCastsTrigger;
        try {
            if (oracleText.contains("Whenever an opponent casts a ")) {
                //prowessTrigger = oracleText.substring(oracleText.indexOf("Whenever you cast a "), oracleText.indexOf(","));
                oppCastsTrigger = "*";
                oppCasts = "auto=@movedTo(" + oppCastsTrigger + "|opponentstack):";
            }
        } catch (Exception ex) {

        }
        return oppCasts;
    }

    //auto={G}:regenerate
    private static String processOracleTextRegenerate(String oracleText) {
        String regenerate = "";
        String regenerateCost;
        try {
            if (oracleText.contains("Regenerate")) {
                regenerateCost = oracleText.substring(oracleText.indexOf("{"), oracleText.indexOf("Regenerate") - 2);
                regenerate = "auto=" + regenerateCost + ":regenerate";
            }

        } catch (Exception ex) {

        }
        return regenerate;
    }

    // This dies
    // auto=@movedTo(this|graveyard) from(battlefield):
    private static String processOracleTextThisDies(String oracleText, String name) {
        String thisDies = "";
        try {
            if (oracleText.contains("When " + name + " dies,")) {
                thisDies = "auto=@movedTo(this|graveyard) from(battlefield):";
            }
        } catch (Exception ex) {

        }
        return thisDies;
    }
    
    //auto=@each my upkeep:
    private static String processOracleTextUpkeep(String oracleText) {
        String upkeep = "";
        try {
            if (oracleText.contains("At the beginning of your upkeep,")) {
                upkeep = "auto=@each my upkeep:";
            }
        } catch (Exception ex) {

        }
        return upkeep;
    }

    private static boolean isReprintOrBasic(String name) {
        String[] reprints
                = {"Battle Mastery",
                    "Death Wind",
                    "Dragon Fodder",
                    "Duress",
                    "Evolving Wilds",
                    "Explosive Vegetation",
                    "ravepurge",
                    "Kindled Fury",
                    "Mind Rot",
                    "Naturalize",
                    "Negate",
                    "Pacifism",
                    "Spidersilk Net",
                    "Summit Prowler",
                    "Tormenting Voice",
                    "Ultimate Price"};

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

    private static String processOracleText(String oracleText) {
        String abilities = "";
        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("flash")) {
                abilities += "flash, ";
            }
            if (oracleText.contains("can't be countered")) {
                abilities += "nofizzle, ";
            }
            if (oracleText.contains("defender")) {
                abilities += "defender, ";
            }
            if (oracleText.contains("flying")) {
                abilities += "flying, ";
            }
            if (oracleText.contains("intimidate")) {
                abilities += "intimidate, ";
            }
            if (oracleText.contains("first strike")) {
                abilities += "first strike, ";
            }
            if (oracleText.contains("double strike")) {
                abilities += "double strike, ";
            }
            if (oracleText.contains("deathtouch")) {
                abilities += "deathtouch, ";
            }
            if (oracleText.contains("haste")) {
                abilities += "haste, ";
            }
            if (oracleText.contains("hexproof")) {
                abilities += "opponentshroud, ";
            }
            if (oracleText.contains("can't be blocked.")) {
                abilities += "unblockable, ";
            }
            if (oracleText.contains("indestructible")) {
                abilities += "indestructible, ";
            }
            if (oracleText.contains("vigilance")) {
                abilities += "vigilance, ";
            }
            if (oracleText.contains("reach")) {
                abilities += "reach, ";
            }
            if (oracleText.contains("trample")) {
                abilities += "trample, ";
            }
            if (oracleText.contains("lifelink")) {
                abilities += "lifelink, ";
            }
            if (oracleText.contains("can't block.")) {
                abilities += "cantblock, ";
            }
            if (oracleText.contains("protection from")) {
                abilities += "protection from, ";
            }
        } catch (Exception ex) {

        }
        if (abilities.endsWith(", ")) {
            abilities = abilities.substring(0, abilities.length() - 2);
        }
        return abilities;
    }
}
