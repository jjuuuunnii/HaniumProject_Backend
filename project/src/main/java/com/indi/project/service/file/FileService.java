package com.indi.project.service.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.indi.project.entity.User;
import com.indi.project.entity.Video;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.UserRepository;
import com.indi.project.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {
    private final AmazonS3 amazonS3;
    private final VideoRepository videoRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    @Transactional
    public void deleteVideo(User user, Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));
        if (!video.getUser().equals(user)) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }

        deleteFile(video.getVideoPath());
        deleteFile(video.getThumbNailPath());
        videoRepository.deleteById(video.getId());
    }

    public void deleteFile(String filePath) {
        amazonS3.deleteObject(bucketName, filePath);
    }

    public String saveFile(MultipartFile file, String dir) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String path = dir + "/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        amazonS3.putObject(new PutObjectRequest(bucketName, path, file.getInputStream(), metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return path;
    }
}
