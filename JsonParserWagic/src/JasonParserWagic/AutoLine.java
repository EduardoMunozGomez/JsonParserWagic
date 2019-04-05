package JasonParserWagic;

import java.util.regex.Pattern;

/**
 *
 * @author Eduardo
 */
public class AutoLine {

    static String processOracleActivatedAbilityCost(String oracleText, String cardName, String type, String subtype) {
        String activatedAbility = "";
        String activatedAbilityCost;
        String activatedAbilityEffect;
        String nextActivatedAbility = "";
        try {
            String incidence = ":";

            if (oracleText.contains(incidence)) {
                oracleText = oracleText.replace("\n", "--");
                activatedAbilityCost = oracleText.substring(oracleText.indexOf('{'), oracleText.indexOf(incidence) + 1);
                activatedAbilityCost = activatedAbilityCost.replace("Discard a card", "{D}");
                activatedAbilityCost = activatedAbilityCost.replace("Sacrifice " + cardName, "{S}");
                activatedAbilityCost = activatedAbilityCost.replace("Sacrifice a creature", "{S(creature|myBattlefield)}");
                activatedAbilityCost = activatedAbilityCost.replace("Sacrifice another creature", "{S(other creature|mybattlefield)}");
                activatedAbilityCost = activatedAbilityCost.replace("Exile " + cardName + " from your graveyard", "{E}");
                activatedAbilityCost = activatedAbilityCost.replace("Pay ", "{L:}");

                activatedAbilityEffect = oracleText.substring(oracleText.indexOf(incidence) + 2);

                if (activatedAbilityEffect.indexOf(":") > 0) {
                    nextActivatedAbility = activatedAbilityEffect.substring(activatedAbilityEffect.indexOf("\n"));
                    activatedAbilityEffect = activatedAbilityEffect.substring(0, activatedAbilityEffect.indexOf("\n"));
                } else {
                    activatedAbilityEffect = activatedAbilityEffect.substring(0, activatedAbilityEffect.indexOf(".") + 1);
                }

                activatedAbilityCost = activatedAbilityCost.replace(",", "");
                activatedAbilityCost = activatedAbilityCost.replace(" ", "");
                activatedAbilityCost = activatedAbilityCost.replace(".", "");

                String activatedAbilityCostEffect = processOracleActivatedAbilityEffect(activatedAbilityEffect, activatedAbilityCost);
                activatedAbility = "auto=" + activatedAbilityCostEffect;
            }

        } catch (Exception ex) {
            System.out.println("DANGER processOracleActivatedAbilityCost " + oracleText + "DANGER");
            System.out.println(ex.getMessage());
        }
        if (nextActivatedAbility.length() > 0) {
            activatedAbility += "\n" + processOracleActivatedAbilityCost(nextActivatedAbility, "", "", "");
        }

        return activatedAbility;
    }

