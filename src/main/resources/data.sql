-- 구독 시드 데이터 (부하 테스트용 50,000건)

-- BUILD_FAILED: 20,000건 (SLACK 10,000 + EMAIL 10,000)
INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT
    'user-' || LPAD(i::text, 6, '0'),
    'BUILD_FAILED',
    'SLACK',
    NULL,
    'ACTIVE',
    NOW(),
    NOW()
FROM generate_series(1, 10000) AS i;

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT
    'user-' || LPAD((i + 10000)::text, 6, '0'),
    'BUILD_FAILED',
    'EMAIL',
    NULL,
    'ACTIVE',
    NOW(),
    NOW()
FROM generate_series(1, 10000) AS i;

-- DEPLOY_COMPLETED: 15,000건 (SLACK 7,500 + WEBHOOK 7,500)
INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT
    'user-' || LPAD((i + 20000)::text, 6, '0'),
    'DEPLOY_COMPLETED',
    'SLACK',
    NULL,
    'ACTIVE',
    NOW(),
    NOW()
FROM generate_series(1, 7500) AS i;

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT
    'user-' || LPAD((i + 27500)::text, 6, '0'),
    'DEPLOY_COMPLETED',
    'WEBHOOK',
    'https://example.com/hook/' || (i + 27500),
    'ACTIVE',
    NOW(),
    NOW()
FROM generate_series(1, 7500) AS i;

-- MONITORING_ALERT: 15,000건 (EMAIL 7,500 + SLACK 7,500)
INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT
    'user-' || LPAD((i + 35000)::text, 6, '0'),
    'MONITORING_ALERT',
    'EMAIL',
    NULL,
    'ACTIVE',
    NOW(),
    NOW()
FROM generate_series(1, 7500) AS i;

INSERT INTO subscriptions (user_id, event_type, channel, webhook_url, status, created_at, updated_at)
SELECT
    'user-' || LPAD((i + 42500)::text, 6, '0'),
    'MONITORING_ALERT',
    'SLACK',
    NULL,
    'ACTIVE',
    NOW(),
    NOW()
FROM generate_series(1, 7500) AS i;
