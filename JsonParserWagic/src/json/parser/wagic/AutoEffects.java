package json.parser.wagic;

// @author Eduardo
public class AutoEffects {

    // Deternime if the card has an activated ability
    static String processOracleBit(String oracleBit, String cardName, String type, String subtype) {
        String[] activatedAbililty;
        String replacedOracleBit;

        activatedAbililty = oracleBit.split(":");

        if (activatedAbililty.length > 1) {
            replacedOracleBit = processActivatedAbililty(activatedAbililty, cardName, type);
        } else {
            return "";//replacedOracleBit = processEffect(oracleBit, cardName);
        }

        return replacedOracleBit;
    }

    // Activated ability cost
    private static String processActivatedAbililty(String[] actAbil, String cardName, String type) {
        String actAbilCost;
        String actAbilEffect;

        actAbilCost = actAbil[0];
        actAbilCost = actAbilCost.replace("Channel ", "autohand=");

        actAbilCost = actAbilCost.replace("discard your hand" + cardName, "reject all(*|myhand)");
        actAbilCost = actAbilCost.replace(", Discard " + cardName, "{discard}");
        actAbilCost = actAbilCost.replace("Discard a card", "{D(*|myhand)}");
        actAbilCost = actAbilCost.replace("Discard a land card", "{D(land|myhand)}");
        actAbilCost = actAbilCost.replace("Discard three cards", "{D(*|myhand)}{D(*|myhand)}{D(*|myhand)}");
        actAbilCost = actAbilCost.replace("Discard a creature card", "{D(creature|myhand)}");
        actAbilCost = actAbilCost.replace("Sacrifice a creature with defender", "{S(creature[defender]|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice " + cardName, "{S}");
        actAbilCost = actAbilCost.replace("Exile " + cardName, "{E}");
        actAbilCost = actAbilCost.replace("Sacrifice a creature", "{S(creature|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice two creatures", "{S(creature|myBattlefield)}{S(creature|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice an artifact", "{S(artifact|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice an artifact creature", "{S(creature[artifact]|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice a land", "{S(land|myBattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice another creature", "{S(other creature|mybattlefield)}");
        actAbilCost = actAbilCost.replace("Sacrifice another artifact", "{S(other artifact|mybattlefield)}");
        actAbilCost = actAbilCost.replace("Exile " + cardName + " from your graveyard", "{E}");
        actAbilCost = actAbilCost.replace("Pay ", "{L:");
        actAbilCost = actAbilCost.replace(" life", "}");

        actAbilCost = actAbilCost.replace(",", "");
        actAbilCost = actAbilCost.replace(" ", "");
        actAbilCost = actAbilCost.replace(".", "");

        if (type.contains("Planeswalker")) {
            //actAbilCost = modifyLoyaltyCounters(actAbilCost);
            actAbilCost = actAbilCost.replace("[", "");
            actAbilCost = actAbilCost.replace("]", "");
            actAbilCost = "{C(0/0," + actAbilCost + ",Loyalty)}";
        }

        actAbilCost = actAbilCost.concat(":");
        actAbilCost = "auto=" + actAbilCost;

        actAbilEffect = actAbil[1];
        actAbilEffect = processEffect(actAbilEffect, cardName);

        return actAbilCost + actAbilEffect;
    }

