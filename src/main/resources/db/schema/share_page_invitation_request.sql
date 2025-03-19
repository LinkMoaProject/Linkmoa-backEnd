CREATE TABLE Share_Page_Invitation_Request (
                                               share_page_invite_request_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                               notification_type ENUM('TRANSMIT_DIRECTORY', 'INVITE_PAGE') NOT NULL,
                                               request_status ENUM('WAITING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'WAITING',
                                               permission_type ENUM('ADMIN','HOST','EDITOR','VIEWER') NOT NULL,
                                               sender_member_id BIGINT NOT NULL,
                                               receiver_member_id BIGINT NOT NULL,
                                               page_id BIGINT NOT NULL,
                                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                                               updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                               FOREIGN KEY (sender_member_id) REFERENCES Member(member_id) ON DELETE CASCADE,
                                               FOREIGN KEY (receiver_member_id) REFERENCES Member(member_id) ON DELETE CASCADE,
                                               FOREIGN KEY (page_id) REFERENCES Page(page_id) ON DELETE CASCADE
);
