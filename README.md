# Java Tracing Example

The project provides an example of distributed tracing using LogSense. The system contains out of three services:

* `service-a` (running on http://localhost:8081/service-a/)
* `service-b` (running on http://localhost:8082/service-b/)
* `service-c` (running on http://localhost:8083/service-c/ and http://localhost:8084/service-c/)

When doing a request to `service-a`, it's being passed to `service-b` and from there to one of the 
`service-c` instances. Each service is synchronous and handled via HttpClient. 

Each of the services responds to any resource name provided within it's path. 

`service-c` takes more time to respond if `foo` was provided as the `message` parameter to `service-a`. In such case,
it goes to Hazelcast and asks if a cached value was stored there. The cache is cleared when any other value is being 
queried for or


## Running


Each of the services needs to have instrumentation set. JVM needs to have following properties provided:

```
-Dlogsense.service.name=<NAME> -Dsa.tracer=logsense -Dlogsense.token=<YOUR TRACING TOKEN> -javaagent:logsense-opentracing-agent-1.1.9.jar
```

The addresses of the services can be controlled via `HOSTB=...`, `HOSTC=...` environment variables. Since the flow of
requests is `A->B->C`, `HOSTB` needs to be set on service A and `HOSTC` needs to be set on service B.

The recent version of agent might be pulled from: https://github.com/logsenseapp/logsense-opentracing-agent/releases/download/v1.1.9/logsense-opentracing-agent-1.1.9.jar

`service-a` might be called via e.g.:

```
 curl "http://localhost:8081/service-a/resource?message=foo"
```

(The first request will store the result into the cache and each subsequent will use it)

Or via:

```
 curl "http://localhost:8081/service-a/resource?message=abc"
```

(this will invalidate the cache)
