package com.automatedlogic;

import com.automatedlogic.service.FileEditor;

public class MainApp {
/*
* this is the main Method of the project
* the pathNames variable will store the locations/dirs of
* properties file into String Array
* and for each location/dir :
* --> updateAllLicense() method will be called to update the expiration date
*     and move the new update license file to main root of the project
* --> runResignFileCmd() method will be called to run gradle signFile command
*     from batch file and generate new hash code for each license and finally move all
*     the properties file to temporary releaseLicenses directory under main root project
* --> thread.sleep() method will put the thread to sleep for certain time until batch file
*     execute completed (give 50s for each dir)
* --> savedFileBacktoDestination() method will be called to copy the new update of license
*     back to the original directory
**/
    public static void main(String[] args) throws Exception {

        FileEditor fileEditor = new FileEditor();

        /*--------LocalDir-----------LOCALDir---------WindowsOS--------*/
//        String[] pathNames = fileEditor.getFilePaths();
//
//            for(String pathName : pathNames) {
//                fileEditor.runResignFileCmd();
//
//                fileEditor.runResignFileSh();
//                Thread.sleep(20 * 1000);
//
//                fileEditor.savedFilesBackToWindowsDir(pathName);
//            }

        /*--------LocalDir-----------LOCALDir--------MacOS--------*/
        String[] macPathNames = fileEditor.getFilePathsMacOs();

            for(String pathName : macPathNames) {
                fileEditor.updateAllLicenses(pathName);
                fileEditor.runResignFileSh();

                Thread.sleep(10 * 1000);

                fileEditor.savedFilesBackToMacDir(pathName);
            }
        }
    }












