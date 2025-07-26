package org.appitcompany.kuimakulak.service;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonS3Service{
    String uploadFile(MultipartFile file);
    void deleteFile(String key);
}
