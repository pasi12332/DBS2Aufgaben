# Build
```
mvn clean package
```

# System Test

The application (demo-app) should be up and running

```
mvn compile test failsafe:integration-test -pl :demo-st
```
