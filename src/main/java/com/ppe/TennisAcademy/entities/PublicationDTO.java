package com.ppe.TennisAcademy.entities;

import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import lombok.Data;

@Data
public class PublicationDTO {

    private Long idPublication;

    private String text;

    private LocalDateTime localDateheurePublication;

    private UserDTO user;




    public static PublicationDTO mapToPublicationDTO(Publication publication) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(publication, PublicationDTO.class);
    }
}
