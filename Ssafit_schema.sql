drop database IF EXISTS final_pjt;

create database final_pjt;
use final_pjt;

drop TABLE IF EXISTS users;
-- 1. 사용자와 트레이너를 통합 관리
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유 식별 ID
    name VARCHAR(100) NOT NULL, -- 이름
    birthDate DATE NOT NULL, -- 생년월일
    phoneNum VARCHAR(20), -- 전화번호
    gender ENUM('M', 'F', 'O') NOT NULL, -- 성별
    email VARCHAR(255) UNIQUE NOT NULL, -- 이메일
    username VARCHAR(50) UNIQUE NOT NULL, -- 로그인 아이디
    password VARCHAR(255) NOT NULL, -- 로그인 PW
    role ENUM('USER', 'TRAINER') NOT NULL, -- 일반유저 / 트레이너
    gym_name VARCHAR(255), -- 트레이너만 사용
    is_private_account BOOLEAN DEFAULT FALSE, -- 공개 / 비공개 계정
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    profileImage VARCHAR(255), -- 프로필 이미지 경로
    security_question VARCHAR(255) NOT NULL, -- 본인 확인 질문
    security_answer VARCHAR(255) NOT NULL -- 본인 확인 답변
);

INSERT INTO users (name, birthDate, gender, email, username, password, role, gym_name, is_private_account, phoneNum, security_question, security_answer)
VALUES
('지피티', '1990-01-01', 'F', 'pt@pt.com', 'trainer', 'trainerpassword', 'TRAINER', '핏니스 센터', FALSE, '010-1111-2222', '당신의 첫 번째 강아지 이름은 무엇인가요?', '바둑이'),
('김싸피', '2024-11-11', 'M', 'ssafy@ssafy.com', 'ssafy', 'ssafy', 'USER', NULL, FALSE, '010-3333-4444', '어린 시절 즐겨먹었던 음식은 무엇인가요?', '떡볶이'),
('임가현', '1999-10-01', 'F', '2m7r.sw@gmail.com', 'rkgus', 'rkgus', 'USER', NULL, TRUE, '010-5555-6666', '당신의 어릴 적 친구의 이름은 무엇인가요?', '정은지'),
('홍길동', '1985-05-20', 'M', 'hong@example.com', 'hong123', 'password123hash', 'USER', NULL, FALSE, '010-7777-8888', '당신의 첫 번째 학교는 어디였나요?', '서울초등학교'),
('박지현', '1992-07-15', 'F', 'park@example.com', 'jihan92', 'mypasswordhash', 'USER', NULL, TRUE, '010-9999-0000', '가장 기억에 남는 여행지는 어디인가요?', '강릉'),
('김민수', '1987-03-30', 'M', 'minsu@example.com', 'minsu88', 'minsu123hash', 'USER', NULL, FALSE, '010-2222-3333', '어릴 적 가장 좋아했던 책은 무엇인가요?', '어린왕자'),
('이하늘', '1989-08-10', 'F', 'haneul@example.com', 'haneul1989', 'mypasswordhash', 'TRAINER', '하늘의 체육관', FALSE, '010-4444-5555', '당신의 첫 번째 애완동물 이름은 무엇인가요?', '치치'),
('정우성', '1995-12-25', 'M', 'woosung@example.com', 'woosung95', 'woosungpasshash', 'USER', NULL, FALSE, '010-6666-7777', '가장 좋아하는 영화는 무엇인가요?', '어벤져스'),
('소지섭', '1991-02-11', 'M', 'soji@example.com', 'soji1991', 'soji123hash', 'TRAINER', '소의 피트니스', FALSE, '010-8888-9999', '어린 시절 기억에 남는 선생님의 이름은 무엇인가요?', '김선생'),
('유진', '1993-04-17', 'F', 'yujin@example.com', 'yujin93', 'yujinpasshash', 'USER', NULL, TRUE, '010-1111-3333', '가장 좋아하는 계절은 무엇인가요?', '봄'),
('이태웃씌', '1997-03-05', 'O', 'xodn@xodn.com', 'xodn', 'xodn', 'USER', NULL, TRUE, '010-2222-4444', '어린 시절 가장 좋아했던 TV 프로그램은 무엇인가요?', '딩동댕');

 SELECT * FROM users;


drop table IF EXISTS preferred_exercises;
-- 선호 운동
CREATE TABLE preferred_exercises (
    exercise_id BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id BIGINT NOT NULL,  -- 사용자 ID
    exercise_name VARCHAR(100) NOT NULL,  -- 운동 이름
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 생성 시간
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT unique_user_exercise UNIQUE (user_id, exercise_name)  -- 유니크 제약

);

