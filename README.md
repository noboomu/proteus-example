# Proteus Example Project
=====================

This project demonstrates a number of Proteus's features.

All configuration is done through the `application.conf` configuration file.

Getting Started
----------------

> the following assumes the default application.conf was not modified.

- Make sure [maven][apache_maven] is installed on your system.
- Run `mvn package exec:exec` in the root directory.
- Open [http://localhost:8090/v1/swagger](http://localhost:8090/v1/swagger) in your browser.
- Open [http://localhost:8090/v1/swagger/redoc](http://localhost:8090/v1/swagger/redoc) for a pretty version of your API.



Hot Reloading
----------------
Make sure both [Watchman][facebook_watchman] and the [pywatchman python package][pywatchman] are installed.

Run `./scripts/run.sh`.

[facebook_watchman]: https://facebook.github.io/watchman/docs/install.html
[pywatchman]: https://pypi.org/project/pywatchman/
[apache_maven]: http://maven.apache.org/install.html