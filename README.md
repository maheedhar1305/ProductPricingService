# myRetail RESTful service 

myRetail RESTful service is a Spring Boot application written in Java. 

## 1. Quickstart

 Here is how you can quickly get started on working with the app. You can :
  - Download the source code and build the binary (Using gradle)
  - Download a publicly available Docker hub image and simply run it (Using Docker)
  - Use the Kubernetes script to deploy the image to a cluster (Using a Kubernetes solution of your choice, example : Google Kubernetes Engine)

### 1.0 Pre-requisites

In order to successfully run the application, following configuration is required
 - *[Required]* Providing a mongo connection URI for the application to use, to communicate with a mongo database
 - *[Required]* Admin user name and password to be used by client applications that want to publish data to the application's API.

### 1.1 Using Gradle
Using Gradle you can build the JAR file from the source code yourself and run the executable in an environment of your choice. Here are the steps to do that

1. Download the source code
2. In a CLI of your choice, navigate to the base directory of the source code. This is the folder with the build.gradle file in it 
3. Run ` gradle build`. This builds the JAR file in the path : `<source_directory>/build/libs/`
4. Use the environment variable `MONGO_CONNECTION_URI` to set the mongo connection URI to be used by the application. Here are the [instructions](https://docs.mongodb.com/manual/reference/connection-string/) for how to define a mongo URI. 
5. Use the environment variables `ADMIN_NAME` and `ADMIN_PWD` to set the username and password you want to use. These credentials need to be used by all client applications who want to publish data to the application. (via the PUT command)
Example :
```sh
$ export MONGO_CONNECTION_URI="mongodb://user:password@localhost:27017/pricing"
$ export ADMIN_NAME="admin"
$ export ADMIN_PWD="adminPwd"
```
6. Start the application and use the [Healthcheck API](#Healthcheck-API) to verify that the application is up.
```sh
$ java -jar build/libs/ProductPricingService.jar
```

### 1.2 Using Docker

1. I have created a Docker image for a stable version of the application. This *publicly available* image can be found [here](https://cloud.docker.com/u/maheeedhar1010/repository/docker/maheeedhar1010/product-pricing-service). You can use docker tool to pull the image on to your environment.
```sh
$ docker pull maheeedhar1010/product-pricing-service:1.0.0
```
2. Once the image has been downloaded to your machine, you can start the application in a Docker container using the following command. We need to pass the `MONGO_CONNECTION_URI`, `ADMIN_NAME`, `ADMIN_PWD` arguments for the application as described in the [pre-requisites](#10-pre-requisites) section. The application will be started as a daemon
```sh
docker run -d -p 8080:8080 \
-e MONGO_CONNECTION_URI="mongodb://price:password@192.168.1.13:27017/pricing" \
-e ADMIN_NAME="admin" \
-e ADMIN_PWD="adminPwd"  \
maheeedhar1010/product-pricing-serice:1.0.0
```
3. Use the [Healthcheck API](#20-healthcheck-api) to verify that the application is up.

### 1.3 Using Kubernetes

The Kubernetes option is the most **Prodution-Ready** option of all the 3 options mentioned here as it comes with many enterprise level features such as Horizontal scaling

## 2. Features
### 2.0 Healthcheck API

You can confirm that the API is running by using the healthcheck URL. The URL is in the context path `/pricing/actuator/health`. Example :

```sh
http://localhost:8080/pricing/actuator/health
```

Once the application is running, you should see the following status :
```sh
{"status":"UP"}
```
