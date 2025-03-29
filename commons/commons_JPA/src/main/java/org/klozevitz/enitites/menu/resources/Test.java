package org.klozevitz.enitites.menu.resources;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.klozevitz.enitites.BaseEntity;
import org.klozevitz.enitites.menu.Item;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "test_t")
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Test extends BaseEntity {
    private Integer correctAnswersNumber;
    private int currentItemId;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private ArrayList<String> menu;

    public Test(Set<Item> menu) {
        this.menu = new ArrayList<>();
        this.currentItemId = 0;

        final AtomicInteger index = new AtomicInteger(1);

        menu.forEach(
                item ->
                        this.menu.add(
                                itemStringFromBaseEntity(item, index.getAndIncrement())
                        )
        );
    }

    public String next() {
        if (currentItemId < menu.size()) {
            currentItemId++;
        }
        return menu.get(currentItemId);
    }

    public String previous() {
        if (currentItemId > 0) {
            currentItemId--;
        }
        return menu.get(currentItemId);
    }

    private String itemStringFromBaseEntity(Item item, int index) {
        AtomicInteger atomicIndex = new AtomicInteger(1);
        StringBuilder sb = new StringBuilder();

        sb
                .append(index)
                .append(". ")
                .append(item.getName())
                .append("\n");

        item.getIngredients().forEach(
                ingredient -> sb
                        .append("\n")
                        .append(atomicIndex.getAndIncrement())
                        .append(". ")
                        .append(ingredient.getName())
                        .append(" - ")
                        .append(ingredient.getWeight())
                        .append(ingredient.getUnits()));

        return sb.toString();
    }
}
