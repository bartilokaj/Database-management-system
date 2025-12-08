## Running Project
Requires maven, docker and java 21+. If you encounter problems with enable-preview
features, you can try to set target in maven-compiler-plugin to your SDK version.
## Makefile
Makefile has target docker, which creates an image, also has target docker-run
with container creation, which binds server to port 7000 and binds the data directory
from the root to the corresponding docker directory, so that container has preserved metastore state.
## Tests
Tests are written, however I do not vouch that they will work. I primarely used Intelij on windows,
and during development I adjusted pom.xml to my environment. To run use `mvn clean compile test
`