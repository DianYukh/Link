package com.example.link.output;

import com.example.link.model.dto.RawFile;
import com.example.link.service.FileReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Slf4j
public class LinkApplicationController {
    private final FileReaderService fileReaderService;

    @PostMapping(value = "/addFile")
    public ResponseEntity<String> addFile(@RequestBody RawFile rawFile) {
        log.info(rawFile.getFileName());
        String processedData = fileReaderService.processedData(rawFile);
        return ResponseEntity.ok("File is accepted");
    }

}
