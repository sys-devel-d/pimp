# Pimp - Backend

## Initial Setup
- Install Maven.
- Install MongoDB.
- Clone repo.

## Prestart
- Run mongo instance.
- ```mongod```

### Restore database with example data
- Go to mongo_dump/
- ``` mongorestore -d pimp pimp```

## Run without tests in one step
- Go to project directory.
- ``` mvn spring-boot:run```

## Build and Run with tests
- Go to project directory.
- ``` mvn clean install```
- ``` java -jar target/pimp-rest-[ARTIFACT-VERSION].jar```

## Run Tests only
- Go to project directory.
- ``` mvn clean verify```
