package json.parser.wagic;

// @author Eduardo
import java.io.FileWriter;
import java.io.IOException;

public class Metadata {

    //  [meta]
    //  author=Wagic Team
    //  name=Limited Edition Alpha
    //  year=1993
    //  total=295
    //  [/meta]        
    static void printMetadata(Object name, Object year, Object total, FileWriter myWriter) {
        try {
            myWriter.write("[meta]\n");
            myWriter.write("author=Wagic Team\n");
            myWriter.write("name=" + name + "\n");
            myWriter.write("year=" + year + "\n");
            myWriter.write("total=" + total + "\n");
            myWriter.write("[/meta]\n");

        } catch (IOException e) {
            System.out.println("An error occurred.");
        }

//        System.out.println("[meta]");
//        System.out.println("author=Wagic Team");
//        System.out.println("name=" + name);
//        System.out.println("year=" + year);
//        System.out.println("total=" + total);
//        System.out.println("[/meta]");
    }
}
