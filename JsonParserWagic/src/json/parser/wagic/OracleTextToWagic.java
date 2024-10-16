package json.parser.wagic;

import org.json.simple.JSONArray;

// @author Eduardo
public class OracleTextToWagic {

    static void parseOracleText(JSONArray keywords, String oracleText, String cardName, String type, String subtype, String power, String manaCost) {
        String abilities = "";
        // Remove remainder text
        oracleText = oracleText.replaceAll("\\(.*?\\)", "");
        // Remove comma from Legendaries
        oracleText = oracleText.replace(cardName, cardName.replace(",", ""));
        cardName = cardName.replace(",", "");
        
        oracleText = oracleText.replace("•", "choice");

        // Keywords present in the dataset to populate abilities=
        if (keywords != null) {
            abilities = Abilities.processKeywordsAbilities(keywords, oracleText);
        }
        // Special abilities for creatures and vehicles
        if (keywords == null && (type.contains("Creature") || subtype.contains("Vehicle") || type.contains("Enchantment"))) {
            abilities = Abilities.processCustomAbilities(oracleText, "", cardName);
        }
        // abilities=
        if (abilities.length() > 0) {
            System.out.println("abilities=" + abilities);
        }

        // Auto lines
        for (String oracleBit : oracleText.split("\n")) {
            // Permanents, exclude Aura and Equipment
            if (!(type.contains("Instant") || type.contains("Sorcery") || subtype.contains("Aura") || subtype.contains("Equipment") || subtype.contains("Siege"))) {

                String hasActivatedAbility = ActivatedAbility.determineActivatedAbility(oracleBit, cardName, type, subtype);
                String hasTriggers = Triggers.processTriggers(oracleBit, cardName, type, subtype, power);

                autoLineExists(hasActivatedAbility);
                autoLineExists(hasTriggers);

                if (hasActivatedAbility.isEmpty()) {
                    if (hasTriggers.isEmpty()) {
                        oracleBit = removeKeywordAbilities(oracleBit);
                        autoLineExists(AutoEffects.processEffect(oracleBit, cardName),true);
                    }
                }

                //autoLineExists(AutoLine.threshold(oracleBit, manaCost));
                autoLineExists(AutoLine.impending(oracleBit, manaCost));
                autoLineExists(AutoLine.offspring(oracleBit, manaCost));
                autoLineExists(AutoLine.gift(oracleBit, manaCost));

                autoLineExists(AutoLine.bestow(oracleBit, manaCost));
                autoLineExists(AutoLine.plot(oracleBit, manaCost));

                //autoLineExists(AutoLine.ManaAbility(oracleBit, subtype));
                autoLineExists(AutoLine.CombatDamage(oracleBit));
                autoLineExists(AutoLine.OppCasts(oracleBit));
                autoLineExists(AutoLine.weak(oracleBit));
                autoLineExists(AutoLine.Discarded(oracleBit));
                autoLineExists(AutoLine.TakeControl(oracleBit));
                autoLineExists(AutoLine.CantBeBlockedBy(oracleBit));
                autoLineExists(AutoLine.Lord(oracleBit, type));
                autoLineExists(AutoLine.processAsLongAs(oracleBit, cardName));
                autoLineExists(AutoLine.processForEach(oracleBit, type));
                autoLineExists(AutoLine.CostReduction(oracleBit));
                //autoLineExists(AutoLine.surveil(oracleBit));
                //autoLineExists(AutoLine.scry(oracleBit));
                //autoLineExists(AutoLine.Disguise(oracleBit));
                //autoLineExists(AutoLine.Backup(oracleBit));
                //autoLineExists(AutoLine.Corrupted(oracleBit, cardName));

                autoLineExists(AutoLineGRN.Proliferate(oracleBit));
                autoLineExists(AutoLineGRN.convoke(oracleBit));

                autoLineExists(AutoLine.Unearth(oracleBit));
                autoLineExists(AutoLine.Prototype(oracleBit));
                autoLineExists(AutoLine.Blitz(oracleBit));
                autoLineExists(AutoLineGRN.Kicker(oracleBit, cardName));
                autoLineExists(AutoLine.Ninjutsu(oracleBit));
                autoLineExists(AutoLineGRN.Undergrowth(oracleBit));
                autoLineExists(AutoLineGRN.Addendum(oracleBit));
                autoLineExists(AutoLineGRN.Riot(oracleBit));
                autoLineExists(AutoLineGRN.Spectacle(oracleBit));
                autoLineExists(AutoLineGRN.Ascend(oracleBit));
                autoLineExists(AutoLineGRN.Partner(oracleBit));
                autoLineExists(AutoLineGRN.Amass(oracleBit));
            }
            //INSTANT, SORCERY
            if (type.contains("Instant") || type.contains("Sorcery")) {
                //autoLineExists(AutoLine.Corrupted(oracleBit, cardName));
                autoLineExists(AutoLine.gift(oracleBit, manaCost));
                autoLineExists(AutoLine.flashback(oracleBit, manaCost));
                autoLineExists(AutoLine.plot(oracleBit, manaCost));

                autoLineExists(AutoLine.ChooseOneOrBoth(oracleBit));
                autoLineExists(AutoLine.MyTarget(oracleBit, "InstantOrSorcery", subtype));
                autoLineExists(AutoEffects.processEffect(oracleBit, type), true);
                autoLineExists(AutoLine.exileDestroyDamage(oracleBit, type));
                //autoLineExists(AutoLine.ReplacerAuraEquipBonus(oracleBit, "InstantOrSorcery"));
                autoLineExists(AutoLine.PutA(oracleBit, type));
//              autoLineExists(AutoLine.Casualty(oracleBit, cardName, manaCost));
                //autoLineExists(AutoLine.surveil(oracleBit));
                //autoLineExists(AutoLineGRN.convoke(oracleBit));

                //autoLineExists(AutoLineGRN.Kicker(oracleBit, cardName));
                //autoLineExists(AutoLineGRN.JumpStart(oracleBit, manaCost));
            }
            // AURA
            if (subtype.contains("Aura")) {
                autoLineExists(AutoLine.MyTarget(oracleBit, subtype, subtype));

                autoLineExists(AutoLine.Corrupted(oracleBit, cardName));
                autoLineExists(AutoLineGRN.convoke(oracleBit));

                //autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                String auraEquipBonus = AutoLine.ReplacerAuraEquipBonus(oracleBit, subtype);
                if (!auraEquipBonus.isEmpty()) {
                    autoLineExists(auraEquipBonus);
                }
                autoLineExists(AutoLine.gift(oracleBit, manaCost));

            }
            // EQUIPMENT
            if (subtype.contains("Equipment")) {
                //autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                autoLineExists(AutoLineGRN.convoke(oracleBit));
                autoLineExists(AutoLine.auraEquipBonus(oracleBit, "Equipment"));
                //autoLineExists(AutoLine.ForMirrodin(oracleBit, "Equipment"));
                if (oracleBit.contains("Equip ")) {
                    autoLineExists(AutoLine.equipCost(oracleBit));
                }
            }
            // SAGA
            //if (subtype.contains("Saga")) {
            //  autoLineExists(AutoLine.EpicSaga(oracleBit, subtype));
            //}
        }
    }

