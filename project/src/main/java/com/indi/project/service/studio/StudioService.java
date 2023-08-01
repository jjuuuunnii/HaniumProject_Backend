package com.indi.project.service.studio;
import com.indi.project.dto.studio.UserVideoListDto;
import com.indi.project.dto.studio.VideoJoinDto;
import com.indi.project.entity.Genre;
import com.indi.project.entity.User;
import com.indi.project.entity.Video;
import com.indi.project.exception.CustomException;
import com.indi.project.exception.ErrorCode;
import com.indi.project.repository.UserRepository;
import com.indi.project.repository.VideoRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudioService {
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    @Value("${file.dir.video}")
    private String videoFileDir;

    @Value("${file.dir.thumbnail}")
    private String thumbNailDir;

    @Transactional
    public void joinUserVideo(VideoJoinDto videoJoinDto, String loginId) {
        try {

            MultipartFile videoFile = videoJoinDto.getVideoFile();
            MultipartFile thumbNail = videoJoinDto.getThumbNail();

            String videoFileName = getFileName(videoFile);
            String thumbNailFileName = getFileName(thumbNail);

            String videoFilePath = videoFileDir + File.separator + videoFileName;
            String thumbNailFilePath = thumbNailDir + File.separator + thumbNailFileName;

            videoFile.transferTo(new File(videoFilePath));
            thumbNail.transferTo(new File(thumbNailFilePath));

            Video video = getVideo(videoJoinDto, loginId, videoFilePath, thumbNailFilePath);
            videoRepository.save(video);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.STORE_FILES_FAILS);
        }
    }

    public List<UserVideoListDto> getUserVideoList(String loginId) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Video> videos = user.getVideos();

        return videos.stream()
                .map(video -> UserVideoListDto.builder()
                        .thumbNailPath(video.getThumbNailPath())
                        .videoTitle(video.getTitle())
                        .videoFilePath(video.getVideoPath())
                        .likes(video.totalLikesCnt())
                        .views(video.getViews())
                        .nickName(user.getNickName())
                        .uploadDateTime(video.getCreatedAt().toString())
                        .genre(video.getGenre().toString())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUserVideo(String loginId, Long videoId) {
        // Confirm the user exists
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // Confirm the video exists
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new CustomException(ErrorCode.VIDEO_NOT_FOUND));

        // Confirm the user is the owner of the video
        if (!video.getUser().equals(user)) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }

        // Delete the video file and thumbnail file
        deleteFile(video.getVideoPath());
        deleteFile(video.getThumbNailPath());

        // Delete the video from the database
        videoRepository.deleteById(video.getId());
    }


    public boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (!file.delete()) {
                throw new CustomException(ErrorCode.FILE_DELETION_FAILED);
            }
            return true;
        }
        return false;
    }

    private Video getVideo(VideoJoinDto videoJoinDto, String loginId, String videoFilePath, String thumbNailFilePath) {
        Video video = Video.builder()
                .user(userRepository.findByLoginId(loginId).get())
                .genre(Genre.fromString(videoJoinDto.getGenre()))
                .videoPath(videoFilePath)
                .thumbNailPath(thumbNailFilePath)
                .title(videoJoinDto.getTitle())
                .views(0)
                .createdAt(LocalDateTime.now())
                .build();
        return video;
    }

    private static String getFileName(MultipartFile videoFile) {
        String videoFileName = UUID.randomUUID() + "_" + videoFile.getOriginalFilename();
        return videoFileName;
    }
}
