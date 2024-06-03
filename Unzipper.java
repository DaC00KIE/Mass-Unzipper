import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Unzipper {

    public static String currentDirectory = System.getProperty("user.dir").replace("\\", "/");

    // some variables to start off
    private static boolean checkAll;
    private static String targetDirectory = currentDirectory;
    private static final List<String> studentsToCheck = new ArrayList<>();

    // will make a list of the problematic folders that need to be manually
    // unzipped
    private static ArrayList<String> problematicFolders = new ArrayList<>();

    public static void main(String[] args) {
        // initializes checkAll, targetDirectory, and studentsToCheck through config.txt
        parseConfigFile();
        System.out.println("Current Directory: " + targetDirectory);

        // Get all zip files in the directory
        File directory = new File(targetDirectory);
        File[] zipFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".zip")
                && name.toLowerCase().matches(".*lab.*"));

        // Process each zip file
        if (zipFiles != null) {
            for (File zipFile : zipFiles) {
                unzipAndFilter(zipFile, false);
            }
        }

        File updatedDirectory = new File(targetDirectory);
        File[] allFiles = updatedDirectory.listFiles();

        System.out.println("");

        for (File file : allFiles) {
            if (containsUnzipZipFile(file)) {
                problematicFolders.add(file.getName());

                File renamedFile = new File(file.getParentFile(), "[UNZIP] " +
                        file.getName());
                if (!file.renameTo(renamedFile)) {
                    System.err.println("Failed to rename folder: " + file.getName());
                }
            }
        }

        System.out.println("\nProblematic Folders this program failed to unzip:");
        for (String folderName : problematicFolders) {
            System.out.println(folderName);
        }
        System.out.println("\nYou should manually unzip them/ check them just in case");
    }

    // Read the value of checkAll from config.txt
    private static void parseConfigFile() {
        String configPath = currentDirectory + "/config.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Target Folder Path")) {
                    // Parse the value of directoryPath
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        targetDirectory = parts[1].trim().replace("\\", "/");
                    }
                } else if (line.startsWith("Check all students")) {
                    // Parse the value of checkAll
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        checkAll = Boolean.parseBoolean(parts[1].trim());
                    }
                } else if (line.startsWith("Students to Check")) {
                    // Parse the list of studentsToCheck
                    while ((line = reader.readLine()) != null && !line.isEmpty()) {
                        studentsToCheck.add(line.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Unzip the zip file and filter its contents
    private static void unzipAndFilter1(File zipFile) {
        // // Extract the zip file
        // File outputDirectory = zipFile.getParentFile();
        // byte[] buffer = new byte[1024];

        // try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
        // ZipEntry zipEntry = zis.getNextEntry();
        // while (zipEntry != null) {
        // try {
        // String entryName = zipEntry.getName();
        // // Check if the entry should be kept
        // if (shouldKeepFolder(entryName) || checkAll) {
        // File newFile = new File(outputDirectory, entryName);
        // if (zipEntry.isDirectory()) {
        // newFile.mkdirs();
        // } else {
        // new File(newFile.getParent()).mkdirs();
        // try (FileOutputStream fos = new FileOutputStream(newFile)) {
        // int length;
        // while ((length = zis.read(buffer)) > 0) {
        // fos.write(buffer, 0, length);
        // }
        // }
        // }
        // // If the entry is a zip file, recursively unzip its contents
        // if (zipEntry.isDirectory() || entryName.toLowerCase().endsWith(".zip")) {
        // unzipAndFilter(newFile);
        // }
        // }
        // } catch (IOException e) {
        // System.err.println("Error processing zip entry: " + zipEntry.getName() + ".
        // Skipping...");
        // e.printStackTrace();
        // }
        // zipEntry = zis.getNextEntry();
        // }
        // System.out.println("Unzipped and filtered: " + zipFile.getName());
        // } catch (IOException e) {
        // System.err.println("\nError unzipping and filtering file: " +
        // zipFile.getName() + "\n");
        // // e.printStackTrace();

        // // Rename the parent zip file with "ERROR" prefix
        // File renamedZipFile = new File(outputDirectory, "[UNZIP] " +
        // zipFile.getName());
        // if (!zipFile.renameTo(renamedZipFile)) {
        // System.err.println("Failed to rename zip file: " + zipFile.getName());
        // }
        // }
    }

    // Unzip the zip file and filter its contents from GPT test 2
    private static void unzipAndFilter2(File zipFile) {
        // try (ZipFile zip = new ZipFile(zipFile)) {
        // Enumeration<? extends ZipEntry> entries = zip.entries();
        // while (entries.hasMoreElements()) {
        // ZipEntry entry = entries.nextElement();
        // if (!entry.isDirectory()) {
        // String entryName = entry.getName();
        // if (shouldKeepFolder(entryName) || checkAll) {
        // File entryFile = new File(targetDirectory, entryName);
        // entryFile.getParentFile().mkdirs();
        // try (InputStream in = zip.getInputStream(entry);
        // OutputStream out = new FileOutputStream(entryFile)) {
        // byte[] buffer = new byte[1024];
        // int bytesRead;
        // while ((bytesRead = in.read(buffer)) != -1) {
        // out.write(buffer, 0, bytesRead);
        // }
        // }
        // }else{
        // continue;
        // }
        // if (entryName.toLowerCase().endsWith(".zip")) {
        // unzipAndFilter(new File(targetDirectory, entryName));
        // }
        // }
        // }
        // System.out.println("Unzipped and filtered: " + zipFile.getName());
        // } catch (IOException e) {
        // System.err.println("\nError unzipping and filtering file: " +
        // zipFile.getName() + "\n");
        // File renamedZipFile = new File(targetDirectory, "[UNZIP] " +
        // zipFile.getName());
        // if (!zipFile.renameTo(renamedZipFile)) {
        // System.err.println("Failed to rename zip file: " + zipFile.getName());
        // }
        // }
    }

    // works right
    private static void unzipAndFilter(File zipFile, boolean nestedWithin) {
        byte[] buffer = new byte[1024];

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                try {
                    String entryName = zipEntry.getName();
                    if (entryName.toLowerCase().endsWith(".zip")) {
                        System.out.println(entryName);
                    }
                    File outputDirectory = zipFile.getParentFile();

                    // Check if the entry should be kept
                    if (shouldKeepFolder(entryName) || checkAll || nestedWithin) {
                        // System.out.println("Unzipping: " + entryName);
                        File newFile = new File(outputDirectory, entryName);
                        if (zipEntry.isDirectory()) {
                            newFile.mkdirs();
                        } else {
                            new File(newFile.getParent()).mkdirs();
                            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                                int length;
                                while ((length = zis.read(buffer)) > 0) {
                                    fos.write(buffer, 0, length);
                                }
                            }
                        }
                        // If the entry is a zip file, recursively unzip its contents
                        if (entryName.toLowerCase().endsWith(".zip")) {
                            unzipAndFilter(newFile, true);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error processing zip entry: " + zipEntry.getName() + ". Skipping...");
                    e.printStackTrace();
                }
                zipEntry = zis.getNextEntry();
            }
            System.out.println("Unzipped and filtered: " + zipFile.getName());
        } catch (IOException e) {
            System.err.println("\nError unzipping and filtering file: " + zipFile.getName() + "\n");
            // e.printStackTrace();

            // Rename the parent zip file with "ERROR" prefix
            File renamedZipFile = new File(zipFile.getParentFile(), "[UNZIP] " + zipFile.getName());
            if (!zipFile.renameTo(renamedZipFile)) {
                System.err.println("Failed to rename zip file: " + zipFile.getName());
            }
        }
    }

    // Check if the folder's name contains a student ID to keep
    private static boolean shouldKeepFolder(String folderName) {
        // Check if any of the student IDs to keep is present in the folder name
        for (String id : studentsToCheck) {
            if (folderName.contains(id)) {
                return true;
            }
        }
        return false;
    }

    // Check if a directory contains a zip file with the "[UNZIP]" prefix
    private static boolean containsUnzipZipFile(File directory) {
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".zip"));
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains("[UNZIP]")) {
                    return true;
                }
            }
        }
        return false;
    }

}