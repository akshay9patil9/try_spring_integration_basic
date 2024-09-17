# README

## Spring Integration

This goal of this project is to try out basic features of Spring Integration.

In the current version (2024-09-17),
 
1. We made a Rest route for  REST endpoint `/so/questions?tag=<tag>`. This endpoint will hit stackoverflow API and fetch the questions related to the given tag. e.g. `http://127.0.0.1:8081/so/questions?tag=android` will fetch questions for android

2. Above route will be directed to stackexchange api to get the related questions.

We learnt how to use flows java dsl, consume external api and few more things in this project.
