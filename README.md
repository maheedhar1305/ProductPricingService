# myRetail RESTful service 

myRetail RESTful service is a Spring Boot application written in Java. It is a RESTful service that can retrieve product details and price by ID. Authorized clients can also publish price information to the application. 

Please refer to the [API documentation](#3-api-specification) for detailed specification of the application's API.

## 1. Quickstart

 There are 3 *options* to quickly access and use the application. They are :
  - Download the source code and build the binary. (Using **Gradle**)
  - Download and run, a publicly available Docker image. (Using **Docker**)
  - Use the Kubernetes script to deploy the application to a cluster. (Using a **Kubernetes** solution of your choice, such as Google Kubernetes Engine)

### Pre-requisites

In order to successfully run the application, the following configuration is required.
 - *[Required]* A mongo connection URI for the application to use, to communicate with a mongo database.
 - *[Required]* Credentials (username / password) to be used by client applications that want to publish data to the application's API.

### 1.1 Using Gradle
Using Gradle you can build the JAR file from the source code and run the executable in an environment of your choice.

Steps to run using Gradle

1. Download the source code.
2. In a CLI of your choice, navigate to the base directory of the source code. This is the folder with the `build.gradle` file in it.
3. Run ` gradle build`. This builds the JAR file in the path : `<source_directory>/build/libs/`.
4. Use the environment variable `MONGO_CONNECTION_URI` to set the mongo connection URI to be used by the application. [Here](https://docs.mongodb.com/manual/reference/connection-string/) are the instructions on how to define a mongo URI. 
5. Use the environment variables `ADMIN_NAME` and `ADMIN_PWD` to set the username and password of your choice. These credentials need to be used by all client applications who want to publish data to the application. (via the PUT command)
Example :
```sh
$ export MONGO_CONNECTION_URI="mongodb://user:password@localhost:27017/pricing"
$ export ADMIN_NAME="admin"
$ export ADMIN_PWD="adminPwd"
```
6. Start the application and use the [Healthcheck API](#21-healthcheck-api) to verify that the application is running.
```sh
$ java -jar build/libs/ProductPricingService.jar
```

### 1.2 Using Docker

Steps to run using Docker

1. A Docker image has been created for the latest stable version of the application. This *publicly available* image can be found [here](https://cloud.docker.com/u/maheeedhar1010/repository/docker/maheeedhar1010/product-pricing-service). You can use docker tool to pull the image on to your environment.
```sh
$ docker pull maheeedhar1010/product-pricing-service:1.0.0
```
2. Once the image has been downloaded to your machine, you can start the application in a Docker container using the command given below. We need to pass the `MONGO_CONNECTION_URI`, `ADMIN_NAME`, `ADMIN_PWD` arguments for the application as described in the [pre-requisites](#pre-requisites) section. The application will be started as a daemon.
```sh
$ docker run -d -p 8080:8080 \
-e MONGO_CONNECTION_URI="mongodb://price:password@192.168.1.13:27017/pricing" \
-e ADMIN_NAME="admin" \
-e ADMIN_PWD="adminPwd"  \
maheeedhar1010/product-pricing-serice:1.0.0
```
3. Use the [Healthcheck API](#21-healthcheck-api) to verify that the application is running.

### 1.3 Using Kubernetes

The Kubernetes option is the most **Prodution-Ready** option of all the 3 options discussed here, as it is accompanied by many enterprise standard features such as **Horizontal scaling**, **high availability**. Please refer to [this section](#22-kubernetes) for a deep dive into the features of Kubernetes, that has been leveraged for this application.

Steps to run using Kubernetes

1. Edit the [product-config.yaml](/product-config.yaml) file to configure the parameters mentioned in the [pre-requisites](#pre-requisites) section.
2. Deploy the configuration to a kubernetes cluster.
```sh
$ kubectl create -f product-config.yaml
```
3. Deploy the application to the kubernetes cluster using the kubernetes descriptor [product-pricing-service.yaml](/product-pricing-service.yaml) file. 
```sh
$ kubectl create -f product-pricing-service.yaml
```
4. To allow traffic into the service, you will have to configure a kubernetes ingress, the scope of which is beyond our context. But for *testing purposes*, we can use the following command to port forward to the service and confirm that the application is running using the [Healthcheck API](#21-healthcheck-api).

```sh
$ kubectl port-forward svc/product-pricing-service 8080:8080
```

## 2. Features
### 2.1 Healthcheck API

You can confirm that the API is running by using the healthcheck URL. The URL is in the context path `/pricing/actuator/health`. Example :

```sh
http://localhost:8080/pricing/actuator/health
```

Once the application is running, you should see the following status :
```sh
{"status":"UP"}
```

Please refer to the [API documentation](#3-api-specification) for more detailed specification of the application's API.

### 2.2 Kubernetes

Here are some brief pointers about the resources defined in the application's [kubernetes descriptor file](/product-pricing-service.yaml), and the significance of their role in a production environment.

**Deployment** 
- The Application runs inside a kubernetes deployment named "product-service-deployment". The Deployment will be a cluster of 3 pods servicing the Traffic.
- By defining deployments for our application container, we get features such as *High availability, Efficient resource usage, Load balancing*.

**Service**
- Exposes our deployment to outside world. This is to be configured with the load balancer in order to allow traffic to flow in to the application.

**ConfigMap**
- We can configure the properties mentioned in the [pre-requisites](#pre-requisites) section.

**HorizontalPodAutoscaler**
- Makes our application scalable.
- Monitors the resource usage of our application and when it crosses the average threshold, spins up more replicas to distribute the load and service requests faster without causing disruption. 

Here is a brief description of how the resources would look like when deployed in a kubernetes cluster :

![Kubernetes resources deployed for the application](/extras/assets/documentation/k8sResources.png)

### 2.3 Unit test suite

The Unit test suite encompasses testing all modules of the application. In addition, it has also been configured to run every time `gradle build` is ran. It has been configured in a way that a build will take place only if all the tests run successfully. This is useful when we want to configure a *CI/CD pipleine* or a procedure to automate build every time, new code is checked in.

This application uses the following frameworks for unit testing
- *Spock (Groovy)* - To test business logic in services and DAO.
- *Mockito and Spring-test* - To test spring components.

![Unit test suite](/extras/assets/documentation/unittest.png)

### 2.4 Meaningful API error messages

- It is important that when there is a failure in the application, we communicate the reason for failure in such a manner that clients are able to troubleshoot and fix the issues on their end. To this effect, the application has a robust and extendable error handling mechanism in place which will handle any failures in the application and accurately convey the reason for failure to the client. 
- Also,  HTTP Error codes are used to depict the nature of the failures.

Here are a few examples :

When the PUT method fails due to some validation error, the Error message accurately conveys the list of validation errors for the client to fix          |  When the GET method failed to fetch an entity, it describes what is missing
:-------------------------:|:-------------------------:
![example-1](/extras/assets/documentation/putError.png)  |  ![example-2](/extras/assets/documentation/getError.png)

### 2.5 Mongo index

Creating indices in mongo helps with the performance of the application as mongo queries have faster response times when the queried fields are indexed. In our case, we use `@Indexed(unique=true)` annotation , in the [Price](/src/main/java/com/myretail/pricingservice/domain/Price.java) domain object, to ensure that the application creates an index, whenever it connects to a mongo instance.

The index is created for the `productId` field and it is declared as unique. This way we ensure duplicate records are not created and also improve query response times.

![mongoIndex.png](/extras/assets/documentation/mongoIndex.png) 

### 2.6 Secure API

Since the application maintains a database that multiple client applications consume, publishers of information need to be authorized in order to update our database. We achieve this in our application by creating a set of credentials which need to be used by any client that wants to publish information to our API. Any unauthorized requests would fail.

Unauthorized requests fail with a HTTP 401 status  |  Authorized requests update the database
:-------------------------:|:-------------------------:
![noauth](/extras/assets/documentation/noauth.png)  |  ![auth](/extras/assets/documentation/auth.png)

## 3. API Specification

The application uses OpenAPI 3.0 Specification to describe its API. [Reference](/openapi.yaml)

GET   |  PUT
:-------------------------:|:-------------------------:
![unauth](/extras/assets/documentation/getApi.png)  |  ![auth](/extras/assets/documentation/putApi.png)
## 4. TODO
### Performance testing
It is always recommended to do performance testing in order to estimate the limits of the system. It will also help us identify performance bottlenecks if any and address them effectively.
### CI/CD pipeline
The application basically has all the tools built in to implement a CICP pipeline. Such an implementation would :
- Run the [Unit test suite](#23-unit-test-suite).
- If success, use [Gradle](#11-using-gradle) to build JAR.
- Create a [Docker image](#12-using-docker) with the newly built JAR. Tag the image with a new version.
- Deploy the latest version of the application to [Kubernetes](#13-using-kubernetes).
### Robust API security
Have a more robust security mechanism for clients trying to publish data to our API. Protocols such as OAuth can be used to provide a more extensible solution.
### Fine tune kubernetes resource requests/limits
The Goal of Kubernetes is to efficiently use resources and not under utilize them. Having taken a conservative approach in providing a 1CPU ~4GB machine to run the application in kubernetes initially, we need to tune the requests based on average load over a period of time. This way we can ensure the cluster is sized sufficiently to satisfy average load and whenever there is a peak, it can horizontally scale up for a brief amount of time. 