    static String processEffect(String oracleBit, String cardName) {
        String effect;
        effect = oracleBit;
        String manaOfAnyColor = "\nAdd{W}\n"
                + "auto={T}:Add{U}\n"
                + "auto={T}:Add{B}\n"
                + "auto={T}:Add{R}\n"
                + "auto={T}:Add{G}";

        //if (cardName.contains(" ")) { System.out.println("PRO EFF " + effect); }
//        if (oracleBit.contains("Mentor") || oracleBit.contains("Convoke") || oracleBit.toLowerCase().contains("surveil")) {
//            return "";
//        }
        effect = effect.toLowerCase();
        cardName = cardName.toLowerCase();

        if (effect.contains("create ")) {
            return AutoLine.Create(effect);
        }
        if (effect.contains("scry ")) {
            return AutoLine.Scry(effect);
        }

//        effect = effect.replace("amass 1", "_AMASS_(1)");
//        effect = effect.replace("amass 2", "_AMASS_(2)");
//        effect = effect.replace("amass 3", "_AMASS_(3)");
//        effect = effect.replace("amass 4", "_AMASS_(4)");
//        effect = effect.replace("amass x", "_AMASS_()");
        effect = effect.replace("enlist ", "_ENLIST_");
        effect = effect.replace("kicker ", "kicker=");
        effect = effect.replace("if it was kicked,", "if paid(kicker) then ");
        effect = effect.replace("proliferate", "_PROLIFERATE_");
        effect = effect.replace("exile this saga, then return it to the battlefield transformed under your control", "moveto(exile) and!( moveto(mybattlefield) and!( transforms((,newability[flip(backside)])) oneshot )! )!");

        effect = effect.replace("you win the game", "wingame");
        effect = effect.replace("this ability triggers only once each turn.", "turnlimited:");
        effect = effect.replace("activate only during your turn", "myturnonly");
        effect = effect.replace("that was dealt damage this turn", "[damaged]");
        effect = effect.replace("+1/+1 counter", "counter(1/1)");
        effect = effect.replace("loyalty counter", "counter(0/0,1,Loyalty)");
        effect = effect.replace("on the bottom of its owner's library", "bottomoflibrary");
        effect = effect.replace("creature with power 4 or greater", "creature[power>=4]");

        effect = effect.replace("return target nonland permanent you don't control to its owner's hand", "target(-land|opponentBattlefield) moveTo(ownerHand)");
        effect = effect.replace("to its owner's hand", "moveto(ownerhand)");
        effect = effect.replace("historic", "artifact,*[legendary],enchantment[saga]");
        effect = effect.replace("return it to the battlefield tapped under its owner's control at the beginning of the next end step", "(blink)ueot");
        effect = effect.replace("creatures", "creature");
        effect = effect.replace("activate this ability only once each turn", "limit:1");
        effect = effect.replace("sacrifice " + cardName, "{S}:");
        effect = effect.replace("sacrifice another creature", "{S(other creature|mybattlefield)}");
        effect = effect.replace("defending player controls", ")|opponentBattlefield)");
        effect = effect.replace("your opponents control", ")|opponentBattlefield)");
        effect = effect.replace("to any target", "target(creature,player)");
        effect = effect.replace("if you do, ", "");
        effect = effect.replace("another ", "other ");
        effect = effect.replace("return target creature an opponent controls to its owner's hand", "moveto(ownerHand) target(creature|opponentBattlefield)");
        effect = effect.replace("return target creature to its owner's hand", "moveto(ownerHand) target(creature)");
        effect = effect.replace("exile target nonland permanent an opponent controls until " + cardName + " leaves the battlefield.", "(blink)forsrc target(*[-land]|opponentbattlefield)");
        effect = effect.replace("you may search your library for a basic land card or gate card, reveal it, put it into your hand, then shuffle your library.", "target(land[basic;gate]|mylibrary) moveto(myhand)");
        effect = effect.replace("you may have it fight target creature you don't control", "may target(creature|opponentbattlefield) dynamicability<!powerstrike eachother!>");
        effect = effect.replace("activate this ability only any time you could cast a sorcery", "asSorcery");
        effect = effect.replace(cardName + " from your graveyard to your hand", "moveto(myhand) all(this)");
        effect = effect.replace("you may cast an instant or sorcery card from your hand without paying its mana cost", "may castcard(normal) notatarget(instant,sorcery|myhand)");
        effect = effect.replace(cardName + " enters the battlefield with a +1/+1 counter on it for each color of mana spent to cast it.", "counter(1/1,sunburst)");
        effect = effect.replace("as long as you've cast an instant or sorcery spell this turn, " + cardName, "auto=aslongas(instant,sorcery|mystack) >1");
        effect = effect.replace("legendary permanent that's an artifact, creature, or enchantment.", "artifact[legendary], creature[legendary], enchantment[legendary])");
        effect = effect.replace("each opponent discards a card", "ability$!name(discard) reject notatarget(*|myhand)!$ opponent");
        effect = effect.replace("each opponent loses 1 life", "life:-1 opponent");
        effect = effect.replace("to your hand", "moveto(ownerhand)");
        effect = effect.replace("whenever this creature attacks, ", "_ATTACKING_");
        effect = effect.replace("put a +1/+1 counter", "counter(1/1)");
        effect = effect.replace("put two +1/+1 counters on ", "counter(1/1,2)");
        effect = effect.replace("enters the battlefield under your control, ", "@movedTo(|myBattlefield):");
        effect = effect.replace("attacking creature without flying", "creature[attacking;-flying]");
        effect = effect.replace("can't be blocked this turn", ") unblockable");
        effect = effect.replace("can't be blocked", "unblockable");
        effect = effect.replace("can't block this turn", "cantblock");
        effect = effect.replace("another target ", "target(another ");
        effect = effect.replace("target ", "target(");
        effect = effect.replace("up to one", "<upto:1>");
        effect = effect.replace("up to two", "<upto:2>");
        effect = effect.replace("up to three", "<upto:3>");
        effect = effect.replace("prevent the next", "prevent:");
        effect = effect.replace("to the battlefield.", "moveTo(mybattlefield)");
        effect = effect.replace("creature doesn't untap during its controller's next untap step", "freeze");

        effect = effect.replace("you don't control", "|opponentBattlefield)");
        effect = effect.replace("creature you control", "creature|myBattlefield)");
        effect = effect.replace("each white creature", "(creature[white]");
        effect = effect.replace(" attacking creature with lesser power", "creature[attacking;power<=])");
        effect = effect.replace("nonland permanent an opponent controls", "*[-land]|opponentbattlefield)");
        effect = effect.replace("if you attacked with a creature this turn, ", "if raid then ");
        effect = effect.replace(" an opponent controls", "|opponentBattlefield)");
        effect = effect.replace(" you control", "|myBattlefield)");
        effect = effect.replace("loses all abilities", "loseabilities");
        effect = effect.replace("discard a card", "ability$!name(discard) reject notatarget(*|myhand)!$");
        effect = effect.replace("discards a card", "ability$!name(discard) reject notatarget(*|myhand)!$");
        effect = effect.replace("discards two cards", "ability$!name(discard) reject notatarget(<2>*|myhand)!$");

        effect = effect.replace("sacrifices a creature", "ability$!sacrifice notatarget(creature|mybattlefield)!$ targetedplayer");
        effect = effect.replace("exile " + cardName, "\nexiledeath");
        effect = effect.replace(" card from your graveyard", "|mygraveyard)");
        effect = effect.replace("take an extra turn after this one", "turns:+1");
        // Deplete (Mill)
        effect = effect.replace("puts the top card of their library into their graveyard", "deplete:1");
        effect = effect.replace("puts the top two cards of their library into their graveyard", "deplete:2");
        effect = effect.replace("puts the top three cards of their library into their graveyard", "deplete:3");
        effect = effect.replace("puts the top four cards of their library into their graveyard", "deplete:4");

        effect = effect.replace("put the top card of your library into your graveyard", "deplete:1");
        effect = effect.replace("put the top two cards of your library into your graveyard", "deplete:2");
        effect = effect.replace("put the top three cards of your library into your graveyard", "deplete:3");
        effect = effect.replace("put the top four cards of your library into your graveyard", "deplete:4");

        effect = effect.replace("you gain 1 life", "life:1");
        effect = effect.replace("you gain 2 life", "life:2");
        effect = effect.replace("you gain 3 life", "life:3");
        effect = effect.replace("you gain 4 life", "life:4");
        effect = effect.replace("you gain 5 life", "life:5");

        effect = effect.replace("you lose 1 life", "life:-1");
        effect = effect.replace("loses 1 life", "life:-1");
        effect = effect.replace("loses 2 life", "life:-2");
        effect = effect.replace("loses 3 life", "life:-3");
        effect = effect.replace("loses 4 life", "life:-4");
        effect = effect.replace("loses 5 life", "life:-5");
        effect = effect.replace("loses 10 life", "life:-10");

        effect = effect.replace("each ", "all(");
        effect = effect.replace("draw a card", "draw:1");
        effect = effect.replace("draw two cards", "draw:2");
        effect = effect.replace("draws two cards", "draw:2");
        effect = effect.replace("for each creature ", "type:creature:");
        effect = effect.replace("until end of turn", "ueot");
        effect = effect.replace("until your next turn", "uynt");
        effect = effect.replace("activate only as a sorcery", "asSorcery");
        effect = effect.replace(cardName + " enters the battlefield tapped.", "auto=tap(noevent)");
        effect = effect.replace(cardName + " gains", "");
        effect = effect.replace(cardName + " gets", "");
        effect = effect.replace(cardName + " has", "");

        effect = effect.replace("deals 1 damage", "damage:1");
        effect = effect.replace("deals 2 damage", "damage:2");
        effect = effect.replace("deals 3 damage", "damage:3");
        effect = effect.replace("deals 4 damage", "damage:4");
        effect = effect.replace("deals 5 damage", "damage:5");
        effect = effect.replace("deals x damage", "damage:x");

        effect = effect.replace("if you do, ", ":");
        effect = effect.replace("when you do, ", ":");
        effect = effect.replace("you may pay", "may pay(");
        effect = effect.replace("you may", "may");
        effect = effect.replace("add one mana of any color", manaOfAnyColor);

        effect = effect.replace(", and that creature gains", " && ");
        effect = effect.replace(" and gains ", " && ");
        effect = effect.replace(" and gain ", " && ");
        effect = effect.replace(" and gets ", " && ");
        effect = effect.replace(" and has ", " && ");
        effect = effect.replace(" and you ", " && ");
        effect = effect.replace(" and ", " && ");
        effect = effect.replace(" put a ", " ");

        effect = effect.replace("return ", "");
        effect = effect.replace(" gains ", ") ");
        effect = effect.replace(" gain ", ") ");
        effect = effect.replace(" gets ", ") ");
        effect = effect.replace(" get ", ") ");
        effect = effect.replace(" has ", ") ");
        effect = effect.replace(", then ", " && ");

        effect = effect.replace(" it) ", "");
        effect = effect.replace(" it ", "");
        effect = effect.replace(" on ", "");
        effect = effect.replace(" or ", ",");
        effect = effect.replace(" to ", " ");

        effect = effect.replace("} {", "}{");
        effect = effect.replace(":|", ":");
        effect = effect.replace("+", "");
        effect = effect.replace(".", "");
        effect = effect.replace(cardName, "");

        effect = effect.trim();

        return effect;
    }

