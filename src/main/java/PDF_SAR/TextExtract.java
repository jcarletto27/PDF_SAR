package PDF_SAR;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.IOException;

/**
 * Created by jcarlett on 10/6/2016.
 */
public class TextExtract {


    //1-3 Utils
    //4-5 App
    //6.) load in file as PDDocument
    //7.) Strip text from document
    //8.) return to App

    PDFTextStripper textStripper = null;
    PDDocument pdDocument = null;

    public PDDocument getPdDocument() {
        return pdDocument;
    }

    public void setPdDocument(String pdDocument) {
        try {
            this.pdDocument = PDDocument.load(pdDocument);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TextExtract() {
        setTextStripper();
    }

    public String getTextFromCurrentDoc() {
        String text = null;
        try {
            text = textStripper.getText(pdDocument);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    public void closeDocument(){
        try {
            pdDocument.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setTextStripper() {

        try {
            textStripper = new PDFTextStripper();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
