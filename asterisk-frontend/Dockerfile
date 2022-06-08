# Build the whole nx workspace
FROM node:lts-alpine AS builder

WORKDIR /app

COPY ./ ./

RUN npm i && npm run prodbuild

# Setup nginx to serve the website app
FROM nginx:alpine

WORKDIR /usr/share/nginx/html

RUN rm -rf ./*

ADD nginx-default.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist/apps/website .

EXPOSE 80
