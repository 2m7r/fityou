use final_pjt;

-- 사용자와 트레이너를 통합 관리
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender ENUM('M', 'F', 'O') NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('USER', 'TRAINER') NOT NULL,
    gym_name VARCHAR(255), -- 트레이너만 사용
    awards TEXT, -- 트레이너만 사용
    preferred_exercise VARCHAR(100),
    is_private_account BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 기록 정보를 저장
CREATE TABLE records (
    record_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    record_date DATE NOT NULL, -- YYYY-MM-DD
    diet TEXT, -- JSON 또는 Text로 저장
    workout TEXT, -- JSON 또는 Text로 저장
    comments TEXT, -- 코멘트
    is_shared_with_trainer BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 챌린지 정보를 저장
CREATE TABLE challenges (
    challenge_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creator_user_id BIGINT NOT NULL,
    description TEXT,
    participant_count INT DEFAULT 0,
    challenge_rank INT,
    duration_start DATE NOT NULL,
    duration_end DATE NOT NULL,
    exercise_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 팔로우 관계를 저장
CREATE TABLE follows (
    user_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, following_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 트레이너와 회원의 관계를 저장
CREATE TABLE trainer_clients (
    trainer_id BIGINT NOT NULL, -- 트레이너의 user_id
    client_id BIGINT NOT NULL, -- PT 회원의 user_id
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (trainer_id, client_id),
    FOREIGN KEY (trainer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (client_id) REFERENCES users(user_id) ON DELETE CASCADE
);

select * from users;
select * from records;
select * from challenges;
select * from follows;
select * from trainer_clients;