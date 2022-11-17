# Spring Cloud Demo on Kubernetes

Spring Cloud application on Kubernetes demo.

## Demo service APIs
We have two services: book and rating.

[Rating service](rating-service/src/main/java/com/example/ratingservice/RatingServiceController.java):

| API  | Result | Note |
| --- | --- | --- |
| / | "rating service ok" | health check |
| /ratings/all | [{"id":1,"bookId":1,"stars":2},{"id":2,"bookId":1,"stars":3},{"id":3,"bookId":2,"stars":4},{"id":4,"bookId":2,"stars":5}] | all ratings |
| /ratings?bookid=1 | [{"id":1,"bookId":1,"stars":2},{"id":2,"bookId":1,"stars":3}] | |

[Book service](book-service/src/main/java/com/example/bookservice/BookServiceController.java):
| API  | Result | Note |
| --- | --- | --- |
| / | "book service ok" | health check |
| /books | [{"id":1,"author":"Spring Cloud Book","title":"John Doe"},{"id":2,"author":"Kubernetes Book","title":"Zhang San"}] |
| /books/1 | {"id":1,"author":"Spring Cloud Book","title":"John Doe"} |
| /books/1/ratings | [{"id":1,"bookId":1,"stars":2},{"id":2,"bookId":1,"stars":3}] | fetch from rating service |
| /pingratingsvc | "rating service ok" | ping rating service |

## Run services using docker compose
### Build docker images
```
➜  ./mvnw clean package
➜  cd compose
➜  docker compose build
```

### Run services

In [compose](compose) directory
```
➜  docker compose up -d
```

Both services should up and running, status like below:
```
➜  docker compose ps
NAME                   COMMAND                  SERVICE             STATUS              PORTS
compose-book-svc-1     "sh -c 'java $JAVA_O…"   book-svc            running (healthy)   0.0.0.0:8081->8080/tcp
compose-rating-svc-1   "sh -c 'java $JAVA_O…"   rating-svc          running (healthy)   0.0.0.0:8082->8080/tcp
```
### Test services

```
➜  curl localhost:8081/books/1/ratings
[{"id":1,"bookId":1,"stars":2},{"id":2,"bookId":1,"stars":3}]
```

Verify liveness and readness probes
```
➜  curl localhost:8081/actuator/health/readiness
{"status":"UP"}%
➜  curl localhost:8082/actuator/health/liveness
{"status":"UP"}%
```
## Deploy to Kubernertes

### Create a Kind cluster

Crate a Kind cluster with nginx ingress follow this guide: [https://kind.sigs.k8s.io/docs/user/ingress/#ingress-nginx](https://kind.sigs.k8s.io/docs/user/ingress/#ingress-nginx)

Change to [kind/cluster](kind/cluster/) directory:
```
➜  cat kind-cluster.yaml | kind create cluster --config=-
➜  kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
```

### Load docker images

```
➜  kind load docker-image rating-service:0.0.1
➜  kind load docker-image book-service:0.0.1
```

In [kind](kind) directory
```
➜  kubectl apply -f app
deployment.apps/book-deployment created
service/book-service created
configmap/booksvc-cm created
ingress.networking.k8s.io/spring-cloud-demo-ingress created
deployment.apps/rating-deployment created
service/rating-service created
```

Verify two services up and running:
```
➜  curl localhost/books/1/ratings
[{"id":1,"bookId":1,"stars":2},{"id":2,"bookId":1,"stars":3}]
```

## Reference Documentation

* [Spring on Kubernetes! workshop](https://hackmd.io/@ryanjbaxter/spring-on-k8s-workshop)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.5/maven-plugin/reference/html/#build-image)

