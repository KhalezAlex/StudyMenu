package org.klozevitz.services.implementations.updateProcessors.doc.byView;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.Package;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.Department;
import org.klozevitz.enitites.menu.Category;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.repositories.menu.CategoryRepo;
import org.klozevitz.services.CategoryService;
import org.klozevitz.services.implementations.updateProcessors.viewResolvers.ResourceUploadResultViewResolver;
import org.klozevitz.services.implementations.util.ExcelToTestParser;
import org.klozevitz.services.implementations.util.TestGenerator;
import org.klozevitz.services.interfaces.updateProcessors.BasicUpdateProcessor;
import org.klozevitz.services.interfaces.updateProcessors.UpdateProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

import static org.klozevitz.enitites.appUsers.enums.states.DepartmentState.WAIT_FOR_DOCUMENT_STATE;

@Log4j
@RequiredArgsConstructor
public class ResourceRequestViewDUP extends BasicUpdateProcessor {
    private final String RESOURCE_UPLOAD_RESULT_VIEW_SUCCESS_MESSAGE = "Вы загрузили excel-документ с категорией (-ями) меню\n" +
            "Просмотреть информацию и работать с ним Вы можете в разделе \"УПРАВЛЕНИЕ МАТЕРИАЛАМИ\"";
    private final String RESOURCE_UPLOAD_RESULT_VIEW_DOCUMENT_NOT_PARSED_MESSAGE = "Вы Загрузили документ, который " +
            "невозможно обработать. Пожалуйста, попробуйте снова!";
    private final String RESOURCE_UPLOAD_RESULT_VIEW_DOCUMENT_NOT_UPLOADED_MESSAGE = "Бот не смог загрузить файл " +
            "из-за технической ошибки. Пожалуйста, попробуйте снова.";

    @Value("${fileService.service.file_info.url}")
    private String tgFileInfoUrl;
    @Value("${fileService.service.file_storage.url}")
    private String tgFileStorageUrl;
    @Value("${fileService.token}")
    private String token;
    private final AppUserRepo appUserRepo;
    private final UpdateProcessor previousViewUpdateProcessor;
    private final ResourceUploadResultViewResolver resourceUploadResultViewResolver;
    private final CategoryService categoryService;
    private final ExcelToTestParser parser;
    private final TestGenerator testGenerator;

    @Override
    public ArrayList<SendMessage> processUpdate(Update update) {
        var telegramUserId = telegramUserId(update);
        var optionalCurrentAppUser = appUserRepo.findByTelegramUserId(telegramUserId);

        var state = optionalCurrentAppUser.get().getDepartment().getState();

        if (!state.equals(WAIT_FOR_DOCUMENT_STATE)) {
            return previousViewUpdateProcessor.processUpdate(update);
        }

        return processDocumentMessage(update, optionalCurrentAppUser.get());
    }

    private ArrayList<SendMessage> processDocumentMessage(Update update, AppUser currentAppUser) {
        var fileId = fileId(update);
        ResponseEntity<String> response = filePath(fileId);
        String message;

        if (response.getStatusCode() == HttpStatus.OK) {
            ByteArrayInputStream bin = fileAsByteArrayInputStream(response);
            try {
                Workbook workbook = new XSSFWorkbook(
                        Package.open(bin)
                );
                Set<Category> menu = parser.parseFile(workbook);


                var persistentDepartment = currentAppUser.getDepartment();
                long departmentId = persistentDepartment.getId();

                setLinks(persistentDepartment, menu);

                removeExisting(menu, departmentId);

                categoryService.saveAll(menu);
                message = RESOURCE_UPLOAD_RESULT_VIEW_SUCCESS_MESSAGE;
            } catch (IOException | InvalidFormatException e) {
                message = RESOURCE_UPLOAD_RESULT_VIEW_DOCUMENT_NOT_PARSED_MESSAGE;
            }
        } else {
            message = RESOURCE_UPLOAD_RESULT_VIEW_DOCUMENT_NOT_UPLOADED_MESSAGE;
        }

        final String finalMessage = message;
        update.getMessage().setText(finalMessage);

        // TODO: в отдельном потоке запустить тест-генератор
        return resourceUploadResultViewResolver.processUpdate(update);
    }

    private void setLinks(Department persistentDepartment, Set<Category> menu) {
        menu.forEach(category -> category.setDepartment(persistentDepartment));
        // TODO  ПРОБЛЕМА ТУТ _ НЕ ВСТАВЛЯЕТ!!!
        persistentDepartment.getMenu().addAll(menu);
    }

    private String fileId(Update update) {
        return update
                .getMessage()
                .getDocument()
                .getFileId();
    }

    private ByteArrayInputStream fileAsByteArrayInputStream(ResponseEntity<String> response) {
        assert response.getBody() != null;
        JSONObject jsonObject = new JSONObject(response.getBody());
        String filePath = String.valueOf(
                jsonObject
                        .getJSONObject("result")
                        .getString("file_path")
        );
        byte[] fileAsByteArray = downloadFile(filePath);
        return new ByteArrayInputStream(fileAsByteArray);
    }

    private byte[] downloadFile(String filePath) {
        var fileUrl = tgFileStorageUrl
                .replace("{token}", token)
                .replace("{filePath}", filePath);
        URL urlObject;
        try {
            urlObject = new URL(fileUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        try (InputStream inputStream = urlObject.openStream()) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity<String> filePath(String fileId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                tgFileInfoUrl,
                HttpMethod.GET,
                request,
                String.class,
                token,
                fileId
        );
    }

    private void removeExisting(Set<Category> categories, long departmentId) {
        categories.forEach(category-> {
            categoryService.deleteByNameAndDepartmentIdCascade(category.getName(), departmentId);
//            var persistentCategory = categoryRepo
//                    .findByNameAndDepartmentId(
//                            category.getName(),
//                            departmentId
//                    );
//
//            if (persistentCategory.isPresent()) {
//                var id = persistentCategory.get().getId();
//                categoryRepo.deleteById(id);
//            }
        });
    }
}
