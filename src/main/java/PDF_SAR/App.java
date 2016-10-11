/**
 * Created by jcarlett on 10/6/2016.
 */
package PDF_SAR;


import java.io.File;
import java.util.List;

public class App {


    public static void otherMain(String path, String searchPhrase) {
        TextExtract textExtract = new TextExtract();
        Utils utils = new Utils();
        RenameFile renameFile = new RenameFile();
        
        List<String> filePaths = null;
        String extractedText = null;
        List<File> trueFiles = null;


        System.out.println("Path = " + path);
        System.out.println("Search Phrase = " + searchPhrase);

        //1-3 Utils
        //4.) Cycle through file list
        //5.) Hop to TextExtract
        //6-8 TextExtract
        //9.) check if Text contains search phrase
        //10.) if so send file to Utils.addPathToTrueList()
        //11.) once cycled through all files pull Utils.getFilesThatContainPhrase()
        //12.) push that to RenameFile.setOldFile()

        utils.setDirectory(new File(path));
        filePaths = utils.pathList();

        for (String sPath : filePaths) {
            textExtract.setPdDocument(sPath);
            extractedText = textExtract.getTextFromCurrentDoc();
            if (extractedText.contains(searchPhrase)) {
                System.out.println(sPath + "  contains Search Phrase: " + searchPhrase);
                utils.addPathToTrueList(sPath);
            }
            textExtract.closeDocument();

        }


        trueFiles = utils.getFilesThatContainPhrase();

        for (File f : trueFiles) {
            renameFile.setOldFileName(f);
            String newFilePath = "C:\\Users\\jcarlett\\Desktop\\Combined\\";
            //String extension = FilenameUtils.getExtension(f.toString());
            String builtFilePath = newFilePath + f.getName();
            System.out.println(builtFilePath);
            renameFile.setNewFileName(new File(builtFilePath));
            Boolean isRenamed = renameFile.renameFile();
            if (isRenamed) {
                System.out.println("Successfully renamed " + f.toString() + " to " + builtFilePath);
            } else {
                System.out.println("There was a problem renaming " + f.toString());
            }

        }


    }
}
