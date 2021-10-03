# Data Catalog Asset Service

The asset handling service of the Data Catalog application.
For the API documentation, see the [API repository](https://github.com/data-catalog/datacatalog-asset-api).

## Requirements

For building and running the application you need:

- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11)
- [Gradle](https://gradle.org/)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `edu.bbte.projectbluebook.datacatalog.users.UserApplication` class from your IDE.

Alternatively you can use the [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/htmlsingle/) like so:

```shell
gradle bootRun
```

## Troubleshoot

If you encounter an `SSLHandshakeException` upon runnig the application, set the `jdk.tls.client.protocols=TLS1.2` parameter.
i.e. in IntelliJ: Run -> Edit Configurations -> Override parameters.

## Related projects:

The list of Data Catalog projects can be found [here](https://github.com/data-catalog).

- [User Service API](https://github.com/data-catalog/datacatalog-user-api)
- [Asset Service API](https://github.com/data-catalog/datacatalog-asset-api)
- [Versioning Service API](https://github.com/data-catalog/datacatalog-versioning-api)


- [User Service](https://github.com/data-catalog/datacatalog-user-service)
- [Asset Service](https://github.com/data-catalog/datacatalog-asset-service)
- [Versioning Service](https://github.com/data-catalog/datacatalog-versioning-service)


- [Web Frontend](https://github.com/data-catalog/datacatalog-frontend)
- [Python library](https://github.com/data-catalog/datacatalog-python-library)

### Deployed applications:

- [Web Frontend](https://datacatalogfrontend.azurewebsites.net/)
- [User Service](https://userhandlingservice.azurewebsites.net/)
- [Asset Service](https://assethandlingservice.azurewebsites.net/)
- [Versioning Service](https://versioningservice.azurewebsites.net/)

***Note**: The services sleep after 30 minutes fo inactivity. You may need to wait a couple of minutes for the applications to start.*
