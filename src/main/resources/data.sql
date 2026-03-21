-- 구독 시드 데이터 (테스트용)
INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at) VALUES
('user-100', 'BUILD_FAILED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-100', 'BUILD_FAILED', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()),
('user-200', 'BUILD_FAILED', 'WEBHOOK', 'https://example.com/webhook', 'ACTIVE', NOW(), NOW());
