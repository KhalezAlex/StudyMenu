package org.klozevitz.enitites.menu;

import javax.persistence.*;
import lombok.*;
import org.klozevitz.enitites.BaseEntity;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_t")
public class Item extends BaseEntity {
    private String name;
    private Double weight;
    private String units;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "item")
    private Set<Ingredient> ingredients;
}
