package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class AutoLine {

    static String processOracleActivatedAbilityCost(String oracleText, String subtype) {
        String activatedAbility = "";
        String activatedAbilityCost;
        String activatedAbilityEffect;
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
                activatedAbilityCost = activatedAbilityCost.replace("Discardacard", "{D}");
                activatedAbilityCost = activatedAbilityCost.replace("Sacrifice", "{S}");
                activatedAbilityCost = activatedAbilityCost.replace("Exile", "{E}");
                activatedAbilityCost = activatedAbilityCost.replace("Pay5life", "{L:5}");
                activatedAbilityCost = activatedAbilityCost.replace(",", "");
                activatedAbilityCost = activatedAbilityCost.replace(" ", "");
                activatedAbilityCost = activatedAbilityCost.replace(".", "");
                
                activatedAbilityEffect = oracleText.substring(oracleText.indexOf(incidence) + 1);
                activatedAbilityEffect = activatedAbilityEffect.substring(0, activatedAbilityEffect.indexOf("."));
                String activatedAbilityCostEffect = processOracleActivatedAbilityEffect(activatedAbilityEffect, activatedAbilityCost);

                activatedAbility = "auto=" + activatedAbilityCostEffect;
            }

        } catch (Exception ex) {

        }
        return activatedAbility;
    }

    static String processOracleActivatedAbilityEffect(String activatedAbilityEffect, String activatedAbilityCost) {
        String activatedAbilityCostEffect = "";
        String adapt = "Adapt ";
        if (activatedAbilityEffect.contains(adapt)) {
            String adaptNumber = activatedAbilityEffect.substring(activatedAbilityEffect.indexOf(adapt) + adapt.length());
            activatedAbilityCostEffect = "this(counter{1/1}<1)while " + activatedAbilityCost + "counter(1/1," + adaptNumber + ")";
        }
        activatedAbilityCostEffect = activatedAbilityCost + activatedAbilityEffect;
        return activatedAbilityCostEffect;
    }

    //auto={W}{U}:transforms((Bird Artifact Creature,setpower=2,settoughness=2,blue,white,flying)) ueot
    //text={T}: Add {W} or {U} to your mana pool. -- {W}{U}: Azorius Keyrune becomes a 2/2 white and blue Bird artifact creature with flying until end of turn.
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

    //Megamorph
    //facedown={3}
    //autofacedown={3}{W}{W}:morph
    //autofaceup=counter(1/1,5)
    protected static String processOracleTextMegaMorph(String oracleText) {
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

            String incidence = (type.equals("aura")) ? "Enchant " : "arget ";
            String incidenceEnd = (type.equals("aura")) ? "\n" : ".";

            if (oracleText.contains(incidence)) {
                target = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                target = target.substring(0, target.indexOf(incidenceEnd));
                if (target.equals("permanent")) {
                    myTarget = "*";
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
                        withWhat = withWhat.replace("or greater", ">=");
                        myTarget = "creature[" + withWhat + "]";
                    } else if (withWhat.contains("or less")) {
                        withWhat = withWhat.replace("or less", "<=");
                        myTarget = "creature[" + withWhat + "]";
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

    protected static String processOracleTextAuraEquipBonus(String oracleText) {
        String equip = "";
        String equipBuff;
        String equipHas;
        try {
            String incidence = "creature gets ";
            if (oracleText.contains(incidence)) {
                equipBuff = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(incidence) + incidence.length() + 5);
                equip += "auto=teach(creature) " + equipBuff.replace("+", "");// + "\n";
            }

            String incidenceEquipHas = " has ";
            if (oracleText.contains(incidenceEquipHas)) {
                equipHas = oracleText.substring(oracleText.indexOf(incidenceEquipHas) + incidenceEquipHas.length(), oracleText.lastIndexOf("."));
                equip += "\nauto=teach(creature) " + equipHas;
            }

            if (oracleText.contains("nchant")) {
                equip = equip.replace("teach(creature) ", "");
            }

        } catch (Exception ex) {

        }
        return equip;
    }

    //    other={3}{B}{R} name(Dash)
    protected static String processOracleTextDash(String oracleText) {
        String dash = "";
        String dashCost;
        try {
            if (oracleText.contains("Dash")) {
                dashCost = oracleText.substring(oracleText.indexOf("Dash") + 5);
                dashCost = dashCost.substring(0, dashCost.indexOf("(") - 1);
                dash = "other=" + dashCost + " name(Dash)\n"
                        + "auto=if paid(alternative) then transforms((,newability[haste],newability[phaseaction[endofturn sourceinplay] "
                        + "moveto(ownerhand) all(this)])) forever";
            }

        } catch (Exception ex) {

        }
        return dash;
    }

    //Enters the battlefield
      protected static String processOracleTextETB(String oracleText, String name) {
        String etb = "";
        try {
            if (oracleText.contains(name + " enters the battlefield")) {
                etb = "auto=";
                String create = AutoLine.processOracleCreate(oracleText);
                etb += create;
                if (oracleText.contains(name + " enters the battlefield, you may")) {
                    etb += "may";
                }
                if (oracleText.contains(name + " enters the battlefield tapped")) {
                    etb += "tap";
                }
            }
        } catch (Exception ex) {

        }
        return etb;
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

    //Prowess
    protected static String processOracleTextCast(String oracleText) {
        String cast = "";
        String occurrence = "Whenever you cast a ";
        String occurrenceEnd = "spell";
        String occurrenceCondition;
        String occurrenceSubString;

        if (oracleText.contains(occurrence)) {
            occurrenceSubString = oracleText.substring(oracleText.indexOf(occurrence) + occurrence.length(), oracleText.lastIndexOf("."));
            occurrenceCondition = occurrenceSubString.substring(0, occurrenceSubString.indexOf(occurrenceEnd)).trim();
            if (occurrenceCondition.equals("noncreature")) {
                occurrenceCondition = "*[-creature]";
            }

            cast = "auto=@movedTo(" + occurrenceCondition + "|mystack):";
            String create = AutoLine.processOracleCreate(oracleText);
            cast += create;
        }

        return cast;
    }

    //Prowess
    protected static String processOracleTextProwess(String oracleText) {
        String prowess = "";
        String prowessTrigger;

        if (oracleText.contains("Prowess")) {
            //prowessTrigger = oracleText.substring(oracleText.indexOf("Whenever you cast a "), oracleText.indexOf(","));
            prowessTrigger = "*[-creature]";
            prowess = "auto=@movedTo(" + prowessTrigger + "|mystack):1/1 ueot";
        }
        return prowess;
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

    protected static String processOracleTextCycling(String oracleText) {
        String cycling = "";
        String cyclingCost;
        try {
            if (oracleText.contains("Cycling")) {
                cyclingCost = oracleText.substring(oracleText.indexOf("Cycling") + 8, oracleText.indexOf(" ({"));
                cycling = "autohand=__CYCLING__(" + cyclingCost + ")";
            }
        } catch (Exception ex) {

        }
        return cycling;
    }

    protected static String processOracleTextAfflict(String oracleText, String name) {
        String afflict = "";
        String afflictDamage = "";
        try {
            String incidence = "Afflict";
            if (oracleText.contains(incidence)) {
                afflictDamage = oracleText.substring(oracleText.indexOf(incidence) + incidence.length() + 1, oracleText.indexOf(incidence) + incidence.length() + 2);
                afflict = "auto=@combat(blocked,turnlimited) source(this):life:-" + afflictDamage + " opponent";
            }
        } catch (Exception ex) {

        }
        return afflict;
    }

    protected static String processOracleTextAttacks(String oracleText, String name) {
        String attacks = "";
        try {
            if (oracleText.contains("Whenever " + name + " attacks")) {
                attacks = "auto=@combat(attacking) source(this):";
            }
        } catch (Exception ex) {

        }
        return attacks;
    }

    protected static String processOracleTextExert(String oracleText) {
        String exert = "";
        try {
            if (oracleText.contains("ou may exert ")) {
                exert = "auto=@combat(attacking) source(this):may freeze this";
            }
        } catch (Exception ex) {

        }
        return exert;
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
                //manaProduced = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(endIncidence));
                manaProduced = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
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
                return action;
            }
            if (oracleText.contains("Destroy ")) {
                action = "auto=destroy";
                return action;
            }
            if (oracleText.contains("Counter ")) {
                action = "auto=fizzle";
                return action;
            }
            if (oracleText.contains("Return ")) {
                action = "auto=moveTo(ownerHand)";
                return action;
            }
            if (oracleText.contains("Tap ")) {
                action = "auto=tap";
                return action;
            }
            if (oracleText.contains("Untap ")) {
                action = "auto=untap";
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

    //Embalm {5}{W} ({5}{W}, Exile this card from your graveyard: Create a token that's a copy of it,
    //except it's a white Zombie Angel with no mana cost. Embalm only as a sorcery.)
    static String processOracleTextEmbalm(String oracleText) {
        String embalm = "";
        String embalmCost;
        try {
            String incidence = "Embalm ";

            if (oracleText.contains(incidence)) {
                embalmCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                embalmCost = embalmCost.substring(0, embalmCost.indexOf(" ("));
                embalm = "autograveyard=" + embalmCost + "{E}:name(Embalm) clone this with(white) addtype(Zombie) asSorcery";

            }
        } catch (Exception ex) {

        }
        return embalm;
    }

    // create a 1/1 white Warrior creature token with vigilance.
    // create(Servo:Artifact Creature Servo:1/1)
    // auto=create(Thopter:Artifact Creature Thopter:1/1:flying)*2
    static String processOracleCreate(String oracleText) {
        String create = "";
        String tokenName = "name";
        String tokenType = "type";
        String tokenColor = "color";
        String tokenPT = "69";
        String tokenAbility = "";
        String tokenMultipl = "Mult";

        String incidence = "reate ";

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
            create = "auto=create(" + tokenName + ":" + tokenType + " " + tokenName + ":" + tokenPT + ":" + tokenColor + ":" + tokenAbility + ")" + tokenMultipl;
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
                numberOfCards = numberOfCards.replace("a", "1");
                numberOfCards = numberOfCards.replace("two", "2");
                numberOfCards = numberOfCards.replace("three", "3");
                numberOfCards = numberOfCards.replace("four", "4");
                numberOfCards = numberOfCards.replace("five", "5");
                numberOfCards = numberOfCards.replace("six", "6");
                numberOfCards = numberOfCards.replace("seven", "7");

                draw = "auto=draw:" + numberOfCards.trim();
            }
        } catch (Exception e) {
        }
        return draw;
    }

    // Discard a card
    static String processOracleTextDiscard(String oracleText) {
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

    static String processOracleTextEternalize(String oracleText) {
        String eternalize = "";
        String eternalizeCost;
        try {
            String incidence = "Eternalize";

            if (oracleText.contains(incidence)) {
                eternalizeCost = oracleText.substring(oracleText.indexOf(incidence) + incidence.length());
                eternalizeCost = eternalizeCost.substring(1, eternalizeCost.indexOf("(") - 1);
                eternalizeCost = eternalizeCost.replace(", Discard a card.", "{D}");
                eternalize = "autograveyard=" + eternalizeCost + "{E}:name(Eternalize) clone this with(black) addtype(Zombie) setpower=4 settoughness=4 asSorcery";
            }
        } catch (Exception ex) {

        }
        return eternalize;
    }

    // auto=@damaged(this):
    static String processOracleEnraged(String oracleText) {
        String enrage = "";
        try {
            String incidence = "Enrage";

            if (oracleText.contains(incidence)) {

                enrage = "auto=@damaged(this):";

            }
        } catch (Exception e) {
        }
        return enrage;
    }

    // auto=if scry then
    static String processOracleTextRaid(String oracleText) {
        String raid = "";
        try {
            String incidence = "Raid ";

            if (oracleText.contains(incidence)) {

                raid = "auto=if raid then";

            }
        } catch (Exception e) {
        }
        return raid;
    }

    static String processOracleScry(String oracleText) {
        String scry = "";
        String scryNumber;
        try {
            oracleText = oracleText.toLowerCase();
            String incidence = "scry ";

            if (oracleText.contains(incidence)) {
                scryNumber = oracleText.substring(oracleText.indexOf(incidence) + incidence.length(), oracleText.indexOf(incidence) + incidence.length() + 1);

                scry = "auto=scry:" + scryNumber;
            }
        } catch (Exception e) {
        }
        return scry;
    }

    static String processOraclecantBeBlockedBy(String oracleText) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
