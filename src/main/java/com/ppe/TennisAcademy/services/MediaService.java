package com.ppe.TennisAcademy.services;

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
import com.ppe.TennisAcademy.services.ImplServices.IMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;


@Service
public class MediaService implements IMediaService {

    @Autowired
    MediaRepository mediaRepository;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    @Override
    public String setFileName(String name) {

        String extension = name != null || name != "" ? "." + Optional.of(name).filter(f -> f.contains("."))
                .map(f -> f.substring(name.lastIndexOf(".") + 1)).orElse("") : "";
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
                if (media.getFileName() != null && media.getFileName() != "" && media.getFile() != null)
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
        if (Media != null)
            return Media;
        return null;
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
                Files.delete(file);
                res = true;
            } catch (IOException e) {
                throw new RuntimeException("Could not delete the files!: " + e.getMessage());
            }
        }

        return res;

    }
}