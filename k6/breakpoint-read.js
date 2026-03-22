import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 500 },
        { duration: '30s', target: 1000 },
        { duration: '30s', target: 2000 },
        { duration: '30s', target: 3000 },
        { duration: '30s', target: 5000 },
    ],
    thresholds: {
        http_req_failed: [{ threshold: 'rate<0.01', abortOnFail: true }],
        http_req_duration: [{ threshold: 'p(95)<100', abortOnFail: true }],
    },
};

const BASE_URL = 'http://localhost:8090';

export default function () {
    const userId = `user-${String(Math.floor(Math.random() * 1500) + 1).padStart(4, '0')}`;

    const res = http.get(
        `${BASE_URL}/api/v1/notifications?userId=${userId}&pageSize=20`
    );

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(0.1 + Math.random() * 0.2);
}
