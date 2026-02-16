package com.example.link.service;

import com.example.link.model.dto.RawFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class FileReaderService {
    public String processedData (RawFile rawFile) {
        log.info("Start reading file: {}", rawFile.getFileName());

        String processed = rawFile.getObject().trim();

        log.info("Finished processing file: {}", rawFile.getFileName());
        return processed;
    }
}
