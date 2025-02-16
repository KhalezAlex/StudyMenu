package org.klozevitz.enitites.menu;

import javax.persistence.*;
import lombok.*;
import org.klozevitz.enitites.BaseEntity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient_t")
public class Ingredient extends BaseEntity {
    private String name;
    private Double weight;
    private String units;
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_id")
    private Item item;
}
