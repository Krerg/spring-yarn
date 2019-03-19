Build the Application
For gradle simply execute the clean and build tasks.

./gradlew clean build
To skip existing tests if any:

./gradlew clean build -x test
For maven simply execute the clean and package goals.

mvn clean package
To skip existing tests if any:

mvn clean package -DskipTests=true
Below listing shows files after a succesfull gradle build.

gs-yarn-basic-dist/target/gs-yarn-basic-dist/gs-yarn-basic-client-0.1.0.jar
gs-yarn-basic-dist/target/gs-yarn-basic-dist/gs-yarn-basic-appmaster-0.1.0.jar
gs-yarn-basic-dist/target/gs-yarn-basic-dist/gs-yarn-basic-container-0.1.0.jar
Run the Application
Now that you’ve successfully compiled and packaged your application, it’s time to do the fun part and execute it on Hadoop YARN.

To accomplish this, simply run your executable client jar from the projects root dirctory.


$ java -jar gs-yarn-basic-dist/target/gs-yarn-basic-dist/gs-yarn-basic-client-0.1.0
