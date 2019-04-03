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


## Healthcheck API

You can confirm that the API is running by using the healthcheck URL. The URL is in the context path `/pricing/actuator/health`. Example :

```sh
http://localhost:8080/pricing/actuator/health
```

Once the application is running, you should see the following status :
```sh
{"status":"UP"}
```

Dillinger uses a number of open source projects to work properly:

* [AngularJS] - HTML enhanced for web apps!
* [Ace Editor] - awesome web-based text editor
* [markdown-it] - Markdown parser done right. Fast and easy to extend.
* [Twitter Bootstrap] - great UI boilerplate for modern web apps
* [node.js] - evented I/O for the backend
* [Express] - fast node.js network app framework [@tjholowaychuk]
* [Gulp] - the streaming build system
* [Breakdance](http://breakdance.io) - HTML to Markdown converter
* [jQuery] - duh

And of course Dillinger itself is open source with a [public repository][dill]
 on GitHub.

### Installation

Dillinger requires [Node.js](https://nodejs.org/) v4+ to run.

Install the dependencies and devDependencies and start the server.

```sh
$ cd dillinger
$ npm install -d
$ node app
```

For production environments...

```sh
$ npm install --production
$ NODE_ENV=production node app
```

### Plugins

Dillinger is currently extended with the following plugins. Instructions on how to use them in your own application are linked below.

| Plugin | README |
| ------ | ------ |
| Dropbox | [plugins/dropbox/README.md][PlDb] |
| Github | [plugins/github/README.md][PlGh] |
| Google Drive | [plugins/googledrive/README.md][PlGd] |
| OneDrive | [plugins/onedrive/README.md][PlOd] |
| Medium | [plugins/medium/README.md][PlMe] |
| Google Analytics | [plugins/googleanalytics/README.md][PlGa] |


### Development

Want to contribute? Great!

Dillinger uses Gulp + Webpack for fast developing.
Make a change in your file and instantanously see your updates!

Open your favorite Terminal and run these commands.

First Tab:
```sh
$ node app
```

Second Tab:
```sh
$ gulp watch
```

(optional) Third:
```sh
$ karma test
```
#### Building for source
For production release:
```sh
$ gulp build --prod
```
Generating pre-built zip archives for distribution:
```sh
$ gulp build dist --prod
```
### Docker
Dillinger is very easy to install and deploy in a Docker container.

By default, the Docker will expose port 8080, so change this within the Dockerfile if necessary. When ready, simply use the Dockerfile to build the image.

```sh
cd dillinger
docker build -t joemccann/dillinger:${package.json.version} .
```
This will create the dillinger image and pull in the necessary dependencies. Be sure to swap out `${package.json.version}` with the actual version of Dillinger.

Once done, run the Docker image and map the port to whatever you wish on your host. In this example, we simply map port 8000 of the host to port 8080 of the Docker (or whatever port was exposed in the Dockerfile):

```sh
docker run -d -p 8000:8080 --restart="always" <youruser>/dillinger:${package.json.version}
```

Verify the deployment by navigating to your server address in your preferred browser.

```sh
127.0.0.1:8000
```

#### Kubernetes + Google Cloud

See [KUBERNETES.md](https://github.com/joemccann/dillinger/blob/master/KUBERNETES.md)


### Todos

 - Write MORE Tests
 - Add Night Mode

License
----

MIT


**Free Software, Hell Yeah!**

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [dill]: <https://github.com/joemccann/dillinger>
   [git-repo-url]: <https://github.com/joemccann/dillinger.git>
   [john gruber]: <http://daringfireball.net>
   [df1]: <http://daringfireball.net/projects/markdown/>
   [markdown-it]: <https://github.com/markdown-it/markdown-it>
   [Ace Editor]: <http://ace.ajax.org>
   [node.js]: <http://nodejs.org>
   [Twitter Bootstrap]: <http://twitter.github.com/bootstrap/>
   [jQuery]: <http://jquery.com>
   [@tjholowaychuk]: <http://twitter.com/tjholowaychuk>
   [express]: <http://expressjs.com>
   [AngularJS]: <http://angularjs.org>
   [Gulp]: <http://gulpjs.com>

   [PlDb]: <https://github.com/joemccann/dillinger/tree/master/plugins/dropbox/README.md>
   [PlGh]: <https://github.com/joemccann/dillinger/tree/master/plugins/github/README.md>
   [PlGd]: <https://github.com/joemccann/dillinger/tree/master/plugins/googledrive/README.md>
   [PlOd]: <https://github.com/joemccann/dillinger/tree/master/plugins/onedrive/README.md>
   [PlMe]: <https://github.com/joemccann/dillinger/tree/master/plugins/medium/README.md>
   [PlGa]: <https://github.com/RahulHP/dillinger/blob/master/plugins/googleanalytics/README.md>
