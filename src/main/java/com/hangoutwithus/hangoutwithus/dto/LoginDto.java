package com.hangoutwithus.hangoutwithus.dto;

import com.sun.istack.NotNull;
import lombok.Getter;

@Getter
public class LoginDto {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
