# Akka HTTP example

This is a simple application made with `Akka HTTP`, `Slick` and `FlyWay`

*requirements:*

* sbt 0.13.8 or above

**database setup**

```
docker run -it --name postgresql -d -p 5432:5432 kiasaki/alpine-postgres:9.4
docker exec -it postgresql createdb -U postgres db_test
```

stop and remove database

```
docker rm -f postgresql
```

**run application**

```
$ sbt "run-main com.restapp.http.WebServer"
```

**some curls**

```
$ curl http://localhost:8080/values/other/path
$ curl http://localhost:8080/values/im/exists
```

**run tests**

```
$ sbt test
```
