# V2 API Test Skeleton

Run all v2 examples:

```bash
mvn -Dtest=v2.tests.* test
```

Override target environment:

```bash
mvn -Dtest=v2.tests.* test -Dv2.baseUri=http://localhost -Dv2.port=8080 -Dv2.username=admin -Dv2.password=admin
```

Or use environment variables:

- `V2_BASE_URI`
- `V2_PORT`
- `V2_USERNAME`
- `V2_PASSWORD`

Core packages:

- `v2.config`: environment and credential config
- `v2.core`: api client and auth session
- `v2.api`: api wrappers
- `v2.tests`: example test cases
