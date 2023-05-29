package com.ppe.TennisAcademy.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.modelmapper.ModelMapper;

import lombok.Data;

@Entity @Data
public class Media implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    @Override
    public Object clone() {
        try {
            return (Media) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getSerialVersionuid() {
        return serialVersionUID;
    }

    public static Media mapToMedia(MediaDTO mediaDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return (modelMapper.map(mediaDTO, Media.class));
    }


}