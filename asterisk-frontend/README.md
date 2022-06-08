# Asterisk frontend
Nx workspace using Angular 13. Check the ``package.json`` scripts for more information on how to run this app.
- [General](#general)
  - [Developing locally](#developing-locally)
  - [Building for production](#building-for-production)

## General
Some basic and important information on how the angular setup is working.
### Developing locally
When developing locally a proxy is used to send all requests to the backend
therefore making a CORS configuration in the backend obsolete. For reference see the 
``proxy.conf.json`` inside the ``website`` app.

Effectively what this means is that if our backend is running at ``localhost:8080``
and our frontend (development) server is running at ``localhost:4200`` a proxy is set up so that
all requests to these particular URLs ``localhost:4200/api/*`` (the frontend itself) are redirected to ``localhost:8080/*``

### Building for production
In production however as you can see in the ``Dockerfile`` nginx is used to
serve the build content of our angular application(s). Since we don't have a development server in production but rather nginx, the setup described
for [Developing locally](#developing-locally) pretty much applies the same here only with port 80 instead of 4200.