-- select * from preferred_exercises;

-- ------------------------- 식단일기 -----------------------------------
DROP TABLE IF EXISTS diet;
-- 2. 식단
CREATE TABLE diet (
    diet_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL, -- 사용자 ID
    breakfast_image VARCHAR(255),  -- 아침 식단 사진 경로
    lunch_image VARCHAR(255),      -- 점심 식단 사진 경로
    dinner_image VARCHAR(255),      -- 저녁 식단 사진 경로
    content TEXT,  -- 기록 내용 (식단 내용 등)
    record_date DATE NOT NULL,  -- 기록 날짜 (YYYY-MM-DD)
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT unique_record_date UNIQUE (user_id, record_date) -- user_id와 record_date가 복합적으로 유니크
);

-- insert into diet (user_id, record_date)
-- values (1, '2024-11-19');
INSERT INTO diet (user_id, breakfast_image, lunch_image, dinner_image, content, record_date)
VALUES 
(3, 
 'images/breakfast_2024_11_24.jpg',  
 'images/lunch_2024_11_24.jpg',      
 'images/dinner_2024_11_24.jpg',     
 '2024년 11월 24일의 식단 기록입니다. 아침에는 오트밀과 과일을 먹었고, 점심에는 샐러드와 치킨, 저녁에는 고구마와 야채를 섭취했습니다.',  
 '2024-11-30'),(3, 
 'images/breakfast_2024_11_24.jpg',  
 'images/lunch_2024_11_24.jpg',      
 'images/dinner_2024_11_24.jpg',     
 '2024년 11월 24일의 식단 기록입니다. 아침에는 오트밀과 과일을 먹었고, 점심에는 샐러드와 치킨, 저녁에는 고구마와 야채를 섭취했습니다.',  
 '2024-11-02');
 
 SELECT * FROM diet WHERE user_id = 3 AND record_date = '2024-11-22';


-- SELECT * FROM diet
-- WHERE user_id = 1
-- ORDER BY record_date DESC;


-- ------------------------- 팔로우 -----------------------------------
drop table IF EXISTS follows;
-- 3. 팔로우 관계를 저장
CREATE TABLE follows (
    user_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, following_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(user_id) ON DELETE CASCADE
);

INSERT INTO follows (user_id, following_id)
VALUES
(1, 2),  -- '지피티'가 '김싸피'를 팔로우
(1, 3),  -- '지피티'가 '임가현'을 팔로우
(2, 3),  -- '김싸피'가 '임가현'을 팔로우
(2, 4),  -- '김싸피'가 '홍길동'을 팔로우
(3, 1),  -- '임가현'이 '지피티'를 팔로우
(3, 5),  -- '임가현'이 '박지현'을 팔로우
(4, 5),  -- '홍길동'이 '박지현'을 팔로우
(4, 6),  -- '홍길동'이 '김민수'를 팔로우
(5, 7),  -- '박지현'이 '이하늘'을 팔로우
(6, 1),  -- '김민수'가 '지피티'를 팔로우
(1, 11), (2, 11), (3, 11), (4, 11), (5, 11), (6, 11), (7, 11), (8, 11), (9, 11);

-- SELECT * FROM follows;


