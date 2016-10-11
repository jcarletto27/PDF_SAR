package PDF_SAR;

import java.io.File;

/**
 * Created by jcarlett on 10/6/2016.
 */
public class RenameFile {
    File oldFileName;
    File newFileName;



        public RenameFile(){

        }

        public void setOldFileName(File file) {
            this.oldFileName = file;
        }

        public void setNewFileName(File file) {
            this.newFileName = file;
        }

        private boolean isFileExist(File file) {
            if (file.exists()) {
                return true;
            } else {
                return false;
            }
        }

        public boolean renameFile() {
            boolean success = false;
            if (isFileExist(this.oldFileName) && !isFileExist(this.newFileName)) {
                success = this.oldFileName.renameTo(this.newFileName);
            }
            return success;
        }

}
