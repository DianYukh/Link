package com.example.link.output;

import com.example.link.model.dto.RawFile;
import com.example.link.model.dto.Ring;
import com.example.link.model.dto.RingsParsedFile;
import com.example.link.service.FileReaderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class LinkApplicationTest {
    private LinkApplicationController controller;
    private FileReaderService readerService;

    @BeforeEach
    public void setUp() throws IOException {
        readerService = Mockito.mock(FileReaderService.class);
        controller = new LinkApplicationController(readerService);
    }
    @Test
    void testAddFileWithValidData(){
        String content = "00.05 НАРУТО Привет, как дела?";
        MultipartFile rawFile = new MockMultipartFile("file", "document.docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document", content.getBytes());
        List<Ring> replicas = new ArrayList<>();
        replicas.add(new Ring("00.05", "НАРУТО", "Привет, как дела?"));
        RingsParsedFile expectedRingsParsedFile = new RingsParsedFile("document.docx", replicas,new HashMap<>());
        when(readerService.processedData(rawFile)).thenReturn(expectedRingsParsedFile);


        ResponseEntity<RingsParsedFile> response = controller.addFile(rawFile);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("document.docx", response.getBody().getFileName());
        assertEquals(1, response.getBody().getReplicas().size());
    }

    @Test
    void testAddMultipleReplicas(){
        MultipartFile rawFile = new MockMultipartFile("file", "document.docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document", " ".getBytes());
        List<Ring> replicas = new ArrayList<>();
        replicas.add(new Ring("00.05", "НАРУТО", "Привет, как дела?"));
        replicas.add(new Ring("00.12", "НАРУТО", "Hello"));

        RingsParsedFile expectedDocument = new RingsParsedFile("document.docx", replicas,new HashMap<>());

        when (readerService.processedData(rawFile)).thenReturn(expectedDocument);

        ResponseEntity<RingsParsedFile> response = controller.addFile(rawFile);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("document.docx", response.getBody().getFileName());
        assertEquals(2, response.getBody().getReplicas().size());
    }

    @Test
    void testEmptyFile(){
        MultipartFile rawFile =  new MockMultipartFile("file", "document.docx","text/plain", " ".getBytes());

        RingsParsedFile expectedRingsParsedFile = new RingsParsedFile("emptydocument.docx", new ArrayList<>(), new HashMap<>());

        when(readerService.processedData(rawFile)).thenReturn(expectedRingsParsedFile);
        ResponseEntity<RingsParsedFile> response = controller.addFile(rawFile);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("emptydocument.docx", response.getBody().getFileName());
        assertEquals(0, response.getBody().getReplicas().size());
    }
}
