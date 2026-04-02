package com.example.link.output;

import com.example.link.model.dto.RawFile;
import com.example.link.model.dto.Ring;
import com.example.link.model.dto.RingsParsedFile;
import com.example.link.service.FileReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1")
@RequiredArgsConstructor
@Slf4j
public class LinkApplicationController {
    private final FileReaderService fileReaderService;

    @PostMapping(value = "/addFile")
    public ResponseEntity<RingsParsedFile> addFile(@RequestParam("file") MultipartFile multiPartFile) {
      //  log.info(multiPartFile.getOriginalFilename);
        RingsParsedFile processedData = fileReaderService.processedData(multiPartFile);
        return ResponseEntity.ok(processedData);
    }

}
