### Why this project

A Java implementation for [jQuery-File-Upload](https://github.com/blueimp/jQuery-File-Upload).

This project is originally forked from [sudr/jQuery-File-Upload-Java](https://github.com/sudr/jQuery-File-Upload-Java), but rewrote all code:

1. Changed it to an IDEA project. Of course, you could use other IDE as well.
2. Introduce framework [Guice](http://code.google.com/p/google-guice/)
3. File location could be configured in property file app.properties
4. Introduce build tool [Gradle](http://www.gradle.org), and this project could run in an embedded Jetty plugin.

### How to run it

1. Install gradle on your machine, if you don't know how to install, please refer to [Gradle installation](http://www.gradle.org/installation)
2. Run `gradle jettyRun`, the app will start

