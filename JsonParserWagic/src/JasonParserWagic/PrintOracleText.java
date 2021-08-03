package JasonParserWagic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// @author Eduardo
public class PrintOracleText {

    static void parseOracleText(String oracleText, String cardName, String type, String subtype, String power, String manaCost) {
        String abilities = "";
        String[] oracleSplit;
        // Remove remainder text        
        Matcher textBetweenPar = Pattern.compile("\\((.*?)\\)").matcher(oracleText);
        while (textBetweenPar.find()) {
            oracleText = oracleText.replace(textBetweenPar.group(1), "");
        }
        oracleText = oracleText.replace("()", "");
        // Remove comma from Legendaries
        if (cardName.contains(",")) {
            Matcher legedName = Pattern.compile(cardName).matcher(oracleText);
            while (legedName.find()) {
                oracleText = oracleText.replace(legedName.group(), cardName.replace(",", ""));
            }
        }
        // Keywords (abilities=)
        if (type.contains("Creature")) {
            abilities = Abilities.processAbilities(oracleText);
        }
        //if (type.contains("Enchantment")) {
            //abilities = Abilities.processEnchantmentAbilities(oracleText);
        //}
        if (type.contains("Instant") || type.contains("Sorcery")) {
            abilities = Abilities.processInstantOrSorceryAbilities(oracleText);
        }
        if (type.contains("Land")) {
            abilities = Abilities.processLandAbilities(oracleText);
        }
        if (abilities.length()>0) {
            System.out.println("abilities=" + abilities.trim());
        }
        // Auto lines
        oracleSplit = oracleText.split("\n");
        for (String oracleBit : oracleSplit) {
            // Evergreen mechanics
            autoLineExists(AutoEffects.processOracleBit(oracleBit, cardName, type, subtype));
            autoLineExists(AutoLine.processOracleTextManaAbility(oracleBit, subtype));
            //check(AutoLineGRN.processOracleUndergrowth(oracleBit));
            //check(AutoLineGRN.processOracleKicker(oracleBit, cardName));
            autoLineExists(AutoLine.processOracleTextCast(oracleBit, cardName));
            autoLineExists(AutoLine.processOracleTextCombatDamage(oracleBit));

            autoLineExists(AutoLine.processOracleTextOppCasts(oracleBit));
            autoLineExists(AutoLine.processOracleTextWeak(oracleBit));
            autoLineExists(AutoLine.processOracleDiscarded(oracleBit));
            autoLineExists(AutoLine.processOracleTextTakeControl(oracleBit));
            autoLineExists(AutoLine.processOracleScry(oracleBit));
            autoLineExists(AutoLine.processOracleCantBeBlockedBy(oracleBit));
            autoLineExists(AutoLine.processOracleLord(oracleBit, type));
            autoLineExists(AutoLine.processOracleTriggers(oracleBit, cardName, type, subtype, power));
            autoLineExists(AutoLine.processAsLongAs(oracleBit, cardName));
            autoLineExists(AutoLine.processForEach(oracleBit, type));
          /*autoLineExists(AutoLineGRN.processOracleConvoke(oracleBit));
            autoLineExists(AutoLineGRN.processOracleSurveil(oracleBit));
            autoLineExists(AutoLineGRN.processOracleAddendum(oracleBit));
            autoLineExists(AutoLineGRN.processOracleRiot(oracleBit));
            autoLineExists(AutoLineGRN.processOracleSpectacle(oracleBit));
            autoLineExists(AutoLineGRN.processOracleAscend(oracleBit));
            autoLineExists(AutoLineGRN.processOraclePartner(oracleBit));*/
            autoLineExists(AutoLineGRN.processOracleAmass(oracleBit));
            autoLineExists(AutoLineGRN.processOracleProliferate(oracleBit));
            
            //INSTANT, SORCERY
            if (type.contains("Instant") || type.contains("Sorcery")) {
                autoLineExists(AutoLine.processOracleTextMyTarget(oracleBit, "InstantOrSorcery", subtype));
                autoLineExists(AutoEffects.processEffect(oracleBit, type));
                autoLineExists(AutoLine.processOracleTextExileDestroyDamage(oracleBit, type));
                autoLineExists(AutoLine.processOracleREPLACERAuraEquipBonus(oracleBit, "InstantOrSorcery"));
                autoLineExists(AutoLine.processOracleCreate(oracleBit), true);
                autoLineExists(AutoLine.processOracleGainLife(oracleBit), true);
                autoLineExists(AutoLine.processOracleDraw(oracleBit));
                autoLineExists(AutoLine.processOraclePutA(oracleBit, type), true);
                autoLineExists(AutoLine.processOracleChooseOneOrBoth(oracleBit));

                autoLineExists(AutoLineGRN.processOracleJumpStart(oracleBit, manaCost));
            }
            // AURA
            if (subtype.contains("Aura")) {
                autoLineExists(AutoLine.processOracleTextMyTarget(oracleBit, subtype, subtype));
                String auraEquipBonus = AutoLine.processOracleREPLACERAuraEquipBonus(oracleBit, subtype);
                if (!auraEquipBonus.isEmpty()) {
                    autoLineExists(auraEquipBonus);
                }
            }
            // EQUIPMENT
            if (subtype.contains("Equipment")) {
                autoLineExists(AutoLine.processOracleTextAuraEquipBonus(oracleBit, "Equipment"));
                autoLineExists(AutoLine.processOracleTextEquipCost(oracleBit));
            }
            /*/ SAGA
            if (subtype.contains("Saga")) {
                autoLineExists(AutoLine.processOracleEpicSaga(oracleBit, subtype));
                String epicSaga = "";
                //= AutoLine.processOracleREPLACERAuraEquipBonus(oracleBit, subtype);
                if (!epicSaga.isEmpty()) {
                    autoLineExists(epicSaga);
                }
            }*/
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
