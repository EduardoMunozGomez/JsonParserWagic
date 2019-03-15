package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class AutoLine {

    static String processOracleActivatedAbilityCost(String oracleText, String cardName) {
        String activatedAbility = "";
        String activatedAbilityCost;
        String activatedAbilityEffect;
        String nextActivatedAbility = "";
        try {
            String incidence = ":";
            //if (oracleText.contains("Eternalize")){
            //  return "";
            //}
            //if (oracleText.contains("Cycling")){
            //  return "";
            //}

            if (oracleText.contains(incidence)) {
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
        }
        if (nextActivatedAbility.length() > 0) {
            activatedAbility += "\n" + processOracleActivatedAbilityCost(nextActivatedAbility, "");
        }

        return activatedAbility;
    }

    static String processOracleActivatedAbilityEffect(String activatedAbilityEffect, String activatedAbilityCost) {
        String activatedAbilityCostEffect = "";
        String effect = "";
        String adapt = "Adapt ";
        String create = "";
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
        } catch (Exception ex) {

        }
        return combatDamage;
    }

    static String processOracleTextMyTarget(String oracleText, String type) {
        String target;
        String myTarget = "";

        try {
            if (oracleText.contains("any target")) {
                myTarget = "target=player,creature";
                return myTarget;
            }
            if (oracleText.contains("target spell")) {
                myTarget = "target=*|stack";
                return myTarget;
            }

            String incidence = (type.equals("Aura")) ? "Enchant " : "arget ";
            String incidenceEnd = (type.equals("Aura")) ? "\n" : ".";

            if (oracleText.contains(incidence)) {
                target = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                target = target.substring(0, target.indexOf(incidenceEnd));
                if (target.contains("permanent")) {
                    myTarget = "*";
                }
                if (target.contains("permanent you control")) {
                    myTarget = "*|myBattlefield";
                }
                if (target.contains("player")) {
                    myTarget = "player";
                }
                if (target.contains("opponent")) {
                    myTarget = "opponent";
                }
                if (target.contains("artifact")) {
                    myTarget = "artifact";
                }
                if (target.contains("creature")) {
                    myTarget = "creature";
                }
                if (target.contains("nonlegendary creature")) {
                    myTarget = "creature[-legendary]";
                }
                if (target.contains("enchantment")) {
                    myTarget = "enchantment";
                }
                if (target.contains("land")) {
                    myTarget = "land";
                }
                if (target.contains("planeswalker")) {
                    myTarget = "planeswalker";
                }
                if (target.contains("creature spell")) {
                    myTarget = "creature|stack";
                }
                if (target.contains("noncreature")) {
                    myTarget = "*[-creature]";
                }
                if (target.contains("noncreature spell")) {
                    myTarget = "*[-creature]|stack";
                }
                if (target.contains(" or ")) {
                    myTarget = target.replace(" or ", ",");
                }
                if (target.contains("nonland permanent")) {
                    myTarget = "*[-land]";
                }
                if (target.contains("nonbasic land")) {
                    myTarget = "land[-basic]";
                }
                if (target.contains("tapped creature")) {
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
                String creatureWith = "creature with ";
                if (target.contains(creatureWith)) {
                    String withWhat = target.substring(target.indexOf(creatureWith) + creatureWith.length());//, target.lastIndexOf(" "));
                    if (withWhat.contains("or greater")) {
                        String ptValue = withWhat.substring(withWhat.indexOf("or greater") - 2, withWhat.indexOf("or greater") - 1);
                        String condition = withWhat.substring(0, withWhat.indexOf("or greater") - 3);
                        myTarget = "creature[" + condition + ">=" + ptValue + "]";
                    } else if (withWhat.contains("or less")) {
                        String ptValue = withWhat.substring(withWhat.indexOf("or less") - 2, withWhat.indexOf("or less") - 1);
                        String condition = withWhat.substring(0, withWhat.indexOf("or less") - 3);
                        myTarget = "creature[" + condition + "<=" + ptValue + "]";
                    } else {
                        myTarget = "creature[" + withWhat + "]";
                    }
                }
                if (target.contains("creature you control")) {
                    myTarget = "creature|myBattlefield";
                }
                if (target.contains("creature you don't control")) {
                    myTarget = "creature|opponentBattlefield";
                }
                myTarget = "target=" + myTarget;
            }
            myTarget = myTarget.replace(",,", ",");
        } catch (Exception ex) {
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
                equipBuff = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(incidence) + incidence.length() + 5);
                equip += "auto=teach(creature) " + equipBuff.replace("+", "");// + "\n";
            }
            String incidenceEquipHas = " has ";
            if (oracleText.contains(incidenceEquipHas)) {
                if(oracleText.contains(incidenceAnd)){
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
            if (cardType.equals("Aura") || cardType.equals("InstantOrSorcery")) {
                equip = equip.replace("teach(creature) ", "");
            }

        } catch (Exception ex) {

        }
        return equip;
    }

    //Enters the battlefield
    protected static String processOracleTextETB(String oracleText, String name) {
        String etb = "";
        try {
            if (oracleText.contains(name + " enters the battlefield")) {
                etb = "auto=";
                if (oracleText.contains(name + " enters the battlefield tapped")) {
                    etb += "tap";
                }
                if (oracleText.contains(name + " enters the battlefield, you may")) {
                    etb += "may";
                }

                String create = processOracleCreate(oracleText);
                etb += create;
                String etbCounters = processOracleETBCounters(oracleText);
                etb += etbCounters;
                String gainLife = processOracleGainLife(oracleText);
                etb += gainLife;

            }
        } catch (Exception ex) {

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
                firebreatingCost = oracleText.substring(oracleText.indexOf("{"), oracleText.indexOf(name) - 2);;
                firebreating = "auto=" + firebreatingCost + ":1/0";
            }
        } catch (Exception ex) {

        }
        return firebreating;
    }

    protected static String processOracleTextCast(String oracleText) {
        String cast = "";
        String occurrence = "Whenever you cast a";
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
                occurrenceCondition = "*[legendary],artifact,enchantment[saga]";
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
            String create = AutoLine.processOracleCreate(oracleText);
            cast += create;
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

    protected static String processOracleTextAttacks(String oracleText, String name, String power) {
        String attacks = "";
        try {
            if (oracleText.contains("Whenever " + name + " attacks") || oracleText.contains("Whenever this creature attacks")) {
                attacks = "auto=@combat(attacking) source(this):";

                String mentor = AutoLineGRN.processOracleMentor(oracleText, power);
                attacks += mentor;
            }
        } catch (Exception ex) {

        }
        return attacks;
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
                //manaProduced = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                //System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " + manaProduced);
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

    static String processOracleTextExileDestroyDamage(String oracleText) {
        String action = "";
        try {
            if (oracleText.contains("Exile ")) {
                action = "auto=moveto(exile)";
                if (oracleText.contains("Exile all")) {
                    action += " all";
                }
                return action;
            }
            if (oracleText.contains("Destroy ")) {
                action = "auto=destroy";
                if (oracleText.contains("Destroy all")) {
                    action += " all";
                }
                return action;
            }
            if (oracleText.contains(" fights ")) {
                action = "auto=transforms((,newability[target(creature|opponentbattlefield) dynamicability<!powerstrike eachother!>])) ueot\n"
                        + "restriction=type(creature|opponentbattlefield)~morethan~0";
                return action;
            }
            if (oracleText.contains("Counter ")) {
                action = "auto=fizzle";
                return action;
            }
            if (oracleText.contains("Return ")) {
                action = "auto=moveTo(ownerHand)";
                if (oracleText.contains("Return all")) {
                    action += " all";
                }
                return action;
            }
            if (oracleText.contains("Tap ")) {
                action = "auto=tap";
                if (oracleText.contains("Tap all")) {
                    action += " all";
                }
                return action;
            }
            if (oracleText.contains("Untap ")) {
                action = "auto=untap";
                if (oracleText.contains("Untap all")) {
                    action += " all";
                }
                return action;
            }
            String dealsIncidence = " deals ";
            String dmgIncidence = " damage ";
            if (oracleText.contains(dealsIncidence) && oracleText.contains(dmgIncidence)) {
                String dmgAmount = oracleText.substring(oracleText.indexOf(dealsIncidence) + dealsIncidence.length(), oracleText.indexOf(dmgIncidence));
                action = "auto=damage:" + dmgAmount;
            }
        } catch (Exception ex) {

        }
        return action;
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
                create = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                create = create.substring(0, create.indexOf("."));
                tokenMultipl = create.substring(0, create.indexOf(" "));
                create = create.substring(create.indexOf(" ") + 1);
                tokenPT = create.substring(0, create.indexOf(" "));
                create = create.substring(create.indexOf(" ") + 1);
                create = create.replace(" and ", ",");
                tokenColor = create.substring(0, create.indexOf(" "));
                create = create.substring(create.indexOf(" ") + 1);
                tokenName = create.substring(0, create.indexOf(" "));
                create = create.substring(create.indexOf(" ") + 1);
                tokenType = create.substring(0, create.indexOf(" "));
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
                // auto=create(Thopter:Artifact Creature Thopter:1/1:blue:flying)*2
                // a 1/1 white Human creature token
                create = "create(" + tokenName + ":" + tokenType + " " + tokenName + ":" + tokenPT + ":" + tokenColor + ":" + tokenAbility + ")" + tokenMultipl;
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
            }
        } catch (Exception e) {
        }
        return scry;
    }

    // can't be blocked by creatures with power
    static String processOracleCantBeBlockedBy(String oracleText) {
        String cantBeBlocked = "";
        String incidence = "can't be blocked by creatures with power ";
        int indexIncidence = oracleText.indexOf(incidence);
        if (indexIncidence > 0) {
            String value = oracleText.substring(indexIncidence + incidence.length(), indexIncidence + incidence.length() + 1);
            cantBeBlocked = String.format("auto=cantbeblockedby(creature[power<=%s])", value);
        }

        return cantBeBlocked;
    }

    // the who not the what
    // auto=all(creature|myBattlefield) 3/3 ueot
    // Other creatures you control
    static String processOracleLord(String oracleText, String type) {
        String lord = "";
        String lordOrAll = "all";
        String onlyOther = "";
        String creatureType = "creature";
        String condition = "";
        String effect = "";
        String withWhat = "";

        try {
            String incidence = "creatures you control";
            String justOther = "other ";
            oracleText = oracleText.toLowerCase();
            if (oracleText.contains(incidence)) {
                if (oracleText.contains("other creatures you control")) {
                    lordOrAll = "lord";
                    onlyOther = "other ";
                } else if (oracleText.contains(justOther)) {
                    lordOrAll = "lord";
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

                effect = effect.replace("until end of turn", "ueot");

                lord = String.format("auto=%s(%s%s%s|myBattlefield)%s", lordOrAll, onlyOther, creatureType, condition, effect);
            }
        } catch (Exception e) {
            System.out.println("DANGER DANGER "  + oracleText + "DANGER DANGER");
            System.out.println(e.getMessage());
            e.printStackTrace();
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

                gainLife = "life:" + gainNumber;
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
        String exileDeath = "";
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "this turn, exile it instead";

            if (oracleText.contains(incidence)) {
                exileDeath = "exiledeath";
            }
        } catch (Exception e) {
        }
        return exileDeath;
    }

    static String processStringNumberToValue(String numberAsString) {
        
        numberAsString = numberAsString.replace(" a ", "1");
        numberAsString = numberAsString.replace(" one  ", "1");
        numberAsString = numberAsString.replace(" two ", "2");
        numberAsString = numberAsString.replace(" three ", "3");
        numberAsString = numberAsString.replace(" four ", "4");
        numberAsString = numberAsString.replace(" five ", "5");
        numberAsString = numberAsString.replace(" six ", "6");
        numberAsString = numberAsString.replace(" seven ", "7");
        
        return numberAsString;
    }

    static String processasLongAsIsMyTurn(String oracleText, String type) {                  
       String asLongAsIsMyTurn = "";
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "as long as it's your turn";

            if (oracleText.contains(incidence)) {
                asLongAsIsMyTurn = "auto=this(variable{controllerturn}>0)";
            }
        } catch (Exception e) {
        }
        return asLongAsIsMyTurn;
    }
}
