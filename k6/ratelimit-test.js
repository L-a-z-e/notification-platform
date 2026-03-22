import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const accepted = new Counter('responses_202');
const rateLimited = new Counter('responses_429');
const other = new Counter('responses_other');

const url = 'http://localhost:8090/api/v1/events';

export const options = {
    scenarios: {
        ratelimit_test: {
            executor: 'constant-arrival-rate',
            rate: 5,
            timeUnit: '1s',
            duration: '30s',
            preAllocatedVUs: 10,
        },
    },
};

export default function () {
    const idempotencyKey = `rl-${__VU}-${__ITER}-${Date.now()}`;
    const payload = JSON.stringify({
        source: 'k6-ratelimit-test',
        eventType: 'BUILD_FAILED',
        payload: 'rate limit test',
    });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'idempotency-key': idempotencyKey,
            'X-Client-Id': 'test-client',
        },
    };

    const res = http.post(url, payload, params);

    if (res.status === 202) {
        accepted.add(1);
    } else if (res.status === 429) {
        rateLimited.add(1);
    } else {
        other.add(1);
    }

    check(res, {
        'status is 202 or 429': (r) => r.status === 202 || r.status === 429,
        'has Retry-After when 429': (r) =>
            r.status !== 429 || r.headers['Retry-After'] !== undefined,
    });
}