    private static String modifyLoyaltyCounters(String actAbilCost) {
        actAbilCost = actAbilCost.replace("0", "{C(0/0,0,Loyalty)}");
        actAbilCost = actAbilCost.replace("+1", "{C(0/0,1,Loyalty)}");
        actAbilCost = actAbilCost.replace("+2", "{C(0/0,2,Loyalty)}");
        actAbilCost = actAbilCost.replace("+3", "{C(0/0,3,Loyalty)}");
        actAbilCost = actAbilCost.replace("+4", "{C(0/0,4,Loyalty)}");
        actAbilCost = actAbilCost.replace("+5", "{C(0/0,5,Loyalty)}");
        actAbilCost = actAbilCost.replace("+6", "{C(0/0,6,Loyalty)}");
        actAbilCost = actAbilCost.replace("+7", "{C(0/0,7,Loyalty)}");
        actAbilCost = actAbilCost.replace("+8", "{C(0/0,8,Loyalty)}");
        actAbilCost = actAbilCost.replace("+9", "{C(0/0,9,Loyalty)}");
        actAbilCost = actAbilCost.replace("+10", "{C(0/0,10,Loyalty)}");

        actAbilCost = actAbilCost.replace("−1", "{C(0/0,-1,Loyalty)}");
        actAbilCost = actAbilCost.replace("−2", "{C(0/0,−2,Loyalty)}");
        actAbilCost = actAbilCost.replace("−3", "{C(0/0,−3,Loyalty)}");
        actAbilCost = actAbilCost.replace("−4", "{C(0/0,−4,Loyalty)}");
        actAbilCost = actAbilCost.replace("−5", "{C(0/0,−5,Loyalty)}");
        actAbilCost = actAbilCost.replace("−6", "{C(0/0,−6,Loyalty)}");
        actAbilCost = actAbilCost.replace("−7", "{C(0/0,−7,Loyalty)}");
        actAbilCost = actAbilCost.replace("−8", "{C(0/0,−8,Loyalty)}");
        actAbilCost = actAbilCost.replace("−9", "{C(0/0,−9,Loyalty)}");
        actAbilCost = actAbilCost.replace("−10", "{C(0/0,−10,Loyalty)}");

        return actAbilCost;
    }
}
