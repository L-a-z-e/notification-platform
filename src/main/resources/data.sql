-- Event 시드 데이터
INSERT INTO events (idempotency_key, source, event_type, payload, created_at) VALUES
('seed-001', 'jenkins', 'BUILD_FAILED', 'PR #42 빌드 실패', NOW()),
('seed-002', 'monitoring', 'ALERT_TRIGGERED', 'CPU 사용률 90% 초과', NOW());

-- Notification 시드 데이터 (event_id=1 기준, user-123에게 발송)
INSERT INTO notifications (event_id, user_id, channel, message, status, error_message, retry_count, created_at, sent_at, updated_at) VALUES
(1, 'user-123', 'SLACK', 'PR #42 빌드 실패', 'SENT', NULL, 0, NOW(), NOW(), NOW()),
(1, 'user-123', 'EMAIL', 'PR #42 빌드 실패', 'FAILED', '이메일 서버 타임아웃', 3, NOW(), NULL, NOW()),
(1, 'user-456', 'SLACK', 'PR #42 빌드 실패', 'SENT', NULL, 0, NOW(), NOW(), NOW()),
(2, 'user-123', 'SLACK', 'CPU 사용률 90% 초과', 'QUEUED', NULL, 0, NOW(), NULL, NOW()),
(2, 'user-123', 'WEBHOOK', 'CPU 사용률 90% 초과', 'SENT', NULL, 0, NOW(), NOW(), NOW());
