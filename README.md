# a todo microservice application built with [JHipster][]

This application demonstrates a minimal setup of an microservice application
scaffolded with [JDL][] using [JDL Studio][].

## How to build

To build the entire application you will need:

* Java 8 SDK
* [NodeJS][]
* npm
* bower
* gulp

To run everything on your unix/linux machine, you will need:

* [Docker][]
* [Docker Compose][]

In folders "myuaa", "mygateway" and "todo" peform

``` sh
./gradlew -x build -Pprod bootRepackage buildDocker
```

In folder "docker-compose" perform

``` sh
docker-compose up -d
```

And wait for some minutes while the virtual cloud is booting up.

## What's inside the box?

* The application running on http://localhost:8080
* service discovery and spring config server on http://localhost:8761 (U: admin, P: admin)
* [JHipster Console][] (Elasticsearch + Logstash + Kibana aka. ELK Stack) running on http://localhost:5601
* elasticsearch node avaible on http://localhost:9200

## Technologies used

* Spring Boot for all services and tests
* Spring Cloud and Netflix OSS for microservice architecture
* OAuth2 for service security
* AngularJS for frontend application
* PhantomJS for frontend tests
* npm and bower for asset dependency management
* gradle for backend build management
* gulp for frontend build management






[JHipster]: https://jhipster.github,io
[JDL]: https://jhipster.github.io/jdl/
[JDL Studio]: https://jhipster.github.io/jdl-studio/
[NodeJS]: https://nodejs.org/
[Docker]: https://www.docker.com/
[Docker Compose]: https://www.docker.com/products/docker-compose
[JHipster Console]: https://github.com/jhipster/jhipster-console
