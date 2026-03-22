-- 구독 시드 데이터 (최소 18건)
-- BUILD_FAILED: 6건 (SLACK 3 + EMAIL 3)
-- DEPLOY_COMPLETED: 6건 (SLACK 3 + WEBHOOK 3)
-- MONITORING_ALERT: 6건 (EMAIL 3 + SLACK 3)

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at) VALUES
('user-001', 'BUILD_FAILED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-002', 'BUILD_FAILED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-003', 'BUILD_FAILED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-004', 'BUILD_FAILED', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()),
('user-005', 'BUILD_FAILED', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()),
('user-006', 'BUILD_FAILED', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()),
('user-007', 'DEPLOY_COMPLETED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-008', 'DEPLOY_COMPLETED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-009', 'DEPLOY_COMPLETED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-010', 'DEPLOY_COMPLETED', 'WEBHOOK', 'https://example.com/hook/10', 'ACTIVE', NOW(), NOW()),
('user-011', 'DEPLOY_COMPLETED', 'WEBHOOK', 'https://example.com/hook/11', 'ACTIVE', NOW(), NOW()),
('user-012', 'DEPLOY_COMPLETED', 'WEBHOOK', 'https://example.com/hook/12', 'ACTIVE', NOW(), NOW()),
('user-013', 'MONITORING_ALERT', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()),
('user-014', 'MONITORING_ALERT', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()),
('user-015', 'MONITORING_ALERT', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()),
('user-016', 'MONITORING_ALERT', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-017', 'MONITORING_ALERT', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()),
('user-018', 'MONITORING_ALERT', 'SLACK', NULL, 'ACTIVE', NOW(), NOW());
