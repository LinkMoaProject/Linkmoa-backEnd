CREATE TABLE Member_Page_Link (
                                  member_page_link_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  page_id BIGINT NOT NULL,
                                  member_id BIGINT NOT NULL,
                                  permission_type ENUM('ADMIN','HOST','EDITOR','VIEWER') NOT NULL,
                                  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  FOREIGN KEY (page_id) REFERENCES Page(page_id) ON DELETE CASCADE,
                                  FOREIGN KEY (member_id) REFERENCES Member(member_id) ON DELETE CASCADE
);