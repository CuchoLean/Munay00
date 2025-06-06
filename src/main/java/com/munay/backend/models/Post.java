package com.munay.backend.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "posts")
public class Post {
    @Id
    private String id;
    private String idUsuario;
    private String nombreUsuario;
    @NotNull(message = "La imagen es obligatoria")
    @Size(min = 1, message = "La imagen no puede estar vacía")
    private byte[] imagen;
    @NotBlank(message = "El texto es obligatorio.")
    private String texto;
}