    static void autoLineExists(String outcome, boolean... needAuto) {
        if (outcome.length() > 0) {
            outcome += "\n";
            if (needAuto.length > 0) {
                outcome = "auto=" + outcome;
            }
        }

        System.out.print(outcome);
    }

    static String removeKeywordAbilities(String oracleBit) {
        // Define una lista de habilidades a remover
        String[] abilities = {
            "flash", "defender", "flying", "intimidate", "first strike", "double strike",
            "deathtouch", "hexproof", "menace", "indestructible", "vigilance", "reach",
            "trample", "lifelink", "haste", "islandwalk", "swampwalk", "mountainwalk",
            "forestwalk", "devoid", "partner" };

        if (oracleBit.contains("rowess")) {
            oracleBit = "@movedTo(*[-creature]|mystack):1/1 ueot";
        }

        // Itera sobre cada habilidad y reemplázala ignorando mayúsculas/minúsculas
        for (String ability : abilities) {
            oracleBit = oracleBit.replaceAll("(?i)" + ability + ",?", "");
        }

        return oracleBit;
    }
}
/*          "you may look at the top card of your library any time", "cycling",
            "you have no maximum hand size", "can't be countered", "can't be blocked.",
            "can't be blocked by more than one creature", "toxic 1", "toxic 2", "toxic 3",
            "can't block.", "attacks each turn if able.", "attacks each combat if able.",
            "power can't block it.", "can block only creatures with flying.",
            "all creatures able to block ", "counter on it for each color of mana spent to cast it",
            "blocks each turn if able", "blocks each combat if able", "players can't gain life",
            "opponents can't gain life", "doesn't untap during your untap step", "protection from",
            "affinity for artifacts", "choose a background", "modular", "changeling" */
