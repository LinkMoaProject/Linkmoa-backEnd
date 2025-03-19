CREATE TABLE Directory (
                           directory_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           directory_name VARCHAR(30) NOT NULL,
                           directory_description VARCHAR(150),
                           parent_directory_id BIGINT NULL,
                           order_index INT NOT NULL DEFAULT 0,
                           created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                           updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (parent_directory_id) REFERENCES Directory(directory_id) ON DELETE CASCADE
);