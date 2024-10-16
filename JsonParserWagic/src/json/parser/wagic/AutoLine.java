package json.parser.wagic;

import java.util.regex.Pattern;
import static json.parser.wagic.AutoEffects.processEffect;

// @author Eduardo
public class AutoLine {
    
    static String impending(String oracleText, String manaCost) {
        String autoImpending = "";
        String impendingCost;

        try {
            String incidence = "Impending 4-";
            if (oracleText.contains(incidence)) {
                impendingCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf(" "));
                autoImpending = "other=" + impendingCost.trim() + " name(Impending)";
                autoImpending += "\nauto=if paid(alternative) then transforms((removetypes,newability[counter(0/0.4.Impending)],newability[becomes(Enchantment)]))";
                autoImpending += "\nauto=@each my end:counter(0/0,-1,Impending)";
                autoImpending += "\nauto=this(counter{0/0.1.Impending}<=0) becomes(Creature)";
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return autoImpending;
    }
        
    protected static String surveil(String oracleText) {
        String surveil = "";

        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "surveil ";
            if (oracleText.contains(incidence)) {
                String surveilNumber = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(incidence) + incidence.length() + 1);
                surveil = String.format("aicode=name(surveil) activate name(surveil) transforms((,newability[foreach(*[zpos<=%s]|mylibrary) moverandom(*[zpos<=%s]) from(mylibrary) to(mylibrary)])) ueot\n",
                        surveilNumber, surveilNumber);
                surveil += String.format("auto=name(surveil) reveal:%s optionone name(put in graveyard) target(<upto:%s>*|reveal) moveto(ownergraveyard) optiononeend optiontwo name(put in library) target(<%s>*|reveal) moveto(ownerlibrary) optiontwoend revealend",
                        surveilNumber, surveilNumber, surveilNumber);
            }
        } catch (Exception ex) {

        }
        return surveil;
    }

    static String flashback(String oracleText, String manaCost) {
        String autoFlashback = oracleText;

        try {
            String incidence = "Flashback";
            if (oracleText.contains(incidence)) {
                autoFlashback = autoFlashback.replaceAll("Flashback ", "flashback=");
            } else {
                return "";
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return autoFlashback.trim();
    }

    //auto=bestow transforms((,newability[@targeted(this):all(trigger[to]) sacrifice])) forever
    //auto=@targeted(this):all(trigger[to]) sacrifice
    //auto=bestow bstw
    //auto=bestow teach(creature) 4/4
    //bestow={3}{U}{U}
    static String bestow(String oracleText, String manaCost) {
        String autoBestow = "";
        String bestowCost;

        try {
            String incidence = "Bestow";
            if (oracleText.contains(incidence)) {
                bestowCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf(" "));
                autoBestow = "auto=bestow\nauto=bestow bstw\nauto=bestow";
                autoBestow += "\nbestow=" + bestowCost.trim();
                //String ifKicked = "was kicked, ";
                //String ifKickedEffect = oracleText.substring(oracleText.indexOf(ifKicked)+ifKicked.length());

                //kicker += "\nauto=kicked" + AutoEffects.processEffect(ifKickedEffect, cardName);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return autoBestow;
    }

    static String offspring(String oracleText, String manaCost) {
        String autoOffspring = "";
        String offspringCost;

        try {
            String incidence = "Offspring";
            if (oracleText.contains(incidence)) {
                offspringCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf(" "));
                autoOffspring = "other=" + manaCost + offspringCost.trim() + " name(Offspring)";
                autoOffspring += "\nauto=if paid(alternative) then clone and!( becomes(,1/1) )!";
                //String ifKicked = "was kicked, ";
                //String ifKickedEffect = oracleText.substring(oracleText.indexOf(ifKicked)+ifKicked.length());

                //kicker += "\nauto=kicked" + AutoEffects.processEffect(ifKickedEffect, cardName);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return autoOffspring;
    }

    static String plot(String oracleText, String manaCost) {
        String autoPlot = "";
        String plotCost;

        try {
            String incidence = "Plot";
            if (oracleText.contains(incidence)) {
                plotCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf(" "));
                autoPlot = "autohand=" + plotCost.trim() + ":_PLOT_";
                autoPlot += "\nautoexile=_PLOTCAST_";
                //String ifKicked = "was kicked, ";
                //String ifKickedEffect = oracleText.substring(oracleText.indexOf(ifKicked)+ifKicked.length());

                //kicker += "\nauto=kicked" + AutoEffects.processEffect(ifKickedEffect, cardName);
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return autoPlot;
    }

    static String gift(String oracleText, String manaCost) {
        String gift = "";
        String giftCost;

        try {
            String incidence = "Gift";
            if (oracleText.contains(incidence)) {
                giftCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf(" ")).trim();
                gift = "other=" + manaCost + " name(Gift " + giftCost + ")";
                gift += "\nauto=if paid(alternative) then ";
                if (giftCost.equals("a Food")) {
                    gift += "_FOOD_";
                }
                if (giftCost.equals("a card")) {
                    gift += "draw:1";
                }
                if (giftCost.equals("a tapped Fish")) {
                    gift += "_FISHTOKEN_ and!(tap(noevent))!";
                }
                if (giftCost.equals("a Treasure")) {
                    gift += "_TREASURE_";
                }
                gift += " opponent";
                gift += "\nauto=if paid(alternative) then ";
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
        return gift;
    }

    //Corrupted
    protected static String Corrupted(String oracleText, String cardName) {
        String corrupted = "";

        if (oracleText.contains("Corrupted")) {
            oracleText = oracleText.replace("Corrupted - ", "");
            corrupted = "auto=" + processEffect(oracleText, cardName);
        }
        return corrupted;
    }

    protected static String ForMirrodin(String oracleText, String cardName) {
        if (oracleText.contains("For Mirrodin!")) {
            return "auto=" + oracleText.replace("For Mirrodin!", "livingweapontoken(Rebel,Rebel,2/2,red)");
        } else {
            return "";
        }
    }

    //Prowess
    protected static String Prowess(String oracleText) {
        String prowess = "";
        String prowessTrigger;

        if (oracleText.contains("Prowess")) {
            //prowessTrigger = oracleText.substring(oracleText.indexOf("Whenever you cast a "), oracleText.indexOf(","));
            prowessTrigger = "*[-creature]";
            prowess = "auto=@movedTo(" + prowessTrigger + "|mystack):1/1 ueot";
        }
        return prowess;
    }

    protected static String CombatDamage(String oracleText) {
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
            //if (AutoEffects.ProcessEffect(oracleText, combatDamage).length() > 0) {
            //  combatDamage += AutoEffects.ProcessEffect(oracleText, combatDamage);
            //}

        } catch (Exception ex) {

        }
        return combatDamage;
    }

    static String MyTarget(String oracleText, String type, String subtype) {
        String target;
        String myTarget = "";
        String letterS = "";
        if (type.contains("all")) {
            letterS = "s";
        }
        try {
            if (oracleText.contains("any target")) {
                myTarget = "target=anytarget";
                return myTarget;
            }
            if (oracleText.contains("target spell")) {
                myTarget = "target=*|stack";
                return myTarget;
            }

            String incidence = (type.contains("Aura")) ? "Enchant " : "arget ";
            String incidenceEnd = (type.contains("Aura")) ? "\n" : ".";

            if (oracleText.contains(incidence) || letterS.contains("s")) {
                if (letterS.contains("s")) {
                    target = oracleText;
                } else {
                    target = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                    //target = target.substring(0, target.indexOf(incidenceEnd));
                }
                if (target.contains("permanent" + letterS)) {
                    myTarget = "*";
                }
                if (target.contains("permanent" + letterS + " you control")) {
                    myTarget = "*|myBattlefield";
                }
                if (target.contains("player")) {
                    myTarget = "player";
                }
                if (target.contains("opponent")) {
                    myTarget = "opponent";
                }

                letterS = target.endsWith("s") ? "s" : "";
                String[] targets = {"artifact", "creature", "nonlegendary creature", "enchantment", "land", "planeswalker", "token"};

                for (String t : targets) {
                    if (target.contains(t + letterS)) {
                        myTarget = t.replace("nonlegendary creature", "creature[-legendary]");
                        break;
                    }
                }
                if (target.contains("creature spell")) {
                    myTarget = "creature|stack";
                }
                if (target.contains("noncreature" + letterS)) {
                    myTarget = "*[-creature]";
                }
                if (target.contains("noncreature spell")) {
                    myTarget = "*[-creature]|stack";
                }
                if (target.contains(" or ")) {
                    myTarget = target.replace(" or ", ",");
                }
                if (target.contains("nonland permanent" + letterS)) {
                    myTarget = "*[-land]";
                }
                if (target.contains("nonbasic land" + letterS)) {
                    myTarget = "land[-basic]";
                }
                if (target.contains("token creature" + letterS)) {
                    myTarget = "creature[token]";
                }
                if (target.contains("token" + letterS)) {
                    myTarget = "*[token]";
                }
                if (target.contains("creatures")) {
                    incidence = "up to two";
                    String incidence2 = "one or two";
                    if (oracleText.contains(incidence) || oracleText.contains(incidence2)) {
                        myTarget = "<upto:2>creature";
                    }
                }
                if (target.contains("creatures")) {
                    incidence = "up to two";
                    String incidence2 = "one or two";
                    if (oracleText.contains(incidence) || oracleText.contains(incidence2)) {
                        myTarget = "<upto:2>creature";
                    }
                }
                if (target.contains("permanents")) {
                    incidence = "up to two";
                    String incidence2 = "one or two";
                    if (oracleText.contains(incidence) || oracleText.contains(incidence2)) {
                        myTarget = "<upto:2>artifact,creature,enchantment,land,planeswalker";
                    }
                }
                String creatureWith = "creature" + letterS + " with ";
                if (target.contains(creatureWith)) {
                    String withWhat = target.substring(target.indexOf(creatureWith) + creatureWith.length());//, target.lastIndexOf(" "));
                    String ptValue;
                    String condition;
                    if (withWhat.contains("or greater")) {
                        ptValue = withWhat.substring(withWhat.indexOf("or greater") - 2, withWhat.indexOf("or greater") - 1);
                        condition = withWhat.substring(0, withWhat.indexOf("or greater") - 3);
                        condition = condition.replace("converted mana cost", "manacost");
                        myTarget = "creature[" + condition + ">=" + ptValue + "]";
                    } else if (withWhat.contains("or less")) {
                        ptValue = withWhat.substring(withWhat.indexOf("or less") - 2, withWhat.indexOf("or less") - 1);
                        condition = withWhat.substring(0, withWhat.indexOf("or less") - 3);
                        condition = condition.replace("converted mana cost", "manacost");
                        myTarget = "creature[" + condition + "<=" + ptValue + "]";
                    } else {
                        myTarget = "creature[" + withWhat + "]";
                    }
                }
                if (target.contains("creature" + letterS + " you control")) {
                    myTarget = "creature|myBattlefield";
                }
                if (target.contains("creature" + letterS + " an opponent controls")) {
                    myTarget = "creature|opponentBattlefield";
                }
                if (target.contains("creature" + letterS + " you don't control")) {
                    myTarget = "creature|opponentBattlefield";
                }
                if (!letterS.equals("s")) {
                    myTarget = "target=" + myTarget;
                }
            }
            myTarget = myTarget.replace(",,", ",");

            if (!(type.contains("Instant") || type.contains("Sorcery") || subtype.contains("Aura") || subtype.contains("Equipment")) && myTarget.contains("target")) {
                myTarget = myTarget.replace("target=", "target(");
                myTarget = myTarget.concat("");
            }
        } catch (Exception ex) {
            System.out.println("DANGER MYTGT " + oracleText + "END MYTGT");
            System.out.println(ex.getMessage());
        }
        return myTarget;
    }

    protected static String equipCost(String oracleText) {
        String equipCost = oracleText.replaceAll("Equip.*?(\\{.*\\})", "$1:equip");
        return "auto=" + equipCost;
    }

    protected static String auraEquipBonus(String oracleText, String cardType) {
        String equip = "";
        String equipBuff;
        String equipHas;
        String equipLose;
        try {
            String incidence = "creature gets ";
            String incidenceAnd = " and ";
            if (oracleText.contains(incidence)) {
                if (oracleText.contains(incidenceAnd)) {
                    equipHas = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf(incidenceAnd));
                    equip += "\nauto=teach(creature) " + equipHas;
                    equipHas = oracleText.substring(oracleText.indexOf(incidenceAnd) + incidenceAnd.length(), oracleText.lastIndexOf("."));
                    equip += "\nauto=teach(creature) " + equipHas;
                }
                equipBuff = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(incidence) + incidence.length() + 5);

                equipBuff = equipBuff.replace("has ", "");
                equipBuff = equipBuff.replace("+", "");

                oracleText = equipBuff;
                equip += "\nauto=teach(creature) " + equipBuff;
            }
            String incidenceEquipHas = " has ";
            if (oracleText.contains(incidenceEquipHas)) {
                if (oracleText.contains(incidenceAnd)) {
                    equipHas = oracleText.substring(oracleText.indexOf(incidenceEquipHas) + incidenceEquipHas.length(), oracleText.lastIndexOf(incidenceAnd));
                    equip += "\nauto=teach(creature) " + equipHas;
                    equipHas = oracleText.substring(oracleText.indexOf(incidenceAnd) + incidenceAnd.length(), oracleText.lastIndexOf("."));
                    equip += "\nauto=teach(creature) " + equipHas;
                }
                equipHas = oracleText.substring(oracleText.indexOf(incidenceEquipHas) + incidenceEquipHas.length(), oracleText.lastIndexOf("."));
                equip += "\nauto=teach(creature) " + equipHas;
            }
            String incidenceEquipLose = " loses ";
            if (oracleText.contains(incidenceEquipLose)) {
                equipLose = oracleText.substring(oracleText.indexOf(incidenceEquipLose) + incidenceEquipLose.length(), oracleText.lastIndexOf("."));
                equip += "\nauto=teach(creature) " + "-" + equipLose;
            }
            String incidenceEquipNoAttackNoBlock = "can't attack or block";
            if (oracleText.contains(incidenceEquipNoAttackNoBlock)) {
                equip += "\nauto=cantattack\n" + "auto=cantpwattack\n" + "auto=cantblock";
            }
            if (oracleText.contains("can't attack.")) {
                equip += "\nauto=cantattack\n" + "auto=cantpwattack\n";
            }
            if (oracleText.contains("can't block")) {
                equip += "\nauto=cantblock";
            }
            if (oracleText.contains("activated abilities can't be activated.")) {
                equip += "\nauto=noactivatedability";
            }
            if (oracleText.contains("tap enchanted")) {
                equip += "\nauto=tap";
            }
            if (oracleText.contains("doesn't untap")) {
                equip += "\nauto=doesnotuntap";
            }

            equip = equip.replace("until end of turn", "ueot");
            if (cardType.equals("Aura") || cardType.equals("InstantOrSorcery")) {
                equip = equip.replace("teach(creature) ", "");
            }

        } catch (Exception ex) {
            ex.getMessage();
        }
        return equip;
    }

    protected static String ReplacerAuraEquipBonus(String oracleText, String cardType) {
        String bonus = "";

        oracleText = oracleText.replace("Enchant creature", "");
        oracleText = oracleText.replace("Enchanted creature", "");
        oracleText = oracleText.replace("Enchant permanent", "");
        oracleText = oracleText.replace("Enchanted permanent", "");
        oracleText = oracleText.replace("Target creature", "");
        oracleText = oracleText.replace(".", "");
        oracleText = oracleText.replace(",", "");
        oracleText = oracleText.replace(" is ", "");
        oracleText = oracleText.replace(" a ", "");
        oracleText = oracleText.replace("until end of turn", "");
        oracleText = oracleText.replace("tap enchanted", "tap");
        oracleText = oracleText.replace("doesn't untap", "doesnotuntap");
        oracleText = oracleText.replace("loses", "-");
        oracleText = oracleText.replace("can't attack", "cantattack and cantpwattack");
        oracleText = oracleText.replace("can't block", "cantblock");
        oracleText = oracleText.replace("can't attack or block", "cantattack and cantpwattack and cantblock");
        oracleText = oracleText.replace("its activated abilities can't be activated", "noactivatedability");
        oracleText = oracleText.replace("in addition to its other types", " transforms(())");
        oracleText = oracleText.replace("+", "");

        Pattern ptn = Pattern.compile("( and has | and gains | and gets | has | gains | gets | and )");
        String[] parts = ptn.split(oracleText);

        for (String eachBonus : parts) {
            if (eachBonus.length() > 1) {
                //System.out.println(eachBonus);
                bonus += "auto=teach(creature) " + eachBonus + "\n";
            }
        }
        if (cardType.contains("InstantOrSorcery")) {
            bonus = bonus.replace("teach(creature) ", "");
        }

        if (bonus.length() > 1) {
            bonus = bonus.substring(0, bonus.lastIndexOf("\n"));
        }
        return bonus;
    }

    //auto=counter(1/1,X) //halfdownX
    protected static String ETBCounters(String oracleText) {
        String etbCounters = "";
        String incidence = " +1/+1 counters on it";
        if (oracleText.contains(incidence)) {
            String numCounters = oracleText.substring(oracleText.indexOf("enters the battlefield with ") + "enters the battlefield with ".length(), oracleText.indexOf(incidence));
            numCounters = processStringNumberToValue(numCounters);
            etbCounters = String.format("counter(1/1,%s)", numCounters);
        }

        return etbCounters;
    }

    protected static String Firebreting(String oracleText, String name) {
        String firebreating = "";
        String firebreatingCost;
        try {
            if (oracleText.contains(name + " gets +1/+0 until end of turn.")) {
                firebreatingCost = oracleText.substring(oracleText.indexOf("{"), oracleText.indexOf(name) - 2);
                firebreating = "auto=" + firebreatingCost + ":1/0";
            }
        } catch (Exception ex) {

        }
        return firebreating;
    }

    protected static String cast(String oracleText, String cardName) {
        String cast = "";
        String occurrence = "you cast a";
        String occurrenceEnd = "spell";
        String occurrenceCondition;
        String occurrenceSubString;

        if (oracleText.contains(occurrence)) {
            occurrenceSubString = oracleText.substring(oracleText.indexOf(occurrence) + occurrence.length(), oracleText.lastIndexOf(","));
            occurrenceCondition = occurrenceSubString.substring(0, occurrenceSubString.indexOf(occurrenceEnd)).trim();
            occurrenceCondition = occurrenceCondition.replace("n ", "");
            if (occurrenceCondition.equals("noncreature")) {
                occurrenceCondition = "*[-creature]";
            }
            if (occurrenceCondition.equals("historic")) {
                occurrenceCondition = "artifact,*[legendary],enchantment[saga]";
            }
            if (occurrenceCondition.equals("instant or sorcery")) {
                occurrenceCondition = "instant,sorcery";
            }
            if (occurrenceCondition.contains("permanent")) {
                occurrenceCondition = "*[-instant;-sorcery]";
            }
            if (occurrenceCondition.contains("")) {
                occurrenceCondition = "*";
            }
            occurrenceCondition = occurrenceCondition.replace(" white ", "*[white]");
            occurrenceCondition = occurrenceCondition.replace(" blue ", "*[blue]");
            occurrenceCondition = occurrenceCondition.replace(" black ", "*[black]");
            occurrenceCondition = occurrenceCondition.replace(" red ", "*[red]");
            occurrenceCondition = occurrenceCondition.replace(" green ", "*[green]");
            cast = "auto=@movedTo(" + occurrenceCondition + "|mystack):";

            String effect = oracleText.substring(oracleText.indexOf(",") + 2);
            effect = AutoEffects.processEffect(effect, cardName);
            if (effect.length() > 0) {
                cast += effect;
            }
        }
        cast = cast.replace("multicolored", "multicolor");
        return cast;
    }

    // Whenever an opponent casts a
    protected static String OppCasts(String oracleText) {
        String oppCasts = "";
        String oppCastsTrigger;
        try {
            if (oracleText.contains("Whenever an opponent casts a")) {
                //oppCastsTrigger = oracleText.substring(oracleText.indexOf("Whenever an opponent casts a "), oracleText.indexOf("?"));
                oppCastsTrigger = "*";
                oppCasts = "auto=@movedTo(" + oppCastsTrigger + "|opponentstack):";
            }
        } catch (Exception ex) {

        }
        return oppCasts;
    }

    // auto=@targeted(this) from(*|opponentbattlefield):damage:3 opponent
    // auto=@targeted(this) from(*|opponenthand):damage:3 opponent
    protected static String Targeted(String oracleText, String name) {
        String thisDies = "";
        try {
            if (oracleText.contains(name + " becomes the target of a spell or ability,")) {
                thisDies = "auto=@targeted(this):";
            }
            if (oracleText.contains("becomes the target of a spell or ability an opponent controls,")) {
                thisDies = "auto=@targeted(this|mybattlefield) from(*|opponentbattlefield,opponenthand,"
                        + "opponentstack,opponentgraveyard,opponentexile,opponentlibrary):";
            }
        } catch (Exception ex) {

        }
        return thisDies;
    }

    //auto=@each my upkeep:
    protected static String Upkeep(String oracleText) {
        String upkeep = "";
        try {
            if (oracleText.contains("At the beginning of your upkeep,")) {
                upkeep = "auto=@each my upkeep:";
            }
            if (oracleText.contains("At the beginning of your end step")) {
                upkeep = "auto=@each my endofturn:";
            }
            if (oracleText.contains("At the beginning of each end step")) {
                upkeep = "auto=@each endofturn:";
            }
        } catch (Exception ex) {

        }
        return upkeep;
    }

    // can't block creatures with power
    protected static String weak(String oracleText) {
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

    static String ManaAbility(String oracleText, String subtype) {
        String manaAbility = "";
        String manaProduced = oracleText;
        String[] basicLandTypes = {"Plains", "Island", "Swamp", "Mountain", "Forest"};

        for (String basicLandType : basicLandTypes) {
            if (subtype.contains(basicLandType)) {
                manaAbility = "";
                return manaAbility;
            }
        }
        try {
            //String incidence = "{T}: Add {";
            //String endIncidence = ".";

            //if (oracleText.contains(incidence)) {
            //manaProduced = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(endIncidence));
            manaProduced = manaProduced.replace(".", "");

            if (manaProduced.contains(",") || manaProduced.contains(" or ")) {
                manaProduced = manaProduced.replace(" or ", "\nauto={T}:Add");
            }
            manaProduced = manaProduced.replace("Add one mana of any color", "ability$! choice Add{W} _ choice Add{U} _ choice Add{B} _ choice Add{R} _ choice Add{G} !$ controller");

            manaAbility = manaProduced.replace(" ", "");
            //}
        } catch (Exception ex) {

        }
        return manaAbility.trim();
    }

    static String exileDestroyDamage(String oracleText, String type) {
        String action = "";
        String auto = "";
        if (type.contains("Instant") || type.contains("Sorcery")) {
            auto = "auto=";
        }

        try {
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("exile ") && !(oracleText.contains("jump-start"))) {
                if (oracleText.contains("would die this turn, exile it instead")) {
                    action = "exiledeath";
                } else {
                    action = "moveto(exile)";
                }

                if (oracleText.contains("exile all ")) {
                    action += " all";
                }
                return auto + action;
            }
            if (oracleText.contains("destroy ")) {
                action = "destroy";
                if (oracleText.contains("destroy all ")) {
                    String toModify = oracleText.substring(oracleText.indexOf("destroy all ") + "destroy all ".length());
                    action += " all " + MyTarget(toModify, "all", type);
                }
                return auto + action;
            }
            if (oracleText.contains("all creatures get")) {
                oracleText = oracleText.replace("all creatures get", "all(creature)");
                oracleText = oracleText.replace("until end of turn.", "ueot");

                return auto + oracleText;
            }
            if (oracleText.contains(" fights ")) {
                action = "transforms((,newability[target(creature|opponentbattlefield) dynamicability<!powerstrike eachother!>])) ueot\n"
                        + "restriction=type(creature|opponentbattlefield)~morethan~0";
                return auto + action;
            }
            if (oracleText.contains("counter t")) {
                action = "fizzle";
                return auto + action;
            }
            if (oracleText.contains("return ")) {
                action = "moveTo(hand)";
                if (oracleText.contains("return all ")) {
                    action += " all";
                }
                return auto + action;
            }
            if (oracleText.contains("tap ")) {
                action = "tap ";
                if (oracleText.contains("tap all ")) {
                    action += " all";
                }
                return auto + action;
            }
            if (oracleText.contains("untap ")) {
                action = "untap ";
                if (oracleText.contains("untap all ")) {
                    action += " all";
                }
                return auto + action;
            }
            String dealsIncidence = " deals ";
            String dmgIncidence = " damage ";
            if (oracleText.contains(dealsIncidence) && oracleText.contains(dmgIncidence)) {
                String dmgAmount = oracleText.substring(oracleText.indexOf(dealsIncidence) + dealsIncidence.length(), oracleText.indexOf(dmgIncidence));
                String dmgTo = oracleText.substring(oracleText.indexOf(dmgIncidence) + dmgIncidence.length());
                if (dmgTo.contains("to each opponent")) {
                    dmgTo = " opponent";
                } else {
                    dmgTo = "";
                }

                dmgAmount += dmgTo;
                action = "damage:" + dmgAmount;
            }
        } catch (Exception ex) {

        }

        if (action.length() > 0) {
            return auto + action;
        }
        return "";
    }

    static String Gains(String oracleText) {
        String gains = "";
        try {
            String gainsIncidence = " gains ";

            if (oracleText.contains(gainsIncidence)) {
                String gainsWhat = oracleText.substring(oracleText.indexOf(gainsIncidence) + gainsIncidence.length(), oracleText.indexOf(" until"));
                gains = "auto=" + gainsWhat;
            }
        } catch (Exception ex) {

        }
        return gains;
    }

    // auto=create(Thopter:Artifact Creature Thopter:1/1:flying)*2
    static String Create(String oracleText) {
        String create = "";
        String tokenName;
        String tokenType;
        String tokenColor;
        String tokenPT;
        String tokenAbility;
        String tokenMultipl;

        String incidence = "reate ";
        try {
            if (oracleText.contains(incidence)) {
                if (oracleText.contains("Create a token that's a copy of ")) {
                    return "clone";
                }
                if (oracleText.contains("clue token")) {
                    return "_CLUE_";
                }
                if (oracleText.contains("food token")) {
                    return "_FOOD_";
                }
                if (oracleText.contains("treasure token")) {
                    return "_TREASURE_";
                }
                create = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                create = create.substring(0, create.indexOf("."));
                tokenMultipl = create.substring(0, create.indexOf(" "));
                create = create.substring(create.indexOf(" ") + 1);
                tokenPT = create.substring(0, create.indexOf(" "));
                create = create.substring(create.indexOf(" ") + 1);
                create = create.replace(" and ", ",");
                tokenColor = create.substring(0, create.indexOf(" "));
                create = create.substring(create.indexOf(" ") + 1);
                if (create.contains(" creature")) {
                    tokenName = create.substring(0, (create.indexOf(" creature")));
                    create = create.substring(create.indexOf(" creature") + 1);
                } else {
                    tokenName = create.substring(0, (create.indexOf(" artifact")));
                    create = create.substring(create.indexOf(" artifact") + 1);
                }

                tokenType = create.substring(0, create.indexOf(" token"));
                create = create.substring(create.indexOf("token") + "token".length());
                if (create.contains(" with ")) {
                    tokenAbility = create.substring(create.indexOf(" with ") + " with ".length());
                } else {
                    tokenAbility = "";
                }
                tokenMultipl = tokenMultipl.replace("a", "");
                tokenMultipl = tokenMultipl.replace("two", "*2");
                tokenMultipl = tokenMultipl.replace("three", "*3");
                tokenMultipl = tokenMultipl.replace("four", "*4");
                tokenMultipl = tokenMultipl.replace("five", "*5");
                tokenMultipl = tokenMultipl.replace("six", "*6");
                tokenMultipl = tokenMultipl.replace("seven", "*7");
                tokenMultipl = tokenMultipl.replace("eight", "*8");

                tokenAbility = tokenAbility.replace("that are tapped,attacking", ":battleready");
                tokenAbility = tokenAbility.replace("tapped and attacking", ":battleready");

                create = "create(" + tokenName + ":" + tokenType + " " + tokenName + ":" + tokenPT + ":" + tokenColor + ":" + tokenAbility + ")" + tokenMultipl;
                if (create.endsWith(":)")) {
                    create = create.replace(":)", ")");
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return create;
    }

    // Discard a card
    static String Discarded(String oracleText) {
        String discard = "";
        try {
            String incidence = "or discard a card,";

            if (oracleText.contains(incidence)) {

                discard = "auto=@discarded(*|myhand):";

            }
        } catch (Exception e) {
        }
        return discard;
    }

    static String TakeControl(String oracleText) {
        String takeControl = "";
        try {
            String incidence = "You control enchanted ";

            if (oracleText.contains(incidence)) {

                takeControl = "alias=1194";

            }
        } catch (Exception e) {
        }
        return takeControl;
    }

    static String scry(String oracleText) {
        String scry = "";
        String scryNumber;
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "scry ";

            if (oracleText.contains(incidence)) {
                scryNumber = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(incidence) + incidence.length() + 1);

                scry = "auto=_SCRY" + scryNumber + "_";
            }
        } catch (Exception e) {
        }
        return scry;
    }

    static String CostReduction(String oracleText) {
        String type = "";
        try {
            String incidence = " spells you cast cost {1} less to cast";

            if (oracleText.contains(incidence)) {
                type = oracleText.substring(0, oracleText.indexOf(incidence));

                type = "auto=lord(" + type + "|mycastingzone) altercost(colorless,-1)";
                type = type.replace(" and ", ",");
            }
        } catch (Exception e) {
        }
        return type;
    }

    static String Prototype(String oracleText) {
        String prototype = "";
        String prototypeCost;
        String prototypePow;
        String prototypeTou;

        try {
            String incidence = "Prototype {";

            if (oracleText.contains(incidence)) {
                prototypeCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf("}"));
                prototypePow = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf("}"));
                prototypeTou = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf("}"));

                prototype = "auto={" + prototypeCost + "} name(prototype)\nauto=if paid(alternative) then ";
            }
        } catch (Exception e) {
        }
        return prototype;
    }

    static String Unearth(String oracleText) {
        String unearth = "";
        String unearthCost;
        try {
            String incidence = "Unearth {";

            if (oracleText.contains(incidence)) {
                unearthCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf("}"));

                unearth = "autograveyard={" + unearthCost + "}:_UNEARTH_";
            }
        } catch (Exception e) {
        }
        return unearth;
    }

    static String Blitz(String oracleText) {
        String blitz = "";
        String blitzCost;
        try {
            String incidence = "Blitz {";

            if (oracleText.contains(incidence)) {
                blitzCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf("}"));

                blitz = "other={" + blitzCost + "}:name(Blitz)\nauto=if paid(alternative) then moveto(mybattlefield) and!( transforms((,newability[haste],newability[_DIES_draw:1],newability[treason])) forever )! asSorcery";
            }
        } catch (Exception e) {
        }
        return blitz;
    }

    static String Ninjutsu(String oracleText) {
        String ninjutsu = "";
        String ninjutsuCost;
        try {
            String incidence = "Ninjutsu {";

            if (oracleText.contains(incidence)) {
                ninjutsuCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.lastIndexOf("}"));

                ninjutsu = "autohand={" + ninjutsuCost + "}{N}:ninjutsu";
            }
        } catch (Exception e) {
        }
        return ninjutsu;
    }

    // Can't be blocked by creature[]
    static String CantBeBlockedBy(String oracleText) {
        String cantBeBlocked = "";

        String strong = "can't be blocked by creatures with power ";
        int strongIncidence = oracleText.indexOf(strong);
        if (strongIncidence > 0) {
            String value = oracleText.substring(strongIncidence + strong.length(), strongIncidence + strong.length() + 1);
            cantBeBlocked = String.format("auto=cantbeblockedby(creature[power<=%s])", value);
            return cantBeBlocked;
        }

        String incidence = "can't be blocked ";
        String creatures = " creatures";
        int indexIncidence = oracleText.indexOf(incidence);
        int creaturesIncidence = oracleText.indexOf(creatures);
        if (indexIncidence > 0 && creaturesIncidence > 0 && indexIncidence < creaturesIncidence) {
            String value = oracleText.substring(indexIncidence + incidence.length(), creaturesIncidence);
            cantBeBlocked = String.format("auto=cantbeblockedby(creature[%s])", value);
        }

        if (indexIncidence > 0) {
            String value = oracleText.substring(indexIncidence + incidence.length(), oracleText.lastIndexOf("."));
            cantBeBlocked = String.format("auto=cantbeblockedby(creature[%s])", value);
        }

        return cantBeBlocked;
    }

    // the who not the what
    // auto=all(creature|myBattlefield) 3/3 ueot
    // Other creatures you control
    static String Lord(String oracleText, String type) {
        String lord = "";
        String lordOrAll;
        String onlyOther = "";
        String creatureType = "creature";
        String condition = "";
        String effect = "";
        String withWhat;

        try {
            String incidence = "s you control";
            String justOther = "other ";
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains("zombie tokens you control")) {
                lord = oracleText.replace("zombie tokens you control have", "auto=lord(Zombie[token]|myBattlefield)");
                return lord;
            }
            if (oracleText.contains("creature tokens you control")) {
                lord = oracleText.replace("creature tokens you control", "auto=lord(creature[token]|mybattlefield)");
                return lord;
            }
            if (oracleText.contains(incidence) && !oracleText.contains("enters the battlefield")) {
                if (oracleText.contains("creatures you control")) {
                    onlyOther = "other ";
                } else if (oracleText.contains(justOther) && oracleText.contains(incidence)) {
                    onlyOther = "other ";
                    creatureType = oracleText.substring(oracleText.indexOf(justOther) + justOther.length(), oracleText.indexOf(incidence));
                } else if (oracleText.contains("multcolored")) {
                    condition = "[multicolor]";
                }
                if (oracleText.contains("creatures you control with ")) {
                    withWhat = oracleText.substring(oracleText.indexOf("creatures you control with ") + "creatures you control with ".length());
                    withWhat = withWhat.substring(0, withWhat.indexOf(" "));
                    condition = "[" + withWhat + "]";
                }

                String[] incidencesArray = {"gain", "get", "have"};
                for (String inci : incidencesArray) {
                    if (oracleText.contains(inci)) {
                        effect += oracleText.substring(oracleText.indexOf(inci) + inci.length(), oracleText.lastIndexOf("."));
                    }
                }

                if (effect.contains("until end of turn")) {
                    lordOrAll = "all";
                } else {
                    lordOrAll = "lord";
                }

                effect = effect.replace("until end of turn", "");
                effect = effect.replace(" and ", ",");

                lord = String.format("auto=%s(%s%s%s|myBattlefield)%s", lordOrAll, onlyOther, creatureType, condition, effect);
            }
        } catch (Exception ex) {
            System.out.println("DANGER DANGER " + oracleText + "DANGER DANGER");
            System.out.println(ex.getMessage());
        }
        return lord;
    }

    static String PutA(String oracleText, String Type) {
        String putA = "";
        String putAObject;
        try {
            oracleText = oracleText.toLowerCase();
            String incidence1 = "put a ";
            String incidence2 = " counter";

            if (oracleText.contains(incidence1) && oracleText.contains(incidence2)) {
                putAObject = oracleText.substring(oracleText.indexOf(incidence1) + incidence1.length(), oracleText.indexOf(incidence2));
                putAObject = putAObject.replace("+", "");
                putA = "counter(" + putAObject + ")";
            }
        } catch (Exception e) {
        }
        return putA;
    }

    static String ExileDeath(String oracleText) {
        String effect = "";
        try {
            effect = oracleText.toLowerCase();

            effect = effect.replace("prevent all combat damage that would be dealt this turn.", "preventAllcombatDamage ueot");

            effect = effect.replace("this turn, exile it instead", "exiledeath");

        } catch (Exception e) {
        }
        return effect;
    }

    static String processStringNumberToValue(String numberAsString) {

        numberAsString = numberAsString.replace("a", "1");
        numberAsString = numberAsString.replace("one", "1");
        numberAsString = numberAsString.replace("two", "2");
        numberAsString = numberAsString.replace("three", "3");
        numberAsString = numberAsString.replace("four", "4");
        numberAsString = numberAsString.replace("five", "5");
        numberAsString = numberAsString.replace("six", "6");
        numberAsString = numberAsString.replace("seven", "7");

        return numberAsString;
    }

    static String processAsLongAs(String oracleText, String name) {
        String asLongAsIsMyTurn = "";
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "as long as it's your turn,";

            if (oracleText.contains(incidence)) {
                asLongAsIsMyTurn = "auto=this(variable{controllerturn}>0) ";
                oracleText = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                asLongAsIsMyTurn += AutoEffects.processEffect(oracleText, name);
            }
        } catch (Exception e) {
        }
        return asLongAsIsMyTurn;
    }

    static String processForEach(String oracleText, String type) {
        String forEach = "";
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "for each";

            if (oracleText.contains(incidence)) {
                forEach = "auto=foreach(|myBattlefield)";
            }

        } catch (Exception e) {
        }
        return forEach;
    }

    static String EpicSaga(String oracleText, String type) {
        String epicSaga = "";
        String epicSagaEffect;

        if (oracleText.contains("I ")) {
            epicSaga = " " + oracleText.substring(0, oracleText.indexOf("I ") + 2);
            epicSagaEffect = oracleText.substring(oracleText.indexOf("I ") + 4);

            epicSagaEffect = AutoEffects.processEffect(epicSagaEffect, "EpicSaga");

            epicSaga = epicSaga.replace(" I, II ", "auto=counter(0/0,1,Lore)\nauto=@each my firstmain:counter(0/0,1,Lore)\nauto=");
            epicSaga = epicSaga.replace(" I ", "auto=counter(0/0,1,Lore)\nauto=@each my firstmain:counter(0/0,1,Lore)\nauto=");
            epicSaga = epicSaga.replace(" II ", "auto=@counteradded(0/0,1,Lore) from(this):this(counter{0/0.2.Lore}<=2)");
            epicSaga = epicSaga.replace(" III ", "auto=@counteradded(0/0,1,Lore) from(this):this(counter{0/0.3.Lore}) sacrifice(this)"
                    + "\nauto=@counteradded(0/0,1,Lore) from(this):this(counter{0/0.3.Lore}) ");

            epicSaga += epicSagaEffect;
        }
        return epicSaga;
    }

    static String ChooseOneOrBoth(String oracleText) {
        String chooseOneOrBoth = "";
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "choose one or both";

            if (oracleText.contains(incidence)) {
                chooseOneOrBoth = "auto=choice name( \n"
                        + "auto=choice name( \n"
                        + "auto=choice name(Both)";
            }
        } catch (Exception e) {
        }
        return chooseOneOrBoth;
    }
}
