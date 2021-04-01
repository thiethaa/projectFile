package com.automatedlogic.service;

import com.automatedlogic.helper.CustomProperties;
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileEditor {

    private final String userDirectory = System.getProperty("user.dir");
    private final File destination = new File(userDirectory);
    private final String absolutePropPathMac = "src/main/resources/filePath.properties";
    private final String absolutePropPath = userDirectory + "\\src\\main\\resources\\filePath.properties";

    /**
     * @ getFilePaths() Method will get the File's location/path
     * from @absolutePropPath properties file and store it into Array type of String
     **/
    public String[] getFilePaths() throws Exception{
        String[] pathNames = null;

        try {
            FileInputStream propPath = new FileInputStream(absolutePropPath);
            Properties propertiesPath = new Properties();
            propertiesPath.load(propPath);
            String paths = propertiesPath.getProperty("file.path");
            pathNames = paths.split(",");
        }catch(Exception e){
            e.printStackTrace();
        }
        return pathNames;
    }
    public String[] getFilePathsMacOs() throws Exception{
        String[] pathNames = null;

        try {
            FileInputStream propPath = new FileInputStream(absolutePropPathMac);
            Properties propertiesPath = new Properties();
            propertiesPath.load(propPath);
            String paths = propertiesPath.getProperty("file.path");
            pathNames = paths.split(",");
        }catch(Exception e){
            e.printStackTrace();
        }
        return pathNames;
    }

    /*
    * @ updateAllLicenses(String pathName) method has one argument type of String
    * will open all the properties file inside @ pathName (original directory) one by one,
    * parse and update the license.expiration date of each properties file
    **/
    public void updateAllLicenses(String pathName) throws Exception {
        String sourcePath = pathName;

        try {
                try (Stream<Path> stream = Files.walk(Paths.get(sourcePath))) {
                    Properties properties = new CustomProperties();
                    stream.filter(s -> !s.toFile().isDirectory() && s.toFile().getName().endsWith(".properties"))
                            .collect(Collectors.toList())
                            .forEach(propPath -> {
                                try (FileInputStream fis = new FileInputStream(propPath.toFile())) {
                                    properties.load(fis);
                                    properties.setProperty("license.expiration", generateLicenseExpirationDate());
                                    properties.store(new FileOutputStream(propPath.toFile()), null);//update file
                                    movedFilesToMainRoot(sourcePath);//move to main root
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * @ generateLicenseExpirationDate() method is to create the date format
    * of MM/dd/yyyy and generate new license.expiration date by extend it
    * one year
    **/
    private String generateLicenseExpirationDate() throws ParseException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.now().plusYears(5);
        return dateTimeFormatter.format(localDate);
    }

    /*
    * @ movedFilesToMainRoot (String sourcePath) method has one argument type of String
    * this method will copy all the properties file inside the @ sourcePath (original directory)
    * to the main root of the project
    **/
    public void movedFilesToMainRoot(String sourcePath) {
        File[] files = new File(sourcePath).listFiles();
        if (files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (file.getName().endsWith(".properties")) {
                        try {
                            FileUtils.copyFileToDirectory(file, destination);
                        } catch (
                                IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /* @ runResignFileCmd() method is to run resignFile.bat file
    * from the main root project to call gradle resign command
    * to generate the licenses with new hash code and finally move
    * all the properties file into temp directory releaseLicenses
    * under the main root project
    *
    * NOTE:
    * the resign command can only be called from main root of the project
    * and the license properties file should be located in the main root project as well
    **/
    public void runResignFileCmd() throws Exception{
        try {
            String[] command = {"cmd.exe", "/C", "Start", userDirectory+"\\resignFile.bat"};
            Runtime.getRuntime().exec(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void runResignFileSh() throws Exception{
        try {
            Runtime.getRuntime().exec("sh ./resignFile.sh");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * @ savedFilesBackToDestination(String pathName) method has one argument type of String
    * this method will save the new license file back to the @pathName (original directory)
    **/
    public void savedFilesBackToWindowsDir(String pathName) throws Exception {
        String destination = pathName;

        try {
                String sourcePath = userDirectory + "\\releaseLicenses\\";
                File[] files = new File(sourcePath).listFiles();
                if (files.length != 0) { // check if file is not empty
                    for (File file : files) {
                        if (file.isFile()) { //check if file is a file
                            if (file.getName().endsWith(".properties")) {
                                String destinationPathName = destination + "\\" + file.getName();
                                File file2 = new File(destinationPathName);
                                if (file2.exists()) {
                                    try {
                                        FileUtils.copyFile(file, file2);
                                    } catch (
                                            IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savedFilesBackToMacDir(String pathName) throws Exception {
        String destination = pathName;

        try {
            String sourcePath = userDirectory + "/releaseLicenses/";
            File[] files = new File(sourcePath).listFiles();
            if (files.length != 0) { // check if file is not empty
                for (File file : files) {
                    if (file.isFile()) { //check if file is a file
                        if (file.getName().endsWith(".properties")) {
                            String destinationPathName = destination + "/" + file.getName();
                            File file2 = new File(destinationPathName);
                            if (file2.exists()) {
                                try {
                                    FileUtils.copyFile(file, file2);
                                } catch (
                                        IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

