package com.youngcha.ez.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class FileUtil {
	
	@Value("${com.jpg.path}")
	String uploadPath;

    @Value("${com.pdf.path}")
    String pdfPath;

    /*
    image를 base64 인코딩 응담.
     */
    public String getImageBase64(String fileName){
        File file = new File(uploadPath+"/"+fileName);
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> getAllImageNames(){

        File[] files = new File(uploadPath).listFiles();
        List<String> fileNames = new ArrayList<>();
        if(files == null)return fileNames;
        for (File f : files){
            fileNames.add(f.getName());
        }
        return fileNames;
    }

    public List<String> getAllPdfNames(){

        File[] files = new File(pdfPath).listFiles();
        List<String> fileNames = new ArrayList<>();
        if(files == null)return fileNames;
        for (File f : files){
            fileNames.add(f.getName());
        }
        return fileNames;
    }
}
