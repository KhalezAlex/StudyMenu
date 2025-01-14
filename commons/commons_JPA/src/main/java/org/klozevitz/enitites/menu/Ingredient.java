package org.klozevitz.enitites.menu;

import javax.persistence.*;
import lombok.*;
import org.klozevitz.enitites.BaseEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient_t")
public class Ingredient extends BaseEntity {
    private String name;
    private Double weight;
    private String units;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
