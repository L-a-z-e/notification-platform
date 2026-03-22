import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 50 },
        { duration: '30s', target: 100 },
        { duration: '30s', target: 200 },
        { duration: '30s', target: 300 },
        { duration: '30s', target: 500 },
        { duration: '30s', target: 700 },
        { duration: '30s', target: 1000 },
    ],
    thresholds: {
        http_req_failed: [{ threshold: 'rate<0.01', abortOnFail: true }],
        http_req_duration: [{ threshold: 'p(95)<500', abortOnFail: true }],
    },
};

const url = 'http://localhost:8090/api/v1/events';
const EVENT_TYPES = ['BUILD_FAILED', 'DEPLOY_COMPLETED', 'MONITORING_ALERT'];

export default function () {
    const idempotencyKey = `bp-${__VU}-${__ITER}-${Date.now()}`;
    const eventType = EVENT_TYPES[Math.floor(Math.random() * EVENT_TYPES.length)];
    const payload = JSON.stringify({
        source: 'k6-breakpoint',
        eventType: eventType,
        payload: 'breakpoint test message',
    });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'idempotency-key': idempotencyKey,
        },
    };

    const res = http.post(url, payload, params);

    check(res, {
        'status is 202': (r) => r.status === 202,
    });

    sleep(0.1 + Math.random() * 0.2);
}
