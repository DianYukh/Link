package com.example.link.service;

import com.example.link.model.dto.ParsingError;
import com.example.link.model.dto.RawFile;
import com.example.link.model.dto.Ring;
import com.example.link.model.dto.RingsParsedFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
public class FileReaderService {
    public RingsParsedFile processedData(MultipartFile rawFile) {
        log.info("Start reading file: {}", rawFile.getOriginalFilename());
        try {

            String content = extractTextFromDocs(rawFile);
            List<Ring> rings = parsRing(content);
            if (rings.isEmpty()) {

                List<ParsingError> parsingErrors = Collections.singletonList(new ParsingError("NO_TEXT", "Error: not correct replicas"));

                log.info("Finished processing file: {}", rawFile.getOriginalFilename());
                Map<String, Integer> countReplicasByCharacter = countReplicasByCharacter(rings);
                return new RingsParsedFile(rawFile.getOriginalFilename(), rings, countReplicasByCharacter, null);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            return new RingsParsedFile(rawFile.getOriginalFilename(), new ArrayList<>(), new HashMap<>(), null);
        }

    }

    private Map<String, Integer> countReplicasByCharacter(List<Ring> rings) {

        Map<String, Integer> characterCounts = new HashMap<>();
        for (Ring ring : rings) {
            String character = ring.getCharacter();
            characterCounts.put(character, characterCounts.getOrDefault(character, 0) + 1);
        }
        log.info("Statistic of character: {}");
        characterCounts.forEach((character, count) -> {
            log.info(character + ": " + count);
        });
        return characterCounts;
    }

    private List<Ring> parsRing(String content) {
        List<Ring> rings = new ArrayList<>();
        if (content == null || content.trim().isEmpty()) {
            return rings;
        }
        Pattern ringCountPattern = Pattern.compile("(\\d{1,2}[:.](\\d{2}))[\\s\\t]+([А-ЯЁІЇЄҐа-яёіїєґA-Za-z]+)[:\\s\\t]*(.+?)(?=\\d{1,2}[:.](\\d{2})[\\s\\t]+[А-ЯЁІЇЄҐа-яёіїєґA-Za-z]+[:\\s\\t]|$)", Pattern.DOTALL);
        Matcher ringCountMatcher = ringCountPattern.matcher(content);
        log.info("Start parsing rings: {}", content);

        int ringCountNumber = 0;
        while (ringCountMatcher.find()) {
            String time = ringCountMatcher.group(1);
            String character = ringCountMatcher.group(3);
            String speech = ringCountMatcher.group(4).trim();
            speech = speech.replaceAll("\\s+", " ");
            time = time.replace(".", ":");
            Ring ring = new Ring(time, character, speech);
            rings.add(ring);
            ringCountNumber++;
            log.debug("Finished parsing rings: {}", time, character, speech);
        }
        log.info("Finished parsing rings: {}", rings.toString());
        return rings;

    }

    private String extractTextFromDocs(MultipartFile rawFile) throws IOException {
        StringBuilder processedRawText = new StringBuilder();

        try (XWPFDocument document = new XWPFDocument(rawFile.getInputStream())) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String paragraphText = paragraph.getText();
                if (!paragraphText.isEmpty()) {
                    processedRawText.append(paragraphText).append("\n");
                }
            }
        }
        log.info("Finished processing file: {}", processedRawText);
        return processedRawText.toString();
    }

    private RingsParsedFile parsRingValidator(String content) {
        List<Ring> rings = new ArrayList<>();
        List<String> errorUnparsedLines = new ArrayList<>();
        String[] lines = content.split("\\r?\\n");
        Pattern ringCountErrorPattern = Pattern.compile("(\\d{1,2}[:.](\\d{2}))[\\s\\t]+([А-ЯЁІЇЄҐа-яёіїєґA-Za-z]+)[:\\s\\t]*(.+?)(?=\\d{1,2}[:.](\\d{2})[\\s\\t]+[А-ЯЁІЇЄҐа-яёіїєґA-Za-z]+[:\\s\\t]|$)");

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }
            Matcher ringCountErrorMatcher = ringCountErrorPattern.matcher(line);
        }
    }


}
