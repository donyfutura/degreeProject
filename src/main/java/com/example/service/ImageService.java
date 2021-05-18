package com.example.service;

import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Random;

@Service
public class ImageService {

    private final String[] words = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p"};
    private final Random random = new Random();

    public ImageService() {
    }

    public String generatePath(byte[] bytes, MultipartFile file) throws IOException {
        String folder = "upload/";
        StringBuilder builder = new StringBuilder();
        builder.append(folder);
        for (int i = 0; i < 3; i++){
            builder.append(words[random.nextInt(words.length)])
                    .append(words[random.nextInt(words.length)])
                    .append("/");
        }
        Files.createDirectories(Paths.get(builder.toString()));
        Path path = Paths.get(builder.toString() + file.getOriginalFilename());
        Files.write(path, bytes);
        System.out.println(path.toString());
        return path.toString().replaceAll("\\\\", "/");
    }

}
