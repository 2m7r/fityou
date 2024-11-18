package com.workout.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workout.model.dto.Diet;
import com.workout.model.dto.RecordDto;
import com.workout.model.dto.Workout;
import com.workout.model.service.RecordService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin("*")
@RequestMapping("/feed")
@Tag(name = "기록 관리 API", description = "운동기록의 CRUD 기능을 제공합니다.")
public class RecordRestController {

	private final RecordService recordService;

	public RecordRestController(RecordService recordService) {
		this.recordService = recordService;
	}

	// 일기 등록
    @PostMapping
    @Operation(summary = "일기 등록", description = "새로운 일기를 등록합니다.")
    public ResponseEntity<String> createRecord(
            @RequestParam("record") RecordDto record,
            @RequestParam("dietImage1") MultipartFile dietImage1, @RequestParam("text1") String text1,
            @RequestParam("dietImage2") MultipartFile dietImage2, @RequestParam("text2") String text2,
            @RequestParam("dietImage3") MultipartFile dietImage3, @RequestParam("text3") String text3,
            @RequestParam("workoutImage") MultipartFile workoutImage, @RequestParam("text4") String text4) {
        
        try {
            // JSON을 RecordDto 객체로 변환 (Jackson 사용)
//            ObjectMapper objectMapper = new ObjectMapper();
//            RecordDto record = objectMapper.readValue(recordJson, RecordDto.class);

            // 파일을 서버에 저장하고 경로 반환
            String dietImagePath1 = saveFile(dietImage1);
            String dietImagePath2 = saveFile(dietImage2);
            String dietImagePath3 = saveFile(dietImage3);
            String workoutImagePath = saveFile(workoutImage);

            // JSON 객체를 구성하여 diet과 workout에 값 설정 (JSON 문자열로 변환)
            Diet diet = new Diet(text1, dietImagePath1, text2, dietImagePath2, text3, dietImagePath3);
            Workout workout = new Workout(text4, workoutImagePath);

            // RecordDto에 diet과 workout 객체 설정
            record.setDiet(diet);  // Diet 객체 설정
            record.setWorkout(workout);  // Workout 객체 설정

            // Record 등록
            int result = recordService.registRecord(record);
            if (result > 0) {
                return new ResponseEntity<>("Record created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Failed to create record", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 파일을 지정된 경로에 저장하고 경로를 반환
    private String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // 파일을 저장할 디렉토리 생성
        String UPLOAD_DIR = "uploads"; // 업로드 디렉토리 경로
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // 파일 이름 설정
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);

        // 파일 저장
        file.transferTo(filePath.toFile());

        // 저장된 파일 경로 반환
        return filePath.toString();
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
