# I1

## I1 Introduction
### What Is I1

If we start a new Java project for enterprise usage, we probably need to develop from scratch even for some commonly used functions. Why do we bother to do that again since people have done that for hundreds of times? So the initial idea of I1 is to collect these "commonly used functions" to prevent reinventing the wheels.

The name "I1" means that these functions are so common that they are normally developed from the very first iteration, if you have agile experience.

### How to use I1

I1 could be used in various ways:

* **Standalone web server**
  I1 module could be packaged to a war and deployed as a standalone web server.

* **Library**
  I1 module could be packaged to a jar file and imported in your project.

* **Copy and paste**
  Of course you could copy and paste code from I1 into your own project.

### I1 Philosophy
* **Same technical stacks**
  To make things easy we use the same technical stacks for all I1 modules: Guice+Jersey+Guava
* **Similiar functions**
  For each I1 module we provide similiar functions, including:
  Be packaged to jar; Be packaged to war; Run as an embedded web server; Admin page for administration usage

## I1 Modules

So far we have developed several of them:

* **i1-email-sender**
  It's not a smtp server, but a web application that could be used to send email with an existing smtp server. It provides admin pages to monitor the emails status.

* **i1-file-management**
  A web application that could be used to upload files and manage files. It also provides some client JavaScript sample code for usage.

* **i1-quartz-monitor**
  A web application that could be used to monitor quartz jobs as well as managing them, such as creating/deleting/suspending/starting jobs.

## How To Use I1

### Set up your IDEA project
In default we use gradle as build tool and IDEA as IDE. To set it up, you should:
* Check you've installed jdk7, gradle and IDEA;
* Fork the code from GitHub;
* Run "gradle cleanIdea idea", gradle will download the dependencies automatically.

### Run the app
To see each module is, you could run it up by either one of the following ways:
** Run Java class "LauncherStartClass"
** Run gradle command "gradle clean jettyRun"
** Package to a war and deploy it to a web server.

## Contributors
[aqingsao](https://github.com/aqingsao), [zengchuan](https://github.com/zengchuan), [caihongbo](https://github.com/caihongbo)

If you have any idea or questions on I1 project, or if you want to contribute, feel free to contact us.