package org.klozevitz.enitites.menu.resources;

import lombok.*;
import org.klozevitz.enitites.BaseEntity;
import org.klozevitz.enitites.menu.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class WorkBook extends BaseEntity {
    private List<String> menu;
    private int currentItemId;

    public WorkBook(Set<Item> menu) {
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
