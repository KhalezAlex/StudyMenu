package org.klozevitz.services.implementations.util;

import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.klozevitz.enitites.menu.Category;
import org.klozevitz.enitites.menu.Ingredient;
import org.klozevitz.enitites.menu.Item;
import org.klozevitz.enitites.menu.TestQuestion;

import java.util.*;

@NoArgsConstructor
public class ExcelToTestParser {
    public Set<Category> parseFile(Workbook workbook) {
        Set<Category> menu = new HashSet<>();

        var index = 0;
        var numberOfSheets = workbook.getNumberOfSheets();
        while (index < numberOfSheets) {
            var sheet = workbook.getSheetAt(index);
            var categoryItems = parseCategoryItems(sheet);
            var categoryName = workbook.getSheetName(index);
            var category = category(categoryItems, categoryName);
            menu.add(category);
            index++;
        }

        return menu;
    }


    private Category category(Set<Item> menu, String categoryName) {
        var category = Category.builder()
                .name(categoryName)
                .menu(menu)
                .build();

        category.getMenu().forEach(item -> item.setCategory(category));

        return category;
    }


    public Set<Item> parseCategoryItems(Sheet sheet) {
        try {
            final Iterator<Row> wbIterator = sheet.iterator();
            final Set<Item> category = new HashSet<>();

            wbIterator.next();
            while (wbIterator.hasNext()) {
                var currentItem = parseItem(wbIterator);
                category.add(currentItem);
            }

            return category;
        } catch (Exception e) {
            System.out.println("ОШИБКА!!!");
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

        // вставить тест
        var currentTest = new TestQuestion(currentItem);

        currentItem.setQuestion(currentTest);

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
