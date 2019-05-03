# Java Tracing Example

The project provides an example of distributed tracing using LogSense. The system contains out of three services:

* `service-a` (running on http://localhost:8081/service-a/)
* `service-b` (running on http://localhost:8082/service-b/)
* `service-c` (running on http://localhost:8083/service-c/)

When doing a request to `service-a`, it's being passed to `service-b` and from there to `service-c`. Each service is 
synchronous and handled via HttpClient. 

Each of the services responds to any resource name provided within it's path. 

`service-c` takes more time to respond if `foo` was provided as the `message` parameter to `service-a`.


## Running


Each of the services needs to have instrumentation set. JVM needs to have following properties provided:

```
 -Dsa.tracer=logsense -Dlogsense.token=<YOUR TRACING TOKEN> -javaagent:logsense-opentracing-agent-1.1.1.jar

```

The recent version of agent might be pulled from: https://github.com/collectivesense/logsense-opentracing-agent/releases/download/v1.1.1/logsense-opentracing-agent-1.1.1.jar

`service-a` might be called via e.g.:

```
 curl "http://localhost:8081/service-a/resource?message=foo"
 ```
