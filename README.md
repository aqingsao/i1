# i1

## Introduction

We are trying to reuse code on various levels: method/class level, plugin/library level or module level. However it's well known that the finer the granularity is, the easier it its to reuse. This project is a trial to reuse on service level, also known as micro services, which could be deployed and called by enterprise applications without reinventing the wheel.

The name "i1" means that there will always be some functions that we have to develop on the first several iterations. 

## Project List

So far we have developing following projects:

* **i1-email-sender**
  It's not a smtp server, but a web application that could be used to send email with existing smtp server. It provides 

* **i1-file-uploader**
  A web application that could be used to upload files. It also provides some client JavaScript sample code for usage.

## To Start

In default we use gradle as build toold and IDEA as IDE. After downloading code, run "gradle cleanIdea idea" to setup the project.