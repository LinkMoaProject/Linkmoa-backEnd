CREATE TABLE Favorite (
                          favorite_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          item_id BIGINT,
                          item_type ENUM('SITE','DIRECTORY'),
                          order_index INT NOT NULL DEFAULT 0,
                          member_id BIGINT NOT NULL,
                          created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (member_id) REFERENCES Member(member_id) ON DELETE CASCADE
);