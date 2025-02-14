package project.studyproject.domain.File.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.studyproject.domain.File.service.FileService;

import java.io.InputStream;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    // 업로드
    @PostMapping
    public ResponseEntity<String> uploadFiles(@RequestParam("file") MultipartFile file) {
        try {
            String response = fileService.uploadFile(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error Upload" + e.getMessage());
        }

    }

    // 다운로드
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            InputStream fileStream = fileService.downloadFile(fileName);
            byte[] content = fileStream.readAllBytes();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(content);

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body(null);
        }
    }

    // 보여주기
    @GetMapping("/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFileAsResource(@PathVariable String fileName) {
            InputStream fileStream = fileService.getFile(fileName);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.IMAGE_PNG)  // PNG 이미지로 가정
                    .body(new InputStreamResource(fileStream));

    }

}
