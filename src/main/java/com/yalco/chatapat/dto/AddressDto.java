package com.yalco.chatapat.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String country;
    private String city;
    private String street;
    private String postCode;
}
