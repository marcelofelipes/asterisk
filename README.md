# Asterisk

A sample full stack web application using JWT. Made in Flutter + Spring Boot + Angular

- [Requirements](#requirements)
- [Structure](#structure)
- [Docker](#docker)

## Requirements
* Java 17
* Maven
* Flutter 3.0.1
* Docker

## Structure
This project is structured in the following way:
* ``/asterisk-backend`` contains the Spring Boot backend

## Docker
The backend is dockerized. Run ``docker build -t asterisk-backend ./asterisk-backend/`` to create a docker image of the Spring Boot backend.

After that is done run the ``docker-compose.yaml`` by executing ``docker compose up``.