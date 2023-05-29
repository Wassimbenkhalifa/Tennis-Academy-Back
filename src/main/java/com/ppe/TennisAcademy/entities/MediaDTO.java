package com.ppe.TennisAcademy.entities;

import java.io.Serializable;

import org.modelmapper.ModelMapper;


import lombok.Data;
import lombok.ToString;

@ToString @Data
public class MediaDTO implements Serializable {

    private Long id;

    private String fileName;

    private byte[] file;

    private String mediaURL;

    public static MediaDTO mapToMediaDTO(Media media) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(media, MediaDTO.class));
    }

}
