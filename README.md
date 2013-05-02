# I1

## What Is I1

We are trying to reuse code on various levels: method/class level, plugin/library level or module level. However it's well known that the finer the granularity is, the easier it its to reuse. This project is a trial to reuse on service level, also known as micro services, which could be deployed and called by enterprise applications without reinventing the wheel.

The name "i1" means that there will always be some functions that we have to develop on the first several iterations. 

## I1 Project List

So far we have developed and are developing the following projects:

* **i1-email-sender**
  It's not a smtp server, but a web application that could be used to send email with existing smtp server. It provides 

* **i1-file-uploader**
  A web application that could be used to upload files. It also provides some client JavaScript sample code for usage.

* **i1-quartz-monitor**
  A web application that could be used to monitor quartz jobs as well as managing them, such as creating/deleting/suspending/starting.

## How To Use I1

In default we use gradle as build tool and IDEA as IDE. After forking the code, run "gradle cleanIdea idea" to setup the project. We could start it in either way:
** Run Java class LauncherStartClass
** Run gradle command "gradle clean jettyRun"
** Package to a war and deploy it to a web server.