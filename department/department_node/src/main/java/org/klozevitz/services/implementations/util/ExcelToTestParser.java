package org.klozevitz.services.implementations.util;

import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.klozevitz.enitites.menu.Ingredient;
import org.klozevitz.enitites.menu.Item;
import org.klozevitz.enitites.menu.TestQuestion;

import java.util.*;

@NoArgsConstructor
public class ExcelToTestParser {
    public Set<Item> parseMenu(Workbook workbook) {
        try {
            var sheet = workbook.getSheetAt(0);
            final Iterator<Row> wbIterator = sheet.iterator();
            final Set<Item> menu = new HashSet<>();

            wbIterator.next();
            while (wbIterator.hasNext()) {
                var currentItem = parseItem(wbIterator);
                menu.add(currentItem);
            }

            return menu;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private Item parseItem(Iterator<Row> wbIterator) {
        var currentRow = wbIterator.next();
        var currentItem = itemFromRow(currentRow);
        while ((currentRow = wbIterator.next()) != null && !endOfItemBlock(currentRow)) {
            var ingredient = parseIngredient(currentRow);
            ingredient.setItem(currentItem);
            currentItem.getIngredients().add(ingredient);
        }
        return currentItem;
    }

    private boolean endOfItemBlock(Row row) {
        var cellType = row.getCell(0).getCellType();

        return cellType == 1 && row.getCell(0).getStringCellValue().equals("-");
    }

    private Ingredient parseIngredient(Row currentRow) {
        return Ingredient.builder()
                .name(currentRow.getCell(1).getStringCellValue())
                .weight(currentRow.getCell(2).getNumericCellValue())
                .units(currentRow.getCell(3).getStringCellValue())
                .build();
    }

    private Item itemFromRow(Row currentRow) {
        return Item.builder()
                .name(currentRow.getCell(0).getStringCellValue())
                .weight(currentRow.getCell(2).getNumericCellValue())
                .units(currentRow.getCell(3).getStringCellValue())
                .ingredients(new HashSet<>())
                .question(new TestQuestion())
                .build();
    }
}
