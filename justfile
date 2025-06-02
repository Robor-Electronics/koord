# Build the project
build:
    ./gradlew build

# Run all checks
check:
    ./gradlew check

# Generate coverage reports
coverage:
    ./gradlew koverXmlReport koverHtmlReport

# Apply linting and formatting rules
spotless:
    ./gradlew spotlessApply

clean:
    ./gradlew clean
