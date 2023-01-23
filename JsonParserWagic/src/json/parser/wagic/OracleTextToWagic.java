package json.parser.wagic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.simple.JSONArray;

// @author Eduardo
public class OracleTextToWagic {

    static void parseOracleText(JSONArray keywords, String oracleText, String cardName, String type, String subtype, String power, String manaCost) {
        String abilities = "";
        // Remove remainder text        
        Matcher textBetweenPar = Pattern.compile("\\((.*?)\\)").matcher(oracleText);
        while (textBetweenPar.find()) {
            oracleText = oracleText.replace(textBetweenPar.group(1), "");
        }
        oracleText = oracleText.replace("()", "");
        // Remove comma from Legendaries
        if (cardName.contains(",")) {
            Matcher legendName = Pattern.compile(cardName).matcher(oracleText);
            while (legendName.find()) {
                oracleText = oracleText.replace(legendName.group(), cardName.replace(",", ""));
            }
        }
        // abilities=
        if (keywords != null && (type.contains("Creature") || subtype.contains("Vehicle"))) {
            abilities = Abilities.processKeywordsAbilities(keywords, oracleText);
        }
        if (keywords == null && (type.contains("Creature") || subtype.contains("Vehicle"))) {
            abilities = Abilities.processAbilities(oracleText, "");
        }
//        if (type.contains("Enchantment")) {
//            abilities = Abilities.processEnchantmentAbilities(oracleText);
//        }
        if (type.contains("Instant") || type.contains("Sorcery")) {
            abilities = Abilities.processInstantOrSorceryAbilities(oracleText);
        }
        if (type.contains("Land")) {
            abilities = Abilities.processLandAbilities(oracleText);
        }
        if (abilities.length() > 0) {
            System.out.println("abilities=" + abilities.trim());
        }
        // Auto lines
        for (String oracleBit : oracleText.split("\n")) {
            // Exclude this types
            if (!(type.contains("Instant") || type.contains("Sorcery") || subtype.contains("Aura") || subtype.contains("Equipment"))) {
                // Evergreen mechanics
                autoLineExists(AutoEffects.processOracleBit(oracleBit, cardName, type, subtype));
                autoLineExists(AutoLine.ManaAbility(oracleBit, subtype));
                autoLineExists(AutoLine.Cast(oracleBit, cardName));
                autoLineExists(AutoLine.CombatDamage(oracleBit));
                autoLineExists(AutoLine.OppCasts(oracleBit));
                autoLineExists(AutoLine.Weak(oracleBit));
                autoLineExists(AutoLine.Discarded(oracleBit));
                autoLineExists(AutoLine.TakeControl(oracleBit));
                autoLineExists(AutoLine.CantBeBlockedBy(oracleBit));
                autoLineExists(AutoLine.Lord(oracleBit, type));
                autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                autoLineExists(AutoLine.processAsLongAs(oracleBit, cardName));
                autoLineExists(AutoLine.processForEach(oracleBit, type));
                autoLineExists(AutoLine.CostReduction(oracleBit));
                autoLineExists(AutoLine.Unearth(oracleBit));
                autoLineExists(AutoLine.Prototype(oracleBit));
                autoLineExists(AutoLine.Blitz(oracleBit));
                autoLineExists(AutoLineGRN.Surveil(oracleBit));

                //autoLineExists(AutoLineGRN.Kicker(oracleBit, cardName));
                //autoLineExists(AutoLine.Ninjutsu(oracleBit));
                //autoLineExists(AutoLineGRN.Undergrowth(oracleBit));
                /*autoLineExists(AutoLineGRN.Convoke(oracleBit));
                autoLineExists(AutoLineGRN.Addendum(oracleBit));
                autoLineExists(AutoLineGRN.Riot(oracleBit));
                autoLineExists(AutoLineGRN.Spectacle(oracleBit));
                autoLineExists(AutoLineGRN.Ascend(oracleBit));
                autoLineExists(AutoLineGRN.Partner(oracleBit));
                autoLineExists(AutoLineGRN.Amass(oracleBit));
                autoLineExists(AutoLineGRN.Proliferate(oracleBit));*/
            }
            //INSTANT, SORCERY
            if (type.contains("Instant") || type.contains("Sorcery")) {
                autoLineExists(AutoLine.MyTarget(oracleBit, "InstantOrSorcery", subtype));
                autoLineExists(AutoEffects.processEffect(oracleBit, type));
                autoLineExists(AutoLine.ExileDestroyDamage(oracleBit, type));
                autoLineExists(AutoLine.ReplacerAuraEquipBonus(oracleBit, "InstantOrSorcery"));
                autoLineExists(AutoLine.Create(oracleBit), true);
                autoLineExists(AutoLine.GainLife(oracleBit), true);
                autoLineExists(AutoLine.Draw(oracleBit));
                autoLineExists(AutoLine.PutA(oracleBit, type), true);
                autoLineExists(AutoLine.ChooseOneOrBoth(oracleBit));
                autoLineExists(AutoLineGRN.Kicker(oracleBit, cardName));
                //autoLineExists(AutoLineGRN.JumpStart(oracleBit, manaCost));
            }
            // AURA
            if (subtype.contains("Aura")) {
                autoLineExists(AutoLine.MyTarget(oracleBit, subtype, subtype));
                //autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                String auraEquipBonus = AutoLine.ReplacerAuraEquipBonus(oracleBit, subtype);
                if (!auraEquipBonus.isEmpty()) {
                    autoLineExists(auraEquipBonus);
                }
            }
            // EQUIPMENT
            if (subtype.contains("Equipment")) {
                //autoLineExists(AutoLine.Triggers(oracleBit, cardName, type, subtype, power));
                autoLineExists(AutoLine.AuraEquipBonus(oracleBit, "Equipment"));
                if (oracleBit.contains("Equip ")) {
                    autoLineExists(AutoLine.EquipCost(oracleBit));
                }
            }
            // SAGA
            if (subtype.contains("Saga")) {
                autoLineExists(AutoLine.EpicSaga(oracleBit, subtype));
                String epicSaga = "";
                //= AutoLine.ReplacerAuraEquipBonus(oracleBit, subtype);
                if (!epicSaga.isEmpty()) {
                    autoLineExists(epicSaga);
                }
            }
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
}
