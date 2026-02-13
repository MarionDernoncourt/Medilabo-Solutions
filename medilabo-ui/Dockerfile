#1. Build
FROM node:20-slim AS build
WORKDIR /app

#Copie des dependances
COPY package*.json ./
RUN npm install

#Copie du reste du code
COPY . .
RUN npm run build

#2. Runtime
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

# Utilisation de CMD pour lancer le serveur web
CMD ["nginx", "-g", "daemon off;"]