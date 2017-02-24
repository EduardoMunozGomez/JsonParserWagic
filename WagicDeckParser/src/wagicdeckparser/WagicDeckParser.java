/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wagicdeckparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 *
 * @author Eduardo
 */
public class WagicDeckParser {

    /**
     * @param args the command line arguments
     */
    private static final String filePath = "C:\\Users\\Eduardo\\Downloads\\Deck.txt";

    public static void main(String[] args) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            String dst;

            dst = line.substring(0, 1);

            line = line.substring(2);

            line = line += " *" + dst;

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();

                if (line != null) {
                    dst = line.substring(0, 1);
                    line = line.substring(2);
                    line = line += " *" + dst;
                }

            }

            String everything = sb.toString();
            System.out.println(everything);

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("C:\\Users\\Eduardo\\Downloads\\Deck3.txt"), "utf-8"))) {
                writer.write(everything);
            }
        }
    }
}
