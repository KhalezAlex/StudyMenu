package org.klozevitz.enitites.menu;

import lombok.*;
import org.klozevitz.enitites.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_question_t")
public class TestQuestion extends BaseEntity {
    private String variant1;
    private String variant2;
    private String variant3;
    private String variant4;

    public boolean isRight(String answer) {
        return answer.equals(variant1);
    }

    public String test(String itemName) {
        StringBuilder sb = new StringBuilder(itemName);

        List<String> variants = new ArrayList<>(List.of(variant1, variant2, variant3, variant4));
        Collections.shuffle(variants);

        while (!variants.isEmpty()) {
            sb.append(variants.remove(0)).append("\n");
        }

        return sb.toString();
    }


}
