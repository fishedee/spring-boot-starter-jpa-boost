package com.fishedee.jpa_boost.sample;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Phone{
        @NotBlank
        private String name;

        @NotBlank
        private String phone;
    }

    @NotBlank
    private String name;

    @Range(min=0,max=1)
    private byte isCustomer;

    @Range(min=0,max=1)
    private byte isSuppiler;

    @NotNull
    @Min(1)
    private Long contactCategoryId;

    private String contactCategoryPath = "";

    private String contactCategoryName = "";

    @NotNull
    private String remark = "";

    @Valid
    private List<Phone> phones = new ArrayList<>();
}
