CREATE TABLE Member (
                        member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        member_email VARCHAR(40) NOT NULL UNIQUE,
                        password VARCHAR(100) NOT NULL,
                        provider VARCHAR(20) NOT NULL,
                        provider_id VARCHAR(40) NOT NULL,
                        member_nickname VARCHAR(30) NOT NULL,
                        member_gender VARCHAR(10),
                        role ENUM('ROLE_USER','ROLE_ADMIN') NOT NULL,
                        member_job VARCHAR(40),
                        member_age_range VARCHAR(20),
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

