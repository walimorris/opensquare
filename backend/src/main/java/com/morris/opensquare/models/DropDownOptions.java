package com.morris.opensquare.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("drop_down_options")
public class DropDownOptions {
    private List<String> ages;
    private List<String> organizations;
    private List<String> professions;
}
