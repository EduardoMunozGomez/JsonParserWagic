/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

/**
 *
 * @author alfieriv
 */

import java.io.*;

public class PrimitiveParser {
    
    public static void main(String[] argv) throws IOException {

        argv = new String[] { "C:\\Users\\Eduardo_\\Documents\\WagicWindows-0231", "C:\\Users\\Eduardo_\\Documents\\WagicWindows-0231\\Res\\sets\\primitives\\output.txt" };
        if (argv.length != 2) {
            System.err.println("Usage: java -jar PrimitiveParser.jar WagicPath OutputFilePath");
            System.exit(-1);
        }
        System.out.println("You selected " + argv[0] + "as Wagic Path and " + argv[1] + "as Output File Path");
        try {        
            File cards = new File(argv[1]);
            FileWriter fw = new FileWriter(cards);
            File primitivesFolder = new File(argv[0] + File.separator + "User" + File.separator + "sets" + File.separator + "primitives" + File.separator);
            File [] listOfPrimitives = primitivesFolder.listFiles();
            for(int i = 0; i < listOfPrimitives.length; i++) {    
                System.out.println("\r\n" + "Now reading primitive file: " + listOfPrimitives[i]);
                String lines = PrimitiveDatabase.readLineByLineJava8(listOfPrimitives[i].getAbsolutePath());
                while(lines.contains("[card]")) {
                    String findStr = "[card]";
                    int lastIndex = lines.indexOf(findStr);
                    String text = "";
                    String type = "";
                    String subtype = "";
                    String name = null;
                    String name2 = "";
                    int a = lines.indexOf("name=",lastIndex);
                    if (a > 0){
                        if(lines.substring(a, lines.indexOf("\n", a)).split("=").length > 1)
                            name = lines.substring(a, lines.indexOf("\n", a)).split("=")[1];
                    }
                    if (name != null)
                        fw.append("Name=" + name + "\n");
                    int b = lines.indexOf("text=", lastIndex);
                    if (b > 0){
                        if(lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=").length > 1)
                            text = lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=")[1];
                    }
                    fw.append("text=" + text + "\n");
                    int d = lines.indexOf("type=",lastIndex);
                    if(d > 0){
                        if(lines.substring(d, lines.indexOf("\n",d)).split("=").length > 1)
                            type = lines.substring(d, lines.indexOf("\n",d)).split("=")[1].toLowerCase();
                    }
                    fw.append("Type=" + type + "\n");
                    int e = lines.indexOf("subtype=",lastIndex);
                    if(e > 0){
                        if(lines.substring(e, lines.indexOf("\n",e)).split("=").length > 1)
                            subtype = lines.substring(e, lines.indexOf("\n",e)).split("=")[1].toLowerCase();
                    }
                    fw.append("subtype=" + subtype + "\n");
                    int c = lines.indexOf("[/card]",lastIndex);
                    int f = lines.indexOf("name(",lastIndex);
                    while(f > 0 && f < c) {
                        name2 = lines.substring(f, lines.indexOf(")",f)).toLowerCase().replace("name(", "");
                        fw.append("name(=" + name2 + "\n");
                        f = lines.indexOf("name(",f+5);
                    }
                    lines = lines.substring(c + 8);
                    fw.append("\n");
                    fw.flush();
                    System.out.println("Added " + name + "card");
                }
            }
            fw.close();
            System.out.println("\r\n" + "All primitive files have been read, the output file is ready!");
        } catch (Exception e) {
            System.err.println("Error while reading primitive files: " + e.getMessage());
            System.exit(-1);
        }
    }
}
