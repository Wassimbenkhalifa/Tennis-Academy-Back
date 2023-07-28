package com.ppe.TennisAcademy.services.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.ppe.TennisAcademy.entities.Media;
import com.ppe.TennisAcademy.entities.MediaDTO;
import com.ppe.TennisAcademy.repositories.MediaRepository;
import com.ppe.TennisAcademy.services.IMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;


@Service
public class MediaService implements IMediaService {

    @Autowired
    MediaRepository mediaRepository;
    static final String alphaNumerics = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom secureRandomGenerator = new SecureRandom();

    public String randomString(int stringLength) {
        StringBuilder sb = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            sb.append(alphaNumerics.charAt(secureRandomGenerator.nextInt(alphaNumerics.length())));
        }
        return sb.toString();
    }

    @Override
    public String setFileName(String name) {
        String extension = "." + Optional.of(name)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(name.lastIndexOf(".") + 1)).orElse("");
        return this.randomString(15) + extension;
    }

    @Override
    public Set<MediaDTO> setMediaNames(Set<MediaDTO> medias) {

        if (medias == null || medias.size() == 0)
            return new HashSet<>();

        for (MediaDTO media : medias) {
            if (media.getFileName() != null && media.getFileName() != "" && media.getFile() != null)
                media.setFileName(setFileName(media.getFileName()));
            else
                medias.remove(media);
        }

        return medias;
    }

    @Override
    public long uploadList(Set<MediaDTO> medias, String path) {
        // String name = file.getOriginalFilename();
        if (medias == null || medias.size() == 0)
            return -1;
        try {
            for (MediaDTO media : medias) {
                if (media.getFileName() != null && !media.getFileName().equals("") && media.getFile() != null)
                    upload(media.getFile(), media.getFileName(), path);
            }
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public Media getById(Long id) {

        Media Media = null;
        try {
            Media = mediaRepository.findById(id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Media;
    }

    @Override
    public void deleteById(long id) {

        Media media = getById(id);
        if (getById(id) != null) {
            mediaRepository.delete(media);
        }
    }

    @Override
    public long upload(byte[] file, String filename, String path) {
        // String name = file.getOriginalFilename();
        try {
            if (file != null) {

                byte[] bytes = file;
                File dir = new File(System.getProperty("user.dir") + "/" + path);
                if (!dir.exists())
                    dir.mkdirs();
                System.out.println("path is  " + dir.getAbsolutePath());
                File serverFile = new File(dir.getAbsolutePath() + "/" + filename);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
            }

            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    public Resource load(String dir, String filename) {
        try {
            Path root = Paths.get("uploads/" + dir);
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            System.out.println(filename);

            if (resource.exists() || resource.isReadable()) {
                System.out.println(resource);
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }

    @Override
    public boolean deleteFileByUrl(String url) {
        boolean res = false;
        if (url != null) {
            Path root = Paths.get(url.substring(0, url.lastIndexOf("/") + 1));
            Path file = root.resolve(url.substring(url.lastIndexOf("/") + 1, url.length()));
            try {
                if(Files.exists(file)){
                    System.out.println("OLD FILE EXIST");
                    Files.delete(file);
                } else {
                    System.out.println("OLD FILE NOT EXISTS");

                }
                res = true;
            } catch (IOException e) {
                throw new RuntimeException("Could not delete the files!: " + e.getMessage());
            }
        }

        return res;
    }
}