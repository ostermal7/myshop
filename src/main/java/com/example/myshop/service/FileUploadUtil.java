package com.example.myshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {
	
	public final static Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static void saveFile(String uploadDir, String filename, MultipartFile multipartFile) throws IOException {
        Path uploadPath= Paths.get(uploadDir);

        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream=multipartFile.getInputStream()){
            Path filePath= uploadPath.resolve(filename);
            Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("Could not save file: "+filename, e);
        }
    }
    public static void cleanDir(String dir){
        Path dirPath=Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file ->{
                if(!Files.isDirectory(file)){
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                    	LOGGER.error("Could not delete the file "+ file);
                       // System.out.println("Could not delete the file "+ file);
                    }
                }
            });
        }catch (IOException e){
        	LOGGER.error("Could not list directory "+dirPath);
           // System.out.println("Could not list directory "+dirPath);
        }
    }
}
