package org.klozevitz.entites;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
