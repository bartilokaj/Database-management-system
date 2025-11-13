## Running project
Project is done in java 25, but any sdk22+ should work as well.
### Intelij
Intelij should detect the maven file and allow to run without issues with 
`mvn clean compile exec:java`.
Alternatively, you can navigate to run->edit-configurations and in Main (you may need to run it once and fail to see
it there), select modify-options->Vm-options, and add
`--enable-preview --add-modules jdk.incubator.vector`
### Without Intelij
You need to have installed java 22+ and have it set as JAVA_HOME environmental variable. Also you need to install [maven](https://maven.apache.org/install.html).
Then again, `mvn clean compile exec:java` should work.
