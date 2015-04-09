/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class AutoWagicLine {

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
    protected static String processOracleTextTransforms(String oracleText, String name) {
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
    protected static String processOracleTextBolster(String oracleText) {
        String bolster = "";
        String bolsterCounters;
        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("bolster")) {
                bolsterCounters = oracleText.substring(oracleText.indexOf("bolster") + 8, oracleText.indexOf("bolster") + 9);
                bolster = "auto=name(Bolster) notatarget(creature[toughness=toughness:lowest:creature:mybattlefield]|mybattlefield) counter(1/1," + bolsterCounters + ")";
            }
        } catch (Exception ex) {

        }
        return bolster;
    }

    protected static String processOracleTextCombatDamage(String oracleText) {
        String combatDamage = "";
        try {
            if (oracleText.contains("deals combat damage to a player,")) {
                combatDamage = "auto=@combatdamaged(player) from(this):";
            } else if (oracleText.contains("deals combat damage to a creature,")) {
                combatDamage = "auto=@combatdamaged(creature) from(this):";
            } else if (oracleText.contains("deals combat damage,")) {
                combatDamage = "auto=@combatdamaged(player) from(this):";
                combatDamage += "\nauto=@combatdamaged(creature) from(this):";
            }
        } catch (Exception ex) {

        }
        return combatDamage;
    }

    protected static String processOracleTextFaceUp(String oracleText) {
        String faceUp = "";
        try {
            if (oracleText.contains("is turned face up,")) {
                faceUp = "autofaceup=";
            }
        } catch (Exception ex) {

        }
        return faceUp;
    }

    //facedown={3}
    //autofacedown={3}{W}{W}:morph
    //autofaceup=counter(1/1,5)
    protected static String processOracleTextMorph(String oracleText) {
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

    protected static String processOracleTextAura(String oracleText) {
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
    protected static String processOracleTextEquip(String oracleText) {
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
    protected static String processOracleTextDash(String oracleText) {
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
    protected static String processOracleTextETB(String oracleText, String name) {
        String etb = "";
        try {
            if (oracleText.contains(name + " enters the battlefield, you may ")) {
                etb = "auto=may";
            } else if (oracleText.contains(name + " enters the battlefield")) {
                etb = "auto=";
            }
        } catch (Exception ex) {

        }
        return etb;
    }

    //enters the battlefield tapped,
    protected static String processOracleTextETBTapped(String oracleText, String name) {
        String etbTapped = "";
        try {
            if (oracleText.contains(name + " enters the battlefield tapped")) {
                etbTapped = "auto=tap";
            }
        } catch (Exception ex) {

        }
        return etbTapped;
    }

    //enters the battlefield tapped,
    protected static String processOracleTextFirebreting(String oracleText, String name) {
        String firebreating = "";
        String firebreatingCost;
        try {
            if (oracleText.contains(name + " gets +1/+0 until end of turn.")) {
                firebreatingCost = oracleText.substring(oracleText.indexOf("{"), oracleText.indexOf(name) - 2);;
                firebreating = "auto=" + firebreatingCost + ":1/0";
            }
        } catch (Exception ex) {

        }
        return firebreating;
    }

    // auto=may choice notatarget(creature|mybattlefield) sacrifice && NEW ABILITY
    protected static String processOracleTextExploit(String oracleText) {
        String exploit = "";
        try {
            if (oracleText.contains("Exploit")) {
                exploit = "auto=may notatarget(creature|mybattlefield) sacrifice name(Exploit) && transforms((,newability[choice";
            }

        } catch (Exception ex) {

        }
        return exploit;
    }

    //auto={G}:regenerate
    protected static String processOracleTextProwess(String oracleText) {
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
    protected static String processOracleTextOppCasts(String oracleText) {
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
    protected static String processOracleTextRegenerate(String oracleText) {
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

    protected static String processOracleTextThisDies(String oracleText, String name) {
        String thisDies = "";
        try {
            if (oracleText.contains("When " + name + " dies,")) {
                thisDies = "auto=@movedTo(this|graveyard) from(battlefield):";
            }
        } catch (Exception ex) {

        }
        return thisDies;
    }

    // auto=@targeted(this) from(*|opponentbattlefield):damage:3 opponent
    // auto=@targeted(this) from(*|opponenthand):damage:3 opponent
    protected static String processOracleTextTargeted(String oracleText, String name) {
        String thisDies = "";
        try {
            if (oracleText.contains(name + " becomes the target of a spell or ability,")) {
                thisDies = "auto=@targeted(this):";
            }
            if (oracleText.contains("becomes the target of a spell or ability an opponent controls,")) {
                thisDies = "auto=@targeted(dragon|mybattlefield):";
            }
        } catch (Exception ex) {

        }
        return thisDies;
    }

    //auto=@each my upkeep:
    protected static String processOracleTextUpkeep(String oracleText) {
        String upkeep = "";
        try {
            if (oracleText.contains("At the beginning of your upkeep,")) {
                upkeep = "auto=@each my upkeep:";
            }
        } catch (Exception ex) {

        }
        return upkeep;
    }

    // can't block creatures with power
    protected static String processOracleTextWeak(String oracleText) {
        String weak = "";
        String weakCondition;
        try {
            if (oracleText.contains("can't block creatures with power")) {
                weakCondition = oracleText.substring(oracleText.indexOf("power") + 6, oracleText.indexOf("power") + 7);
                weak = "auto=cantbeblockerof(creature[power>=" + weakCondition + "])";
            }
        } catch (Exception ex) {

        }
        return weak;
    }
}
