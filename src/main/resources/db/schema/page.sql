CREATE TABLE Page (
                      page_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      root_directory_id BIGINT NOT NULL,
                      page_title VARCHAR(50) NOT NULL,
                      page_description VARCHAR(150),
                      page_type ENUM('PERSONAL', 'BASIC', 'STANDARD', 'PREMIUM') NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);