    static String processOracleActivatedAbilityEffect(String activatedAbilityEffect, String activatedAbilityCost) {
        String activatedAbilityCostEffect = "";
        String effect = "";
        String adapt = "Adapt ";
        String create;
        if (activatedAbilityEffect.contains(adapt)) {
            String adaptNumber = activatedAbilityEffect.substring(activatedAbilityEffect.indexOf(adapt) + adapt.length());
            adaptNumber = adaptNumber.substring(0, adaptNumber.length() - 1);
            activatedAbilityCostEffect = "this(counter{1/1}<1)while " + activatedAbilityCost + "counter(1/1," + adaptNumber + ")";
            return activatedAbilityCostEffect;
        }
        create = processOracleCreate(activatedAbilityEffect);
        if (create.length() > 0) {
            effect = create;
        }

        String gains = processOracleGains(activatedAbilityEffect);
        if (gains.length() > 0) {
            gains = gains.replace("auto=", "");
            effect = gains;
        }

        String gets = processOracleGets(activatedAbilityEffect);
        if (gets.length() > 0) {
            gets = gets.replace("auto=", "");
            effect = gets;
        }

        String lifeGain = processOracleGainLife(activatedAbilityEffect);
        if (lifeGain.length() > 0) {
            lifeGain = lifeGain.replace("auto=", "");
            effect = lifeGain;
        }

        activatedAbilityCostEffect = activatedAbilityCost + effect;
        return activatedAbilityCostEffect;
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
            //if (AutoEffects.processEffect(oracleText, combatDamage).length() > 0) {
            //  combatDamage += AutoEffects.processEffect(oracleText, combatDamage);
            //}

        } catch (Exception ex) {

        }
        return combatDamage;
    }

    static String processOracleTextMyTarget(String oracleText, String type, String subtype) {
        String target;
        String myTarget = "";
        String letterS = "";
        if (type.contains("all")) {
            letterS = "s";
        }
        try {
            if (oracleText.contains("any target")) {
                myTarget = "target=player,creature";
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
                if (target.contains("artifact" + letterS)) {
                    myTarget = "artifact";
                }
                if (target.contains("creature" + letterS)) {
                    myTarget = "creature";
                }
                if (target.contains("nonlegendary creature" + letterS)) {
                    myTarget = "creature[-legendary]";
                }
                if (target.contains("enchantment" + letterS)) {
                    myTarget = "enchantment";
                }
                if (target.contains("land" + letterS)) {
                    myTarget = "land";
                }
                if (target.contains("planeswalker" + letterS)) {
                    myTarget = "planeswalker";
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
                if (target.contains("tapped creature" + letterS)) {
                    myTarget = "creature[tapped]";
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
                myTarget = myTarget.concat(")");
            }
        } catch (Exception ex) {
            System.out.println("DANGER MYTGT " + oracleText + "END MYTGT");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return myTarget;
    }

    protected static String processOracleTextEquipCost(String oracleText) {
        String equip;
        String equipCost;

        equipCost = oracleText.substring(oracleText.indexOf("Equip ") + 6);
        equipCost = equipCost.substring(0, equipCost.indexOf("}") + 1);
        equip = "auto=" + equipCost + ":equip";

        return equip;
    }

    protected static String processOracleTextAuraEquipBonus(String oracleText, String cardType) {
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
            System.out.println("JasonParserWagic.AutoLine.processOracleTextAuraEquipBonus()");
            ex.getMessage();
            //ex.printStackTrace();
        }
        return equip;
    }

    protected static String processOracleREPLACERAuraEquipBonus(String oracleText, String cardType) {
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

    // Enters the battlefield
    protected static String processOracleTextETB(String oracleText, String name, String type, String subtype) {
        String etb = "";
        if (subtype.contains("Aura") || subtype.contains("Equipment")) {
            return "";
        }

        try {
            if (oracleText.contains(name + " enters the battlefield")) {
                etb = "auto=";
                oracleText = oracleText.substring(oracleText.indexOf(" enters the battlefield, ") + " enters the battlefield, ".length());
                if (oracleText.contains(name + " enters the battlefield tapped")) {
                    etb += "tap(noevent)";
                }
                if (oracleText.contains(name + " enters the battlefield, you may")) {
                    etb += "may";
                }

                String takeAction = processOracleTextExileDestroyDamage(oracleText, type);
                etb += takeAction;
                String myTarget = processOracleTextMyTarget(oracleText, type, subtype);
                etb += myTarget;
                String effect = AutoEffects.processEffect(oracleText, name);
                etb += "EFFECT " + effect + "END EFFECT ";
                //String effect = processOracleActivatedAbilityEffect(oracleText, type);
                //etb += "EFFECT " + effect + "END EFFECT ";
                String create = processOracleCreate(oracleText);
                etb += create;
                String etbCounters = processOracleETBCounters(oracleText);
                etb += etbCounters;
                String gainLife = processOracleGainLife(oracleText);
                etb += gainLife;

            }
        } catch (Exception ex) {
            System.out.println("DANGER FROM ETB " + oracleText + "DANGER");
            System.out.println(ex.getMessage());
        }
        return etb;
    }

    //auto=counter(1/1,X) //halfdownX 
    protected static String processOracleETBCounters(String oracleText) {
        String etbCounters = "";
        String incidence = " +1/+1 counters on it";
        if (oracleText.contains(incidence)) {
            String numCounters = oracleText.substring(oracleText.indexOf("enters the battlefield with ") + "enters the battlefield with ".length(), oracleText.indexOf(incidence));
            numCounters = processStringNumberToValue(numCounters);
            etbCounters = String.format("counter(1/1,%s)", numCounters);
        }

        return etbCounters;
    }

    protected static String processOracleTextFirebreting(String oracleText, String name) {
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

    protected static String processOracleTextCast(String oracleText, String cardName) {
        String cast = "";
        String occurrence = "you cast a";
        String occurrenceEnd = "spell";
        String occurrenceCondition;
        String occurrenceSubString;

        if (oracleText.contains(occurrence)) {
            occurrenceSubString = oracleText.substring(oracleText.indexOf(occurrence) + occurrence.length(), oracleText.lastIndexOf("."));
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
            occurrenceCondition = occurrenceCondition.replace(" white ", "*[white]");
            occurrenceCondition = occurrenceCondition.replace(" blue ", "*[blue]");
            occurrenceCondition = occurrenceCondition.replace(" black ", "*[black]");
            occurrenceCondition = occurrenceCondition.replace(" red ", "*[red]");
            occurrenceCondition = occurrenceCondition.replace(" green ", "*[green]");
            //System.out.println("HELP ME OOOOUT11111 "+occurrenceCondition);
            cast = "auto=@movedTo(" + occurrenceCondition + "|mystack):";

            String effect = oracleText.substring(oracleText.indexOf(",") + 2);
            effect = AutoEffects.processEffect(effect, cardName);
            if (effect.length() > 0) {
                cast += effect;
            }

            //String create = AutoLine.processOracleCreate(oracleText);
            //cast += create;
        }
        cast = cast.replace("multicolored", "multicolor");
        return cast;
    }

    // Whenever an opponent casts a
    protected static String processOracleTextOppCasts(String oracleText) {
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

    protected static String processOracleTextThisDies(String oracleText, String name) {
        String thisDies = "";
        try {
            if (oracleText.contains("When " + name + " dies,") || oracleText.contains("When this creature dies")) {
                thisDies = "auto=@movedTo(this|graveyard) from(battlefield):";
                String create = AutoLine.processOracleCreate(oracleText);
                thisDies += create;
            }
        } catch (Exception ex) {

        }

        if (thisDies.length() > 0) {
            //thisDies += "\n";
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
                thisDies = "auto=@targeted(this|mybattlefield) from(*|opponentbattlefield,opponenthand,"
                        + "opponentstack,opponentgraveyard,opponentexile,opponentlibrary):";
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

    static String processOracleTextManaAbility(String oracleText, String subtype) {
        String manaAbility = "";
        String manaProduced;
        String[] basicLandTypes = {"Plains", "Island", "Swamp", "Mountain", "Forest"};
        for (String basicLandType : basicLandTypes) {
            if (subtype.contains(basicLandType)) {
                manaAbility = "";
                return manaAbility;
            }
        }
        try {
            String incidence = "{T}: Add {";
            String endIncidence = ".";

            if (oracleText.contains(incidence)) {
                manaProduced = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(endIncidence));
                manaProduced = manaProduced.replace(".", "");

                if (manaProduced.contains(",") || manaProduced.contains(" or ")) {
                    //manaProduced = manaProduced.replace(",", "\nauto={T}:Add");
                    manaProduced = manaProduced.replace(" or ", "\nauto={T}:Add");
                }

                manaAbility = "auto={T}:Add{" + manaProduced;
            }
        } catch (Exception ex) {

        }
        return manaAbility;
    }

    static String processOracleTextExileDestroyDamage(String oracleText, String type) {
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
                    action += " all " + processOracleTextMyTarget(toModify, "all", type);
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
                action = "moveTo(ownerHand)";
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

    static String processOracleGains(String oracleText) {
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

    static String processOracleGets(String oracleText) {
        String gets = "";
        try {
            String getsIncidence = " gets ";

            if (oracleText.contains(getsIncidence)) {
                String getsWhat = oracleText.substring(oracleText.indexOf(getsIncidence) + getsIncidence.length(), oracleText.indexOf(" until"));
                gets = "auto=" + getsWhat;
            }
        } catch (Exception ex) {

        }
        return gets;
    }

    // create a 1/1 white Warrior creature token with vigilance.
    // create(Servo:Artifact Creature Servo:1/1)
    // auto=create(Thopter:Artifact Creature Thopter:1/1:flying)*2
    static String processOracleCreate(String oracleText) {
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

                create = "create(" + tokenName + ":" + tokenType + " " + tokenName + ":" + tokenPT + ":" + tokenColor + ":" + tokenAbility + ")" + tokenMultipl;
                if (create.endsWith(":)")){
                    create = create.replace(":)", ")");
                }
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return create;
    }

    // Draw Monsta Cardo!
    static String processOracleDraw(String oracleText) {
        String draw = "";
        String numberOfCards;
        try {
            String incidence = "draw";
            String drawACard = "draw a card.";
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains(drawACard)) {
                draw = "auto=draw:1";
                return draw;
            } else if (oracleText.contains(incidence)) {
                numberOfCards = oracleText.substring(oracleText.indexOf(incidence) + incidence.length() + 1, oracleText.indexOf(" card"));
                numberOfCards = processStringNumberToValue(numberOfCards);

                draw = "auto=draw:" + numberOfCards.trim();
            }
        } catch (Exception e) {
        }
        return draw;
    }

    // Discard a card
    static String processOracleDiscarded(String oracleText) {
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

    static String processOracleTextTakeControl(String oracleText) {
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

    static String processOracleScry(String oracleText) {
        String scry = "";
        String scryNumber;
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "scry ";

            if (oracleText.contains(incidence)) {
                scryNumber = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(incidence) + incidence.length() + 1);

                scry = "auto=scry:" + scryNumber + " scrycore delayed dontshow donothing scrycoreend scryend";
                //scry = "auto=reveal:" + scryNumber;
            }
        } catch (Exception e) {
        }
        return scry;
    }

    // Can't be blocked by creature[]
    static String processOracleCantBeBlockedBy(String oracleText) {
        String cantBeBlocked = "";

        String strong = "can't be blocked by creatures with power ";
        int strongIncidence = oracleText.indexOf(strong);
        if (strongIncidence > 0) {
            String value = oracleText.substring(strongIncidence + strong.length(), strongIncidence + strong.length() + 1);
            cantBeBlocked = String.format("auto=cantbeblockedby(creature[power<=%s])", value);
            return cantBeBlocked;
        }

        String incidence = "can't be blocked by ";
        String creatures = " creatures";
        int indexIncidence = oracleText.indexOf(incidence);
        int creaturesIncidence = oracleText.indexOf(creatures);
        if (indexIncidence > 0 && creaturesIncidence > 0) {
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
    static String processOracleLord(String oracleText, String type) {
        String lord = "";
        String lordOrAll = "";
        String onlyOther = "";
        String creatureType = "creature";
        String condition = "";
        String effect = "";
        String withWhat;

        try {
            String incidence = "s you control";
            String justOther = "other ";
            oracleText = oracleText.toLowerCase();

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

                lord = String.format("auto=%s(%s%s%s|myBattlefield)%s", lordOrAll, onlyOther, creatureType, condition, effect);
            }
        } catch (Exception ex) {
            System.out.println("DANGER DANGER " + oracleText + "DANGER DANGER");
            System.out.println(ex.getMessage());
            //ex.printStackTrace();
        }
        return lord;
    }

    static String processOracleGainLife(String oracleText) {
        String gainLife = "";
        String gainNumber;
        try {
            oracleText = oracleText.toLowerCase();
            String incidenceG = "you gain ";
            String incidenceL = " life";

            if (oracleText.contains(incidenceG) && oracleText.contains(incidenceL)) {
                gainNumber = oracleText.substring(oracleText.indexOf(incidenceG) + incidenceG.length(), oracleText.indexOf(incidenceL));
                if (oracleText.contains("for each attacking creature you control.")) {
                    gainLife = "foreach(creature[attacking]|mybattlefield) " + "life:" + gainNumber;
                } else {
                    gainLife = "life:" + gainNumber;
                }

            }
        } catch (Exception e) {
        }
        return gainLife;
    }

    static String processOraclePutA(String oracleText, String Type) {
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

    static String processOracleExileDeath(String oracleText) {
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
                oracleText = oracleText.substring(oracleText.indexOf(incidence)+incidence.length());
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

    static String processOracleEpicSaga(String oracleText, String type) {
        String epicSaga = "";
        String epicSagaEffect;

        if (oracleText.contains("—")) {
            epicSaga = " " + oracleText.substring(0, oracleText.indexOf("—") + 2);
            epicSagaEffect = oracleText.substring(oracleText.indexOf("—") + 1);

            epicSagaEffect = AutoEffects.processEffect(epicSagaEffect, "EpicSaga");

            epicSaga = epicSaga.replace(" I, II —", "auto=counter(0/0,1,Lore)\nauto=@each my firstmain:counter(0/0,1,Lore)\nauto=");
            epicSaga = epicSaga.replace(" I —", "auto=counter(0/0,1,Lore)\nauto=@each my firstmain:counter(0/0,1,Lore)\nauto=");
            epicSaga = epicSaga.replace(" II —", "auto=@counteradded(0/0,1,Lore) from(this):this(counter{0/0.2.Lore}<=2)");
            epicSaga = epicSaga.replace(" III —", "auto=@counteradded(0/0,1,Lore) from(this):this(counter{0/0.3.Lore}) sacrifice(this)"
                    + "\nauto=@counteradded(0/0,1,Lore) from(this):this(counter{0/0.3.Lore}) ");

            epicSaga += epicSagaEffect;
        }
        return epicSaga;
    }

    static String processOracleChooseOneOrBoth(String oracleText) {
        String chooseOneOrBoth = "";
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "choose one or both";

            if (oracleText.contains(incidence)) {
                chooseOneOrBoth = "auto=alternative \n"
                        + "auto=alternative \n"
                        + "other=name(Both)";
            }
        } catch (Exception e) {
        }
        return chooseOneOrBoth;
    }

    static String processOracleTriggers(String oracleText, String cardName, String type, String subtype, String power) {
        String trigger = "";
        String triggerEffect;
        if (subtype.contains("Aura") || subtype.contains("Equipment")) {
            return "";
        }
        try {
            cardName = cardName.replace(",", "");

            if ((oracleText.toLowerCase().contains("whenever ")
                    || oracleText.contains("At the beginning")
                    || oracleText.contains(cardName + " enters the battlefield")
                    || oracleText.contains("When " + cardName + " dies,")
                    || oracleText.contains("When this creature dies")
                    || oracleText.contains(cardName + " becomes the target of a spell or ability")
                    || oracleText.contains("becomes the target of a spell or ability an opponent controls")
                    || oracleText.contains("Mentor")
                    || oracleText.contains("Raid "))
                    && !(oracleText.contains("Whenever you cast"))) {

                if (oracleText.contains("Mentor")) {
                    trigger = "auto=@combat(attacking) source(this):";

                    String mentor = AutoLineGRN.processOracleMentor(oracleText, power);
                    if (mentor.length() > 0) {
                        return trigger += mentor;
                    }
                }

                trigger = oracleText.substring(0, oracleText.indexOf(","));
                triggerEffect = oracleText.substring(oracleText.indexOf(",") + 1);

                trigger = trigger.replace("Enrage — ", "");
                trigger = trigger.replace("Raid — ", "");

                trigger = trigger.replace(cardName + " enters the battlefield tapped", "tap(noevent)");
                trigger = trigger.replace("When " + cardName + " enters the battlefield", "");

                trigger = trigger.replace("When ", "@");
                trigger = trigger.replace("Whenever ", "@");
                trigger = trigger.replace(" you gain life ", "lifeof(player):");
                trigger = trigger.replace("you may pay ", "pay(");
                trigger = trigger.replace(". If you do ", "):");
                trigger = trigger.replace("you draw a card","drawof(player):");
                trigger = trigger.replace(cardName + " becomes tapped","tapped(this):");
                // Moved to battlefield
                trigger = trigger.replace("a creature enters the battlefield under your control", "movedTo(creature|myBattlefield):");
                trigger = trigger.replace("a land enters the battlefield under your control", "movedTo(land|myBattlefield):");
                trigger = trigger.replace("a creature enters the battlefield", "movedTo(creature|Battlefield):");
                trigger = trigger.replace("enters the battlefield under your control", "movedTo(*[]|myBattlefield):");

                trigger = trigger.replace(cardName + " attacks", "combat(attacking) source(this):");
                trigger = trigger.replace(cardName + " and at least two other creatures attack", "combat(attacking) source(this) restriction{type(other creature[attacking]|myBattlefield)~morethan~1}:");

                trigger = trigger.replace(cardName + " dies", "movedTo(this|graveyard) from(battlefield):");
                trigger = trigger.replace("When this creature dies", "movedTo(this|graveyard) from(battlefield):");
                trigger = trigger.replace("a creature dies", "movedTo(creature|graveyard) from(battlefield):");
                trigger = trigger.replace("another creature dies", "movedTo(other creature|graveyard) from(battlefield):");
                trigger = trigger.replace("another creature or planeswalker you control dies", "movedTo(other *[creature;planeswalker]|graveyard) from(myBattlefield):");
                trigger = trigger.replace("a creature you control dies", "movedTo(creature|graveyard) from(mybattlefield):");
                trigger = trigger.replace("a creature an opponent controls dies", "movedTo(creature|graveyard) from(opponentbattlefield):");
                trigger = trigger.replace("a creature or planeswalker you control dies", "movedTo(creature,planeswalker|graveyard) from(mybattlefield):");
                trigger = trigger.replace(" dies", "movedTo(|graveyard) from(battlefield):");
                // Phases
                trigger = trigger.replace("At the beginning of ", "@");
                trigger = trigger.replace("your upkeep", "each my upkeep:");
                trigger = trigger.replace("your draw step", "each my draw:");
                trigger = trigger.replace("your precombat main phase", "each my firstmain:");
                trigger = trigger.replace("combat on your turn", "each my combatbegins:");
                trigger = trigger.replace("your end step", "each my endofturn:");
                trigger = trigger.replace("each end step", "each endofturn:");

                trigger = trigger.replace("If you attacked with a creature this turn", "if raid then ");
                trigger = trigger.replace("if it was kicked", "kicked ");

                trigger = trigger.replace(cardName + " is dealt damage", "damaged(this):");
                trigger = trigger.replace("you gain life", "lifeof(player):");

                trigger = trigger.replace("becomes the target of a spell or ability an opponent controls", "targeted(this|mybattlefield) from(*|opponentbattlefield,opponenthand,opponentstack,opponentgraveyard,opponentexile,opponentlibrary):");
                trigger = trigger.replace(cardName + " becomes the target of a spell or ability", "targeted(this):");

                triggerEffect = AutoEffects.processEffect(triggerEffect, cardName);
                trigger = "auto=" + trigger + triggerEffect;

            }
        } catch (Exception e) {
        }
        return trigger;
    }

    static String processOracleInstantSorcery(String oracleText) {
        String effect = oracleText;
        return effect;
    }
}
