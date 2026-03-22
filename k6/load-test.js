import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '15s', target: 10 },
        { duration: '30s', target: 50 },
        { duration: '30s', target: 100 },
        { duration: '15s', target: 0 }
    ],
    thresholds: {
        http_req_duration: ['p(95)<2000'],
        http_req_failed: ['rate<0.01']
    }
};

const url = 'http://localhost:8090/api/v1/events'
const EVENT_TYPES = ['BUILD_FAILED', 'DEPLOY_COMPLETED', 'MONITORING_ALERT'];


export default function () {
    const idempotencyKey = `k6-${__VU}-${__ITER}`
    const eventType = EVENT_TYPES[Math.floor(Math.random() * EVENT_TYPES.length)];
    const payload= JSON.stringify({
        source: 'k6-load-test',
        eventType: eventType,
        payload: 'test-message'
    })
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'idempotency-key': idempotencyKey
        }
    }

    const res = http.post(url, payload, params);

    check(res, {
        'status is 202': (r) => r.status === 202
    });

    sleep(0.1 + Math.random() * 0.3);
}