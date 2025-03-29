package org.klozevitz.enitites.menu;

import lombok.*;
import org.klozevitz.enitites.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    @OneToOne
    private Item item;

    public TestQuestion(Item item) {
        this.item = item;
        Map<String, Double> ingredients = item
                .getIngredients()
                .stream()
                .collect(Collectors
                        .toMap(
                                Ingredient::getName,
                                Ingredient::getWeight
                        )
                );
        this.variant1 = v1(ingredients);
        this.variant2 = wrongVariant(ingredients);
        this.variant3 = wrongVariant(ingredients);
        this.variant4 = wrongVariant(ingredients);

    }

    private String v1(Map<String, Double> ingredients) {
        StringBuilder sb = new StringBuilder();
        sb
                .append("<b>")
                .append(item.getName())
                .append("</b>\n");
        ingredients.keySet().forEach(key -> sb
                .append(key)
                .append(" - ")
                .append(ingredients.get(key).intValue())
                .append("\n"));
        sb.append("\n");

        return sb.toString();
    }

    private String wrongVariant(Map<String, Double> ingredients) {
        StringBuilder sb = new StringBuilder();
        AtomicInteger wrongAnswerIndex = new AtomicInteger(0);
        Map<String, Double> map = new HashMap<>();

        sb
                .append("<b>")
                .append(item.getName())
                .append("</b>\n");

        Set<String> ingredientKeys = ingredients.keySet();

        ingredientKeys.forEach(key -> map.put(key, ingredients.get(key)));

        ingredientKeys.forEach(key -> {
            sb
                    .append(key)
                    .append(" - ");
            double wrongWeight;
            if (wrongAnswerIndex.get() == 0) {
                if (map.size() == 1) {
                    wrongWeight = wrongWeight(map.get(key));

                    sb
                            .append((int) wrongWeight)
                            .append("\n");
                } else {
                    var random = Math.random();

                    if (random > 0.5) {
                        double multiplier = 0.7 + (1.3 - 0.7) * new Random().nextDouble();
                        wrongWeight = map.get(key) * multiplier;
                    } else {
                        wrongWeight = map.get(key);
                    }
                    wrongWeight = (int) (Math.floor(wrongWeight / 5.0) * 5);

                    sb
                            .append((int) wrongWeight)
                            .append("\n");
                    wrongAnswerIndex.getAndIncrement();
                    map.remove(key);
                }
            } else {
                var random = Math.random();

                if (random > 0.5) {
                    double multiplier = 0.7 + (1.3 - 0.7) * new Random().nextDouble();
                    wrongWeight = map.get(key) * multiplier;
                } else {
                    wrongWeight = map.get(key);
                }
                wrongWeight = (int) (Math.floor(wrongWeight / 5.0) * 5);
                sb
                        .append((int) wrongWeight)
                        .append("\n");
                wrongAnswerIndex.getAndIncrement();
                map.remove(key);
            }
        });

        return sb.toString();
    }

    private double wrongWeight(double weight) {
        double wrongWeight;
        if (weight < 10) {
            wrongWeight = weight * (1 + Math.random());
        } else {
            if (Math.random() > 0.5) {
                wrongWeight = weight * (1 + Math.random());
            } else {
                wrongWeight = weight * (1 - Math.random());
            }
        }

        wrongWeight = (int) wrongWeight;

        if (weight != wrongWeight) {
            return wrongWeight;
        } else {
            return wrongWeight(weight);
        }
    }

    public boolean isDerivative(String str1, String str2) {
        if (str1.length() != str2.length()) {
            return false;
        }

        Map<Character, Integer> charCount = new HashMap<>();

        for (char c : str1.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }

        for (char c : str2.toCharArray()) {
            if (!charCount.containsKey(c)) {
                return false; // Найден новый символ
            }
            charCount.put(c, charCount.get(c) - 1);
            if (charCount.get(c) < 0) {
                return false; // Какого-то символа слишком много
            }
        }

        return true;
    }
}
