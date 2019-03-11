package JasonParserWagic;

/**
 *
 * @author Eduardo
 */
public class Metadata {

    //  [meta]
    //  author=Wagic Team
    //  name=Limited Edition Alpha
    //  year=1993
    //  total=295
    //  [/meta]        
    static void printMetadata(Object name, Object year, Object total) {
        System.out.println("[meta]");
        System.out.println("author=Wagic Team");
        System.out.println("name=" + name);
        System.out.println("year=" + year);
        System.out.println("total=" + total);
        System.out.println("[/meta]");
    }
}
