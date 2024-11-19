package com.workout.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.workout.model.dto.RecordDto;
import com.workout.model.service.RecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/feed")
@Tag(name = "기록 관리 API", description = "운동기록의 CRUD 기능을 제공합니다.")
public class RecordRestController {

    private final RecordService recordService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public RecordRestController(RecordService recordService) {
        this.recordService = recordService;
    }

    // 운동 기록 등록 API
    @PostMapping("/create")
    @Operation(summary = "일기 등록", description = "새로운 일기를 등록합니다.")
    public ResponseEntity<?> createRecord(
            @RequestParam("userId") Long userId,
            @RequestParam("recordDate") String recordDate,
            @RequestParam("isSharedWithTrainer") boolean isSharedWithTrainer,
            @RequestParam(value = "dietImages", required = false) MultipartFile[] dietImages,
            @RequestParam(value = "workoutImage", required = false) MultipartFile workoutImage,
            @RequestParam("content") String content) {

        try {
            // 이미지 저장 경로 설정
            String dietImagePath = saveImages(dietImages);
            String workoutImagePath = saveImage(workoutImage);  // single image for workout

            // RecordDto 생성
            RecordDto recordDto = new RecordDto();
            recordDto.setUserId(userId);
            recordDto.setRecordDate(recordDate);
            recordDto.setSharedWithTrainer(isSharedWithTrainer);
            recordDto.setContent(content);

            // Diet 사진 경로를 List로 설정
            if (dietImagePath != null) {
                recordDto.setDietPhotoPaths(List.of(dietImagePath.split(",")));
            }
            
            // Workout 사진 경로 설정
            recordDto.setWorkout(workoutImagePath);

            // DB에 운동 기록 저장
            recordService.registRecord(recordDto);

            return ResponseEntity.status(HttpStatus.CREATED).body("운동 기록 등록 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("운동 기록 등록 실패: " + e.getMessage());
        }
    }

    // 이미지 저장 로직
    private String saveImages(MultipartFile[] images) throws IOException {
        if (images == null || images.length == 0) {
            return null;
        }

        StringBuilder imagePaths = new StringBuilder();
        for (MultipartFile image : images) {
            String originalFilename = image.getOriginalFilename();
            String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

            // 파일 저장
            File file = new File(uploadDir + File.separator + uniqueFilename);
            image.transferTo(file);

            // 저장된 파일 경로
            imagePaths.append(uniqueFilename).append(",");
        }

        // 마지막 쉼표 제거
        return imagePaths.toString().endsWith(",") ? imagePaths.substring(0, imagePaths.length() - 1) : imagePaths.toString();
    }

    // Single image save logic for workout
    private String saveImage(MultipartFile image) throws IOException {
        if (image == null) {
            return null;
        }

        String originalFilename = image.getOriginalFilename();
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // 파일 저장
        File file = new File(uploadDir + File.separator + uniqueFilename);
        image.transferTo(file);

        // 저장된 파일 경로
        return uniqueFilename;
    }

    // 일기 상세 조회
    @GetMapping("/{recordId}")
    @Operation(summary = "일기 상세 조회", description = "일기 상세를 조회합니다.")
    public ResponseEntity<RecordDto> getRecordById(@PathVariable Long recordId) {
        try {
            RecordDto record = recordService.getRecordById(recordId);
            if (record != null) {
                return new ResponseEntity<>(record, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 일기 수정
    @PutMapping("/{recordId}")
    @Operation(summary = "일기 수정", description = "일기를 수정합니다.")
    public ResponseEntity<String> updateRecord(@PathVariable Long recordId, @RequestBody RecordDto record) {
        try {
            record.setRecordId(recordId); // recordId 설정
            int result = recordService.modifyRecord(record);
            if (result > 0) {
                return new ResponseEntity<>("Record updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update record", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 일기 삭제
    @DeleteMapping("/{recordId}")
    @Operation(summary = "일기 삭제", description = "일기를 삭제합니다.")
    public ResponseEntity<String> deleteRecord(@PathVariable Long recordId) {
        try {
            int result = recordService.removeRecord(recordId);
            if (result > 0) {
                return new ResponseEntity<>("Record deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to delete record", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 특정 유저의 모든 일기 조회
    @GetMapping("/user/{userId}")
    @Operation(summary = "특정 유저의 모든 일기 조회", description = "특정 유저의 모든 일기를 조회합니다.")
    public ResponseEntity<List<RecordDto>> getRecordsByUserId(@PathVariable Long userId) {
        try {
            List<RecordDto> records = recordService.getRecordsByUserId(userId);
            if (records != null && !records.isEmpty()) {
                return new ResponseEntity<>(records, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 팔로우한 유저들의 일기 조회
    @GetMapping("/followers/{userId}")
    @Operation(summary = "팔로우한 유저들의 일기 조회", description = "팔로우한 유저들의 일기를 조회합니다.")
    public ResponseEntity<List<List<RecordDto>>> getFollowersRecordsByUserId(@PathVariable Long userId) {
        try {
            List<List<RecordDto>> followersRecords = recordService.getFollowersRecordsByUserId(userId);
            if (followersRecords != null && !followersRecords.isEmpty()) {
                return new ResponseEntity<>(followersRecords, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
