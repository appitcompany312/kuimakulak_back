package org.appitcompany.kuimakulak.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.appitcompany.kuimakulak.service.AmazonS3Service;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/s3")
public class AmazonS3Controller {
    private final AmazonS3Service amazonS3Service;

    @Operation(summary = "Загрузка файла в s3", description = "Авторизация: Все")
    @PostMapping(path = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return amazonS3Service.uploadFile(file);
    }

    @DeleteMapping("/deleteFile")
    @Operation(summary = "Удалить файл из Amazon S3"
            , description = "На вход должен быть ссылка из файла из Amazon S3")
    public String deleteFile(@RequestParam String fileUrl) {
        amazonS3Service.deleteFile(fileUrl);
        return "File deleted successfully";
    }
}
