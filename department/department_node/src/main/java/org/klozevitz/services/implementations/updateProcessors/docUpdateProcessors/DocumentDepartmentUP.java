package org.klozevitz.services.implementations.updateProcessors.docUpdateProcessors;

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
import org.klozevitz.enitites.menu.Item;
import org.klozevitz.repositories.appUsers.AppUserRepo;
import org.klozevitz.services.implementations.util.ExcelToTestParser;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Log4j
@RequiredArgsConstructor
public class DocumentDepartmentUP implements UpdateProcessor {
    @Value("${fileService.service.file_info.url}")
    private String tgFileInfoUrl;
    @Value("${fileService.service.file_storage.url}")
    private String tgFileStorageUrl;
    @Value("${fileService.token}")
    private String token;
    private final AppUserRepo appUserRepo;
    private final UpdateProcessor nullableStateUpdateProcessor;
    private final UpdateProcessor previousViewUpdateProcessor;
    private final ExcelToTestParser parser;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case WAIT_FOR_DOCUMENT_STATE:
                processDocumentMessage(update, currentAppUser);
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }

    private SendMessage processDocumentMessage(Update update, AppUser currentAppUser) {
        var fileId = fileId(update);
        ResponseEntity<String> response = filePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            ByteArrayInputStream bin = fileAsByteArrayInputStream(response);
            try {
                Workbook workbook = new XSSFWorkbook(Package.open(bin));
                Set<Item> menu = parser.parseMenu(workbook);
                var fileName = fileName(update);
                var persistentDepartment = currentAppUser.getDepartment();
                var indexOfExtension = indexOfExtension(fileName);
                var categoryName = fileName.substring(0, indexOfExtension);
                var transientCategory = transientCategory(menu, persistentDepartment, categoryName);

                setLinks(persistentDepartment, menu, transientCategory);
                appUserRepo.save(currentAppUser);
                System.out.println();
                // TODO ОБЯЗАТЕЛЬНО!!! вернуть нормальную вьюху
                return null;
            } catch (IOException | InvalidFormatException e) {
                // TODO ОБЯЗАТЕЛЬНО!!! вернуть сообщение-вью с ошибкой
                throw new RuntimeException(e);
            }
        }
        // TODO вернуть вью о невозможности скачать файл
        return null;
    }

    private void setLinks(Department persistentDepartment, Set<Item> menu, Category transientCategory) {
        menu.forEach(item -> item.setCategory(transientCategory));
        persistentDepartment.getMenu().add(transientCategory);
    }

    private Category transientCategory(Set<Item> menu, Department persistentAppDepartment, String categoryName) {
        return Category.builder()
                .name(categoryName)
                .department(persistentAppDepartment)
                .menu(menu)
                .build();
    }

    private int indexOfExtension(String fileName) {
        return fileName.lastIndexOf(".xlsx") == -1 ?
                fileName.lastIndexOf("xls") - 1 :
                fileName.lastIndexOf("xlsx") - 1;
    }

    private String fileId(Update update) {
        return update
                .getMessage()
                .getDocument()
                .getFileId();
    }

    private String fileName(Update update) {
        return update
                .getMessage()
                .getDocument()
                .getFileName();
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
}
