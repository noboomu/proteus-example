# Proteus Example Project
=====================

This project demonstrates a number of Proteus's features.

[Pac4j](https://github.com/pac4j/pac4j) integration with Github OAuth is included.

All configuration is done through the `application.conf` configuration file.

Getting Started
----------------

> the following assumes the default application.conf was not modified.

- Make sure [maven][apache_maven] is installed on your system.
- Run `mvn package exec:exec` in the root directory.
- Open [http://localhost:8090/v1/openapi](http://localhost:8090/v1/openapi) in your browser.



Hot Reloading
----------------
Make sure [entr][entr] is installed.

Run `./scripts/run.sh`.

[entr]: http://eradman.com/entrproject/
[apache_maven]: http://maven.apache.org/install.html
