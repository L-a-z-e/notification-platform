-- 구독 시드 데이터 (현실적 수준 1,500건)
-- BUILD_FAILED: 500건 (SLACK 250 + EMAIL 250)
-- DEPLOY_COMPLETED: 500건 (SLACK 250 + WEBHOOK 250)
-- MONITORING_ALERT: 500건 (EMAIL 250 + SLACK 250)

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT 'user-' || LPAD(i::text, 4, '0'), 'BUILD_FAILED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()
FROM generate_series(1, 250) AS i;

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT 'user-' || LPAD((i + 250)::text, 4, '0'), 'BUILD_FAILED', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()
FROM generate_series(1, 250) AS i;

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT 'user-' || LPAD((i + 500)::text, 4, '0'), 'DEPLOY_COMPLETED', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()
FROM generate_series(1, 250) AS i;

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT 'user-' || LPAD((i + 750)::text, 4, '0'), 'DEPLOY_COMPLETED', 'WEBHOOK', 'https://example.com/hook/' || (i + 750), 'ACTIVE', NOW(), NOW()
FROM generate_series(1, 250) AS i;

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT 'user-' || LPAD((i + 1000)::text, 4, '0'), 'MONITORING_ALERT', 'EMAIL', NULL, 'ACTIVE', NOW(), NOW()
FROM generate_series(1, 250) AS i;

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT 'user-' || LPAD((i + 1250)::text, 4, '0'), 'MONITORING_ALERT', 'SLACK', NULL, 'ACTIVE', NOW(), NOW()
FROM generate_series(1, 250) AS i;
