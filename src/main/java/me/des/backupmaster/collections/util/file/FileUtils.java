package me.des.backupmaster.collections.util.file;

import me.des.backupmaster.BackupMaster;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class FileUtils {

    public static void cloneFolder(String pathFrom, String pathTo){
        deleteFolder(pathTo);
        Path sourceFolder = Paths.get(pathFrom);
        BackupMaster.getInstance().getLogger().log(Level.INFO, "Cloning world folder: "+sourceFolder.getFileName());
        Path targetFolder = Paths.get(pathTo);

        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            try {
                cloneFolderWithRetries(sourceFolder, targetFolder, MAX_RETRIES);
                return true; // Successfully cloned
            } catch (IOException e) {
                e.printStackTrace();
                return false; // Error occurred during cloning
            }
        });
        future.join();
    }

    public static void deleteFolder(String folderSource){
        Path folderPath = Paths.get(folderSource);

        CompletableFuture<Void> deletionFuture = deleteFolderContentsAsync(folderPath);

        deletionFuture.thenRun(() -> System.out.println("All files deleted asynchronously."));

        // Optionally, you can perform other tasks here without waiting for the deletion to complete

        // Ensure that the program doesn't exit before the asynchronous deletion is done
        deletionFuture.join();
    }


    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 500; // 0.5 second


    private static void cloneFolderWithRetries(Path sourceFolder, Path targetFolder, int remainingRetries) throws IOException {
        try {
            Files.walkFileTree(sourceFolder, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = targetFolder.resolve(sourceFolder.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = targetFolder.resolve(sourceFolder.relativize(file));
                    File f = new File(file.toString());
                    if(f.getName().equals("session.lock")){
                        return FileVisitResult.CONTINUE;
                    }
                    boolean success = false;
                    int retries = 0;
                    while (!success && retries < remainingRetries) {
                        try {
                            Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                            success = true;
                        } catch (IOException e) {
                            retries++;
                            System.out.println("File locked, waiting and retrying...");
                            try {
                                TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }
                    if (!success) {
                        BackupMaster.getInstance().getLogger().log(Level.SEVERE, "Failed to copy file after retries: " + file);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            if (remainingRetries > 0) {
                cloneFolderWithRetries(sourceFolder, targetFolder, remainingRetries - 1);
            } else {
                throw e;
            }
        }
    }

    private static CompletableFuture<Void> deleteFolderContentsAsync(Path folderPath) {
        return CompletableFuture.runAsync(() -> {
            try {
                Files.walkFileTree(folderPath, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
