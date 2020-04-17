package com.pepcus.apps.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyValuePair {
    private String key;
    private String value;
}
