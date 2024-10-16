package utilities;

/**
 *
 * @author Eduardo_
 */
public class Constants {

    public static final String[] KEYWORDS = {"canloyaltyasinst", "nolegend", "cantbesacrified", "poisonsixtoxic", "poisonfourtoxic", "canloyaltytwice", "affinityalldeadcreatures", "affinitytwobasiclandtypes", "affinitybasiclandtypes", "affinityallcreatures", "affinitygravecreatures", "affinityopponentcreatures", "affinitygraveinstsorc", "affinityparty", "affinityenchantments", "affinitycontrollercreatures", "affinityattackingcreatures", "showopponenttoplibrary", "energyshroud", "expshroud", "noentertrg", "nodietrg", "canplayauraequiplibrarytop", "nomovetrigger", "protectionfromcoloredspells", "decayed", "noncombatvigor", "replacescry", "nofizzle alternative", "poisonshroud", "affinityplains", "affinityislands", "affinityswamps", "affinitymountains", "affinitygreencreatures", "poisonthreetoxic", "adventure", "hasreplicate", "handdeath", "cantpaylife", "reachshadow", "oppgraveexiler", "treason", "cantlose", "cantwin", "asflash", "canplaycreaturelibrarytop", "nonight", "adventure", "playershroud", "protection from white", "protection from blue", "protection from black", "protection from red", "protection from green", "toxic", "combattoughness", "affinityforests", "weak", "evadebigger", "discardtoplaybyopponent", "legendruleremove", "cantbeblockedby(ox)", "cantbeblockedby(eldrazi scion)", "oppgcreatureexiler", "legendarylandwalk", "auraward", "infect", "swamphome", "skulk", "islandhome", "desertwalk", "spellmastery", "nonbasiclandwalk", "dethrone", "cantattack", "cantpwattack", "librarydeath", "phasing", "cantmilllose", "wilting", "foresthome", "offering", "phantom", "vigor", "poisontwotoxic", "poisontoxic", "cantchangelife", "threeblockers", "cantlifelose", "shackler", "shufflelibrarydeath", "twodngtrg", "wither", "affinityartifacts", "leyline", "hydra", "totemarmor", "nosolo", "showopponenthand", "showcontrollerhand", "canplayfromexile", "canplayfromgraveyard", "noentertrg", "foretell", "exiledeath", "lifefaker", "partner", "hasnokicker", "hasstrive", "tokenizer", "hasaftermath", "mutate", "Flying", "Trample", "doublefacedeath", "boast", "isconspiracy", "hasdisturb", "daybound", "nightbound", "mentor", "hellbent", "overload", "adventure", "undamageable", "Haste", "training", "overload", "inplaydeath", "canbecommander", "storm", "soulbond", "split second", "fear", "mygraveexiler", "undying", "shadow", "flanking", "horsemanship", "plainswalk", "snowforestlandwalk", "snowplainslandwalk", "snowmountainlandwalk", "snowislandlandwalk", "snowswamplandwalk", "hiddenface", "hasotherkicker", "changeling", "shroud", "cycling", "exalted", "persist", "controllershroud", "devoid", "prowess", "madness", "flash", "defender", "flying", "intimidate", "first strike", "double strike", "deathtouch", "hexproof", "menace", "indestructible", "vigilance", "reach", "trample", "lifelink", "haste", "islandwalk", "swampwalk", "mountainwalk", "forestwalk", "showfromtoplibrary", "canplayinstantsorcerylibrarytop", "canplayartifactlibrarytop", "canplaylandlibrarytop", "canplayfromlibrarytop", "nomaxhand", "oppnomaxhand", "nofizzle", "unblockable", "oneblocker", "cantblock", "mustattack", "strong", "cloud", "lure", "sunburst", "mustblock", "nolifegain", "nolifegainopponent", "doesnotuntap", "protection from", "backgroundpartner", "modular"};
    public static final String[] STARTING_KEYWORDS = {"grade", "[card]", "[/card]", "text", "name", "mana", "type", "subtype", "power", "toughness", "auto", "target", "abilities", "aicode", "other", "otherrestriction", "flashback", "color", "suspend", "kicker", "buyback", "modular", "doublefaced", "bestow", "retrace", "partner", "#AUTO_DEFINE", "dredge", "backside", "restriction", "anyzone", "alias", "facedown", "crewbonus", "phasedoutbonus", "#"};
    public static final String[] VALID_TARGETS = {"*", "anytarget", "planeswalker", "instant|stack", "sorcery|stack", "aura|battlefield", "giant|mybattlefield", "instant,sorcery|mygraveyard", "mountain", "<anyamount>swamp|mybattlefield", "aura|mybattlefield", "goblin|mygraveyard", "<anyamount>plains|mybattlefield", "sorcery|mygraveyard", "<anyamount>spirit|mybattlefield", "instant[blue]|stack", "spirit,arcane|stack", "<anyamount>mountain|mybattlefield", "spirit", "<upto:1>sorcery|mygraveyard", "<prex>mountain", "creature", "artifact", "<upto:2>", "player", "land", "enchantment", "opponent", "*|stack", "equipment", "wall", "*|battlefield", "*|mybattlefield", "*|mygraveyard"};
    public static final String[] VALID_TRIGGERS = {"copied", "becomesmonarchfoeof", "becomesmonarchof", "countermod", "ninjutsued", "trained", "boasted", "foretold", "shuffledof", "shuffledfoeof", "scryed", "exploited", "bearerchosen", "defeated", "ringtemptedof", "producedmana", "takeninitiativeof", "drawn", "dungeoncompleted", "coinflipped", "experiencedof", "experiencedfoeof", "phasedin", "facedup", "lifefoeof", "lifelostfoeof", "vampired", "cycled", "transformed", "energizedof", "dierolled", "explored", "surveiled", "exerted", "energizedfoeof", "proliferateof", "damageof", "discarded", "mutated", "counterremoved", "damagefoeof", "tokencreated", "poisonedof", "poisonedfoeof", "lifelostof", "sacrificed", "lifeof", "movedto", "targeted", "combat", "damaged", "drawof", "each", "counteradded", "rebounded", "next", "drawfoeof", "tapped"};
    public static final String[] VALID_TYPES = {"artifact", "creature", "enchantment", "instant", "land", "legendary", "planeswalker", "sorcery", "tribal", "conspiracy", "emblem", "dungeon", "battle"};
    public static final String[] VALID_ZONES = {"mygraveyard", "opponentgraveyard", "targetownergraveyard", "targetcontrollergraveyard", "ownergraveyard", "graveyard", "targetedpersonsgraveyard", "myinplay", "opponentinplay", "targetownerinplay", "targetcontrollerinplay", "ownerinplay", "inplay", "targetedpersonsinplay", "mybattlefield", "opponentbattlefield", "targetownerbattlefield", "targetcontrollerbattlefield", "ownerbattlefield", "battlefield", "targetedpersonsbattlefield", "myhand", "opponenthand", "targetownerhand", "targetcontrollerhand", "ownerhand", "hand", "targetedpersonshand", "mylibrary", "opponentlibrary", "targetownerlibrary", "targetcontrollerlibrary", "ownerlibrary", "library", "targetedpersonslibrary", "myremovedfromgame", "opponentremovedfromgame", "targetownerremovedfromgame", "targetcontrollerremovedfromgame", "ownerremovedfromgame", "removedfromgame", "targetedpersonsremovefromgame", "myexile", "opponentexile", "targetownerexile", "targetcontrollerexile", "ownerexile", "exile", "targetedpersonsexile", "mystack", "opponentstack", "targetownerstack", "targetcontrollerstack", "ownerstack", "stack", "targetedpersonsstack", "myreveal", "opponentreveal", "targetownerreveal", "targetcontrollerreveal", "ownerreveal", "reveal", "targetedpersonsreveal", "mysideboard", "opponentsideboard", "targetownersideboard", "targetcontrollersideboard", "ownersideboard", "sideboard", "targetedpersonssideboard", "mycommandzone", "opponentcommandzone", "targetownercommandzone", "targetcontrollercommandzone", "ownercommandzone", "commandzone", "targetedpersonscommandzone", "mytemp", "opponenttemp", "targetownertemp", "targetcontrollertemp", "ownertemp", "temp", "targetedpersonstemp", "*", "sideboard", "commandzone", "reveal", "graveyard", "battlefield", "inplay", "hand", "library", "nonbattlezone", "stack", "exile", "mycastingzone", "myrestrictedcastingzone", "mycommandplay", "myhandlibrary", "mygravelibrary", "opponentgravelibrary", "mygraveexile", "opponentgraveexile", "opponentcastingzone", "opponentrestrictedcastingzone", "opponentcommandplay", "opponenthandlibrary", "mynonplaynonexile", "opponentnonplaynonexile", "myhandexilegrave", "opponenthandexilegrave", "myzones", "opponentzones"};

}