-- --------------------------- 챌린지 -----------------------------------
drop table IF EXISTS challenges;
-- 4. 챌린지 정보를 저장
CREATE TABLE challenges (
    challenge_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유 식별 ID
    name VARCHAR(255) NOT NULL, -- 챌린지 이름
    creator_user_id BIGINT NOT NULL, -- 챌린지 등록 유저ID
    description TEXT, -- 설명
    participant_count INT DEFAULT 1, -- 챌린지 현재 참여자 수
    duration_start DATE NOT NULL, -- 시작날짜
    duration_end DATE NOT NULL, -- 끝나는 날짜
    exercise_type VARCHAR(100), -- 헬스, 요가, 식단 등등
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

insert into challenges (name, creator_user_id, description, duration_start, duration_end, exercise_type)
values
('빡세게 살빼자', 1, '빡세게 살 뺄 분만 오세요', '2024-10-10', '2024-12-14', '헬스'),
('요가로 마음 챙기기', 3, '스트레스 해소와 유연성 향상을 위한 요가 챌린지', '2024-09-01', '2024-10-30', '요가'),
('체중 감량 챌린지', 4, '건강하게 체중을 감량하는 도전', '2024-11-01', '2024-12-01', '헬스'),
('식단 관리 마스터', 5, '균형 잡힌 식단으로 건강한 생활', '2024-11-10', '2024-12-10', '식단'),
('힙업 챌린지', 6, '힙업과 하체 강화 운동', '2024-10-15', '2024-11-30', '헬스'),
('매일 운동하기', 7, '매일 꾸준히 운동하며 체력 키우기', '2024-08-20', '2024-11-20', '헬스'),
('건강한 아침 시작', 8, '매일 아침 건강한 루틴으로 하루 시작하기', '2024-09-01', '2024-09-30', '식단'),
('매일 10,000보 걷기', 9, '하루 10,000보 걷기로 체력 향상', '2024-07-01', '2024-09-01', '운동'),
('스트레칭으로 시작하는 하루', 10, '하루를 스트레칭으로 시작하며 유연성 키우기', '2024-10-01', '2024-10-31', '요가'),
('체지방 감소 프로젝트', 11, '체지방 감소를 목표로 하는 운동 챌린지', '2024-11-05', '2024-12-05', '헬스'),
('헬스 루틴 만들기', 2, '자기 맞춤형 헬스 루틴 만들기 도전', '2024-09-10', '2024-11-10', '헬스'),
('하루 30분 요가', 3, '하루 30분 요가로 유연성 키우기', '2024-10-05', '2024-11-05', '요가'),
('식단 조절로 다이어트', 4, '식단 조절을 통해 다이어트 성공하기', '2024-10-15', '2024-11-15', '식단'),
('운동으로 스트레스 날리기', 5, '운동으로 스트레스를 해소하는 도전', '2024-09-01', '2024-10-01', '헬스'),
('매일 20분 운동', 6, '매일 20분 운동으로 건강을 지키기', '2024-08-01', '2024-10-01', '헬스'),
('요가로 체형 개선', 7, '요가를 통해 체형을 개선하고 건강하게', '2024-10-01', '2024-12-01', '요가'),
('다이어트 챌린지', 8, '건강한 다이어트를 위한 챌린지', '2024-09-01', '2024-11-01', '식단'),
('근력 훈련 챌린지', 9, '근력 훈련으로 강한 몸 만들기', '2024-10-01', '2024-11-30', '헬스'),
('꾸준한 운동 습관 만들기', 10, '꾸준히 운동해서 좋은 습관 만들기', '2024-08-15', '2024-11-15', '헬스');

-- select * from challenges;


drop table if exists challenge_people;
-- 5. 챌린지별 참여하고 있는 참여자 목록 저장
create table challenge_people (
	user_id BIGINT NOT NULL, -- 참여한 유저ID
    challenge_id BIGINT NOT NULL, -- 참여한 챌린지ID
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (challenge_id) REFERENCES challenges(challenge_id) ON DELETE CASCADE
);

INSERT INTO challenge_people (user_id, challenge_id) VALUES
(1, 1),
(3, 2),
(4, 3),
(5, 4),
(6, 5),
(7, 6),
(8, 7),
(9, 8),
(10, 9),
(11, 10),
(2, 11),
(3, 12),
(4, 13),
(5, 14),
(6, 15),
(7, 16),
(8, 17),
(9, 18),
(10, 19);

-- select * from challenge_people;

-- -----------------------운동 일기 ----------------------------------------------
-- 6. 운동
DROP TABLE IF EXISTS workouts;
CREATE TABLE workouts (
    workout_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유ID
    user_id BIGINT NOT NULL, -- 유저ID
    description TEXT, -- 설명? 말?
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- SELECT * from workouts;

-- 7. 운동일기 사진
DROP TABLE IF EXISTS workout_photos;
CREATE TABLE workout_photos (
    photo_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    workout_id BIGINT NOT NULL,
    photo_url VARCHAR(255) NOT NULL, -- 사진 경로(URL 또는 파일 경로)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (workout_id) REFERENCES workouts(workout_id) ON DELETE CASCADE
);
-- SELECT * from workout_photos;

-- 8. 운동 종류
DROP TABLE IF EXISTS exercises;
CREATE TABLE exercises (
    exercise_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유ID
    name VARCHAR(100) NOT NULL UNIQUE, -- 운동 이름
    category VARCHAR(50) -- 가슴, 어깨, 등 등
);
insert into exercises (name, category)
values
	('벤치 프레스', '가슴'),
    ('인클라인 벤치 프레스', '가슴'),
    ('덤벨 플라이', '가슴'),
    ('체스트 프레스', '가슴'),
    ('풀업', '등'),
    ('랫 풀다운', '등'),
    ('바벨 로우', '등'),
    ('덤벨 로우', '등'),
    ('데드리프트', '등'),
    ('숄더 프레스', '어깨'),
    ('덤벨 숄더 프레스', '어깨'),
    ('사이드 레터럴 레이즈', '어깨'),
    ('프론트 레이즈', '어깨'),
    ('오버헤드 프레스', '어깨'),
    ('버피', '전신'),
    ('스쿼트 점프', '전신'),
    ('클린 앤드 저크', '전신'),
    ('케틀벨 스윙', '전신'),
    ('푸쉬업', '전신'),
    ('플랭크', '코어'),
    ('러시안 트위스트', '코어'),
    ('레그 레이즈', '코어'),
    ('싯업', '코어'),
    ('힙 브릿지', '코어'),
    ('바벨 컬', '팔'),
    ('덤벨 컬', '팔'),
    ('트라이셉스 푸시다운', '팔'),
    ('덤벨 트라이셉스 킥백', '팔'),
    ('해머 컬', '팔'),
    ('스쿼트', '하체'),
    ('레그 프레스', '하체'),
    ('런지', '하체'),
    ('힙 스러스트', '하체'),
    ('카프 레이즈', '하체');

-- SELECT * from exercises;

-- 9. 운동 루틴
DROP TABLE IF EXISTS workout_exercises;
CREATE TABLE workout_exercises (
    id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 고유ID
    workout_id BIGINT NOT NULL, -- 운동일기ID
    exercise_id BIGINT NOT NULL, -- 운동종류ID
    weight INT, -- 무게(kg)
    reps INT,   -- 횟수
    sets INT,   -- 세트 수
    FOREIGN KEY (workout_id) REFERENCES workouts(workout_id) ON DELETE CASCADE,
    FOREIGN KEY (exercise_id) REFERENCES exercises(exercise_id) ON DELETE CASCADE
);
-- SELECT * from workout_exercises;


-- ------------------------- 댓글 -----------------------------------
drop table IF EXISTS comments;
-- 10. 댓글 기록
CREATE TABLE comments (
    comment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    target_id BIGINT NOT NULL, -- 댓글이 속한 대상의 ID
    target_type ENUM('diet', 'workout') NOT NULL, -- 댓글이 연결된 유형 (식단 or 운동)
    user_id BIGINT NOT NULL, -- 댓글 작성자 ID
    content TEXT NOT NULL, -- 댓글 내용
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);
-- insert into comments (target_id, target_type, user_id, content)
-- values (4, 'workout', 1, '트레이너임?');

-- SELECT * from comments;


-- ------------------------- 트레이너-회원관계 -----------------------------------
drop table IF EXISTS trainer_clients;
-- 11. 트레이너와 회원의 관계를 저장
CREATE TABLE trainer_clients (
    trainer_id BIGINT NOT NULL, -- 트레이너의 user_id
    client_id BIGINT NOT NULL, -- PT 회원의 user_id
    comment TEXT, -- 특이사항
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (trainer_id, client_id),
    FOREIGN KEY (trainer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES users(user_id) ON DELETE CASCADE
);

INSERT INTO trainer_clients (trainer_id, client_id, comment, created_at)
VALUES
(1, 2, '초보자이며 체력 향상이 필요', '2024-11-01 09:00:00'), 
(1, 3, '유연성 향상을 위한 운동 집중', '2024-11-01 09:30:00'), 
(1, 4, '중급자 수준, 체중 감량 목표', '2024-11-02 10:00:00'), 
(7, 5, '근력 훈련 강화가 필요', '2024-11-02 10:30:00'), 
(7, 6, '체중 증가와 근육량 증가 목표', '2024-11-03 11:00:00'), 
(9, 8, '스포츠 재활 치료 중', '2024-11-04 12:00:00'), 
(9, 10, '체력 유지 및 유산소 운동 강화', '2024-11-05 13:00:00');


-- SELECT * from trainer_clients;

-- -------------------------  -----------------------------------






-- drop table IF EXISTS awards;
-- -- 수상경력
-- CREATE TABLE awards (
--     award_id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_id BIGINT NOT NULL, -- 트레이너의 ID
--     award_name VARCHAR(255) NOT NULL, -- 수상 경력 이름
--     award_year INT, -- 수상 연도
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
-- );







-- select * from trainer_clients;
-- select * from records;
-- select * from preferred_exercises;
-- select * from likes;
-- select * from follows;
-- select * from diet;
-- select * from comments;
-- select * from challenges;
-- select * from awards;
-- select * from users;
-- select * from workout;
