package me.des.backupmaster.collections.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileZipper extends SimpleFileVisitor<Path> {

    private static ZipOutputStream zos;

    private Path sourceDir;

    public FileZipper(Path sourceDir){
        this.sourceDir = sourceDir;
    }


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        try{
            Path targetFile = sourceDir.relativize(file);

            zos.putNextEntry(new ZipEntry(targetFile.toString()));

            byte[] bytes = Files.readAllBytes(targetFile);
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();

        }catch(IOException e){

        }

        return FileVisitResult.CONTINUE;

    }



}
