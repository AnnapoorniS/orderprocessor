package com.retail.orderprocessor.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class Item {
    private int quantity;
    private int productId;
    private String productName;
    private String productPrice;
}
