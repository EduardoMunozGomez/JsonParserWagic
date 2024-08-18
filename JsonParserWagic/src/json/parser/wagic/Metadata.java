package json.parser.wagic;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The Metadata class provides a method to print metadata information for a set of cards.
 * It writes the metadata to a FileWriter in a specific format used by Wagic.
 * 
 * @author Eduardo
 */
public class Metadata {

    /**
     * Writes metadata information to the provided FileWriter.
     * The metadata includes the author, set name, year, and total number of cards.
     * 
     * @param name   The name of the card set.
     * @param year   The year the card set was released.
     * @param total  The total number of cards in the set.
     * @param myWriter The FileWriter to write the metadata to.
     */
    static void printMetadata(Object name, Object year, Object total, FileWriter myWriter) {
        try {
            myWriter.write("[meta]\n");
            myWriter.write("author=Wagic Team\n");
            myWriter.write("name=" + name + "\n");
            myWriter.write("year=" + year + "\n");
            myWriter.write("total=" + total + "\n");
            myWriter.write("[/meta]\n");
        } catch (IOException e) {
            System.err.println("An error occurred while writing metadata: " + e.getMessage());
        }
    }
}

