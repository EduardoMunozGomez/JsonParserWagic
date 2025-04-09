package utilities;

/**
 *
 * @author Eduardo_
 */
import java.io.*;

public class SyntaxCheck {

    public static void main(String[] args) {
        // Replace the path below with the path to your folder
        String folderName = args[0];
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {

            if (file.isFile()) {
                try {
                    try ( // Open the file for reading
                            BufferedReader br = new BufferedReader(new FileReader(file))) {
                        String line;
                        int lineNumber = 1;
                        while ((line = br.readLine()) != null) {
                            if (line.startsWith("text=")||line.startsWith("name=")) {
                                // Ignore this line, it starts with the prefix "text="
                                lineNumber++;
                                continue;
                            }
                            // Check if the line contains a comma between square brackets
                            if (line.matches("^(?!.*phaseaction\\[[^\\[\\]]*\\,[^\\[\\]]*\\])[^\\[\\]]*\\[[^\\[\\]\\(\\)]*\\,[^\\[\\]\\(\\)]*\\].*$")) {
                                System.out.println("Error in " + file.getName() + " line " + lineNumber + ": " + line);
                            }

                            // Check for a space before a parenthesis, excluding '(blink)'
                            if (line.matches(".* \\((?!blink).*")) {
                                System.out.println("Error: Space before parenthesis in " + file.getName() + " line " + lineNumber + ": " + line);
                            }
                            lineNumber++;
                        }
                        // Close the file
                    }
                } catch (IOException e) {
                    System.err.println("Error SyntaxCheck: " + e.getMessage());
                }
            }
        }
    }
}