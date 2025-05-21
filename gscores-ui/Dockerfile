# Build stage
FROM node:20-alpine AS build

WORKDIR /app

# Set environment variables
ENV NODE_OPTIONS=--max-old-space-size=1024

# Copy package files and install dependencies
COPY package*.json ./
RUN npm ci

# Copy all application files and build
COPY . .
RUN npm run build

# Production stage using Nginx
FROM nginx:alpine

# Copy built files to Nginx serve directory
COPY --from=build /app/dist /usr/share/nginx/html

# Copy Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
