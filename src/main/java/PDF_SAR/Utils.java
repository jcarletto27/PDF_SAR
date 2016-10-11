package PDF_SAR;

/**
 * Created by jcarlett on 10/6/2016.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    //Gather all files in a directory, and put the paths in a list


    //1.) set a directory
    //2.) get a file list
    //3.) return the list

    File directory = null;
    List<File> files = null;
    List<File> filesThatContainPhrase = new ArrayList<File>();


    public void setDirectory(File directory) {
        this.directory = directory;
        addFilesToList(directory);
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public File getDirectory() {
        return directory;
    }

    public List<File> getFiles() {
        return files;
    }

    public void addFilesToList(File file) {
        if (file.isDirectory()) {
            setFiles(Arrays.asList(file.listFiles()));
        } else {
            System.out.println("This file is not a directory.");
        }

    }

    public List<String> pathList() {

        List<String> paths = null;
        if (this.files != null) {
            paths = new ArrayList<String>();
            for (File f : this.files) {
                paths.add(f.getAbsolutePath());
                //System.out.println(f.getAbsolutePath());
            }
        }
        return paths;
    }

    public void addPathToTrueList(String path) {
        filesThatContainPhrase.add(new File(path));
    }

    public List<File> getFilesThatContainPhrase() {
        return filesThatContainPhrase;
    }



}
