package utilities;

import java.io.IOException;

/**
 * @author Eduardo_
 */
public class AllFilesValidation {
    public static void main(String[] args) throws IOException {
        String folderName = "C:\\Users\\Eduardo_\\Documents\\Wagic\\Res\\sets\\primitives";
        
        CardValidation.main(new String[]{folderName});
        FirstBracketsChecker.main(new String[]{folderName});
        Keywords.main(new String[]{folderName});
        ParenthesesChecker.main(new String[]{folderName});
        SyntaxCheck.main(new String[]{folderName});
    }
}
