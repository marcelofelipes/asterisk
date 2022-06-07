# Asterisk

A sample full stack web application and mobile app. Utilising Json Web Tokens (JWT) to authorized requests. Made with the (as I call it) PSARF stack. Which means 
**P**ostgresql, **S**pring-Boot, **A**ngular, **R**edis and **F**lutter.

- [Requirements](#requirements)
- [Structure](#structure)
- [Docker](#docker)

## Requirements
* Java 17
* Maven
* Redis
* Flutter 3.0.1
* Docker
* NodeJS and npm

## Structure
This project is structured in the following way:
* ``/asterisk-backend`` contains the Spring Boot backend
* ``/asterisk/frontend`` contains a nx workspace with an angular app
* ``/documentation`` contains images used in README

## Docker
The whole stack is dockerized. Run ``docker compose up --build`` to execute the postgresql, redis, angular website and spring boot backend containers.
To verify everything is working visit http://localhost:8080/healthcheck (backend) and http://localhost:80/ (frontend)