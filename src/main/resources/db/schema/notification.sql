
CREATE TABLE Notification (
                              notification_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              notification_content VARCHAR(90) NOT NULL,
                              is_read BOOLEAN NOT NULL DEFAULT FALSE,
                              notification_type ENUM('TRANSMIT_DIRECTORY', 'INVITE_PAGE') NOT NULL,
                              request_id BIGINT,
                              sender_member_id BIGINT NOT NULL,
                              receiver_member_id BIGINT NOT NULL,
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                              updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              FOREIGN KEY (sender_member_id) REFERENCES Member(member_id) ON DELETE CASCADE,
                              FOREIGN KEY (receiver_member_id) REFERENCES Member(member_id) ON DELETE CASCADE
);