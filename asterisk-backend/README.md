# Asterisk-Backend

A secured Spring Boot REST backend application using JWT and Postgresql. It manages
and authenticates Users.

- [Introduction](#introduction)
- [Setup](#setup)
- [Architecture](#architecture)
- [Routes](#routes)

## Introduction
As already said this backend is using JWTs to handle the authorization of protected routes. It does that utilizing a "two-part key".

## Setup
For your computer to be able to run this application you need to have maven and java 17 and postgresql installed.

Once this is done go ahead and edit the ``application.properties`` file inside ``/src/main/java/resources``. Once you configured the database and email connections you are good to go.

## Architecture

## Routes