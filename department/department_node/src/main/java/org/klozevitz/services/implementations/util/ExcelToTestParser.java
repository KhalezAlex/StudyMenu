package org.klozevitz.services.implementations.util;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.klozevitz.enitites.menu.Ingredient;
import org.klozevitz.enitites.menu.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
public class ExcelToTestParser {
    private final String firstString = "\"Название блюда/\n" +
            "номер ингридиента\"\n";
    private final Workbook wb;

    public List<Item> parseMenu() {
        try {
            var sheet = wb.getSheetAt(0);
            Iterator<Row> wbIterator = sheet.iterator();
            List<Item> menu = new ArrayList<>();
            Row currentRow;

            wbIterator.next();

            while (wbIterator.hasNext()) {
                currentRow = wbIterator.next();

                var currentItem = itemFromRow(currentRow);

                while ((currentRow = wbIterator.next()) != null && currentRow.getCell(0).getNumericCellValue() != 0) {
                    var ingredient = ingredientFromRow(currentRow);
                    currentItem.getIngredients().add(ingredient);
                }

                menu.add(currentItem);
            }

            return menu;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private Ingredient ingredientFromRow(Row currentRow) {
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
                .build();
    }
}
