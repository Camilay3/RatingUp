# build do Angular
FROM node:22-alpine AS build
WORKDIR /app
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .
RUN npm run build

# nginx final, já com os arquivos estáticos e as configs
FROM nginx:alpine
COPY config/nginx/nginx.conf /etc/nginx/nginx.conf
COPY ["config/nginx/http.d/00 - default.conf", "/etc/nginx/http.d/00 - default.conf"]
COPY ["config/nginx/ratingup.prod.conf", "/etc/nginx/http.d/01 - ratingup.conf"]
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html