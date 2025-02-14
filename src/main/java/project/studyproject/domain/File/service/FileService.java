package project.studyproject.domain.File.service;

import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileService {
    private final MinioClient minioClient;

    @Value("${minio.server.bucket}")
    private String bucketName;

    // 업로드 파일
    public String uploadFile(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename(); // 파일이름 가져오고
        InputStream fileStream = file.getInputStream(); // 파일 데이터를 읽고 가져온다.

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        } // 버킷이 없으면 만들어주는 코드

        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(fileStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build(); // 파일 빌
        ObjectWriteResponse response = minioClient.putObject(putObjectArgs);
        // 미니오 서버에 반환하는 응답 객체

        String imageUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(fileName)
                        .build()
        );

        return imageUrl;

    }

    // 다운로드 파일
    public InputStream downloadFile(String fileName) throws Exception {
        try{
            return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build());
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    // 파일 가져오기
    public InputStream getFile(String fileName){
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다: " + fileName, e);
        }
    }
}
