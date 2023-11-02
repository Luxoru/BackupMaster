package me.des.backupmaster.collections.util.file;

import lombok.SneakyThrows;
import me.des.backupmaster.BackupMaster;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {


    @BeforeEach
    public void init(){

    }

//    @SneakyThrows
//    @Test
//    void cloneFolder() {
//
//        FileUtils.cloneFolder("C:\\Users\\hussa\\Documents\\IMPORTANT","C:\\Users\\hussa\\Documents\\test");
//
//        File file = new File("C:\\Users\\hussa\\Documents\\test\\githubPAT.txt");
//        assertTrue(file.exists());
//    }
//    @SneakyThrows
//    @Test
//    void deleteFolder() {
//        FileUtils.cloneFolder("C:\\Users\\hussa\\Documents\\IMPORTANT","C:\\Users\\hussa\\Documents\\test");
//
//        File file = new File("C:\\Users\\hussa\\Documents\\test\\githubPAT.txt");
//        if(file.exists()){
//            FileUtils.deleteFolder("C:\\Users\\hussa\\Documents\\test");
//        }
//
//        assertFalse(new File("C:\\Users\\hussa\\Documents\\test\\githubPAT.txt").exists());
//    }

    @Test
    void listSubfolders() {
    }
}