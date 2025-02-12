package org.klozevitz.services.implementations.updateProcessors.docUpdateProcessors;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.Package;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.services.messageProcessors.UpdateProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Resource;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

@Log4j
@RequiredArgsConstructor
public class DocumentUP implements UpdateProcessor {
    @Value("${fileService.service.file_info.url}")
    private String fileInfoUrl;
    @Value("${fileService.service.file_storage.url}")
    private String fileStorageUrl;
    @Value("${fileService.token}")
    private String token;

    @Resource(name = "nullableState_UpdateProcessor")
    private UpdateProcessor nullableStateUpdateProcessor;
    @Resource(name = "previousView_UpdateProcessor")
    private UpdateProcessor previousViewUpdateProcessor;


    @Override
    public SendMessage processUpdate(Update update, AppUser currentAppUser) {
        var state = currentAppUser.getDepartment().getState();

        if (state == null) {
            return nullableStateUpdateProcessor.processUpdate(update, currentAppUser);
        }

        switch (state) {
            case WAIT_FOR_DOCUMENT_STATE:
                // TODO вернуть сообщение об том, что все окей
                processDocumentMessage(update, currentAppUser);
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
            default:
                return previousViewUpdateProcessor.processUpdate(update, currentAppUser);
        }
    }

    private SendMessage processDocumentMessage(Update update, AppUser currentAppUser) {
        var fileId = update
                .getMessage()
                .getDocument()
                .getFileId();
        ResponseEntity<String> response = filePath(fileId);
        if (response.getStatusCode() == HttpStatus.OK) {
            ByteArrayInputStream bin = fileAsByteArrayInputStream(response);
            try {
                Workbook workbook = new XSSFWorkbook(Package.open(bin));
                System.out.println();
            } catch (IOException | InvalidFormatException e) {
                // TODO ОБЯЗАТЕЛЬНО!!! вернуть сообщение-вью с ошибкой
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private ByteArrayInputStream fileAsByteArrayInputStream(ResponseEntity<String> response) {
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
        var fileUrl = fileStorageUrl
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
                fileInfoUrl,
                HttpMethod.GET,
                request,
                String.class,
                token,
                fileId
        );
    }
}
