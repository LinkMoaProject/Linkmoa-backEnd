CREATE TABLE Site (
                      site_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      site_name VARCHAR(30) NOT NULL,
                      site_url VARCHAR(255) NOT NULL,
                      order_index INT NOT NULL DEFAULT 0,
                      directory_id BIGINT NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (directory_id) REFERENCES Directory(directory_id) ON DELETE CASCADE
);