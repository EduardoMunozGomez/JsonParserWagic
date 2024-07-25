package json.parser.wagic;

/**
 *
 * @author Eduardo_
 */
public class Triggers {

    static String processTriggers(String oracleText, String cardName, String type, String subtype, String power) {
        String trigger = "";
        String triggerEffect;
        if (subtype.contains("Aura") || subtype.contains("Equipment")) { //
            return "";
        }
        try {
            // Trigger conditions
            if ((oracleText.toLowerCase().contains("whenever ")
                    || oracleText.contains("When")
                    || oracleText.contains("As " + cardName)
                    || oracleText.contains("As long as")
                    || oracleText.contains("At the beginning")
                    || oracleText.contains(cardName + " enters the battlefield")
                    || oracleText.contains(cardName + " becomes the target of a spell or ability")
                    || oracleText.contains("becomes the target of a spell or ability an opponent controls")
                    || oracleText.contains("Mentor")
                    || oracleText.contains("Raid ")
                    || oracleText.toLowerCase().contains("ward "))
                    && !(oracleText.contains("Whenever you cast"))) {

                // The comma limits the trigger
                if (oracleText.contains(",")) {
                    trigger = oracleText.substring(0, oracleText.lastIndexOf(","));
                    triggerEffect = oracleText.substring(oracleText.lastIndexOf(",") + 1);
                } else {
                    triggerEffect = oracleText.replace(cardName + " enters the battlefield", "");
                }

                trigger = trigger.replace("Enrage - ", "");
                trigger = trigger.replace("Raid - ", "");
                trigger = trigger.replace("Valiant - ", "");
                trigger = trigger.replace("Threshold - ", "_THRESHOLD_");

                trigger = trigger.replace("When this Class becomes level 2", "@counteradded(0/0.1.Level) from(this) restriction{compare(hascntlevel)~equalto~2}:");
                trigger = trigger.replace("When this Class becomes level 3", "@counteradded(0/0.1.Level) from(this) restriction{compare(hascntlevel)~equalto~3}:");
                trigger = trigger.replace("Whenever this creature becomes the target of a spell or ability you control for the first time each turn", "_VALIANT_");
                trigger = trigger.replace("Whenever " + cardName + " becomes the target of a spell or ability you control for the first time each turn", "_VALIANT_");
                trigger = trigger.replace("Whenever " + cardName + " deals combat damage to a player,", "@combatdamaged(player) from(this):");
                trigger = trigger.replaceAll(" or another ([a-zA-Z]+) you control enters", "@movedTo(other $1|myBattlefield) ");

                trigger = trigger.replace("As long as you've lost life this turn", "aslongas(variable{oplifelost}>0) ");
                trigger = trigger.replace(cardName + " enters the battlefield tapped", "tap(noevent)");
                trigger = trigger.replace("When " + cardName + " enters", "");
                //trigger = trigger.replace("When " + cardName + " enters", "");
                trigger = trigger.replace("When this creature enters,", "");
                trigger = trigger.replace("When this creature enters", "");
                trigger = trigger.replace("When " + cardName + " enters the battlefield", "");
                trigger = trigger.replace("Whenever " + cardName + " enters the battlefield", "");
                trigger = trigger.replace("Whenever " + cardName + " attacks", "_ATTACKING_");
                trigger = trigger.replace("Whenever " + cardName, "");
                trigger = trigger.replace(cardName + " enters the battlefield", "");
                trigger = trigger.replace("when you sacrifice it", "@sacrificed(this):");
                trigger = trigger.replace("Whenever a player casts their second spell each turn", "@movedto(*|mystack) restriction{thisturn(*|mystack)~equalto~1}:\nauto=@movedto(*|opponentStack) restriction{thisturn(*|opponentStack)~equalto~1}:\"");
                trigger = trigger.replace("Whenever another creature you control enters", "@movedto(other creature|myBattlefield):");

                trigger = trigger.replace(" you gain life ", "lifeof(player) from(*[-lifefaker]|*):");
                trigger = trigger.replace("you may pay ", "pay(");
                trigger = trigger.replace(". If you do ", "):");
                trigger = trigger.replace("you draw a card", "drawof(player):");
                trigger = trigger.replace("Whenever you sacrifice an artifact or creature", "sacrificed(*[creature;artifact]|myBattlefield):");
                trigger = trigger.replace("Whenever you sacrifice an artifact", "sacrificed(artifact|myBattlefield):");
                trigger = trigger.replace("Whenever you sacrifice a creature", "sacrificed(creature|myBattlefield):");
                trigger = trigger.replace(cardName + " becomes tapped", "tapped(this):");
                // Moved to battlefield
                trigger = trigger.replace("a creature enters the battlefield under your control", "movedTo(creature|myBattlefield):");
                trigger = trigger.replace("another creature enters the battlefield under your control", "movedTo(other creature|myBattlefield):");
                trigger = trigger.replace("another artifact enters the battlefield under your control", "movedTo(other artifact|myBattlefield):");
                trigger = trigger.replace("a land enters the battlefield under your control", "movedTo(land|myBattlefield):");
                trigger = trigger.replace("a creature enters the battlefield", "movedTo(creature|Battlefield):");
                trigger = trigger.replace("enters the battlefield under your control", "movedTo(*[]|myBattlefield):");
                trigger = trigger.replace("enters the battlefield under an opponent's control", "movedTo(*[]|opponentBattlefield):");

                trigger = trigger.replace("or attacks", "\nauto=_ATTACKING_");
                trigger = trigger.replace("Whenever this creature attacks", "_ATTACKING_");
                trigger = trigger.replace(cardName + " and at least two other creatures attack", "_ATTACKING_restriction{type(other creature[attacking]|myBattlefield)~morethan~1}:");
                trigger = trigger.replace("Whenever " + cardName + " deals combat damage to a player", "combatdamaged(player) from(this):");
                trigger = trigger.replace("a Samurai or Warrior you control attacks alone", "combat(attackedalone) source(*[Samurai;Warrior]|myBattlefield):all(trigger[to])");

                trigger = trigger.replace("When " + cardName + " dies", "_DIES_");
                trigger = trigger.replace("Whenever " + cardName + " dies", "_DIES_");
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
                trigger = trigger.replace("your next end step", "my endofturn once");

                trigger = trigger.replace("If you attacked with a creature this turn", "if raid then ");
                trigger = trigger.replace("if it was kicked", "kicked ");
                trigger = trigger.replace("is turned face up", "autofaceup=");

                trigger = trigger.replace(cardName + " is dealt damage", "damaged(this):");
                trigger = trigger.replace("you gain life", "lifeof(player) from(*[-lifefaker]|*):");
                trigger = trigger.replace("with three or more creatures", "restriction{type(creature[attacking]|myBattlefield)~morethan~2}:");

                trigger = trigger.replace("becomes the target of a spell or ability an opponent controls", "targeted(this|mybattlefield) from(*|opponentbattlefield,opponenthand,opponentstack,opponentgraveyard,opponentexile,opponentlibrary):");
                trigger = trigger.replace(cardName + " becomes the target of a spell or ability", "targeted(this):");
                trigger = trigger.replace("Whenever another", "@other");
                trigger = trigger.replace("Whenever you attack", "@each my blockers:");
                trigger = trigger.replace("Whenever you ", "@");
                trigger = trigger.replace("Whenever a ", "@");
                trigger = trigger.replace("Whenever ", "@");
                trigger = trigger.replace("When ", "@");
                trigger = trigger.replace("cardName ", "");

                triggerEffect = AutoEffects.ProcessEffect(triggerEffect, cardName);
                trigger = "auto=" + trigger + triggerEffect;

            }
        } catch (Exception e) {
        }
        return trigger;
    }
}
