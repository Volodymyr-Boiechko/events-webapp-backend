### Stage 1: Compile and Build angular codebase ###

# Use official node image as the base image
FROM node:14.20.1-alpine AS build

ARG WORK_DIR=/usr/src/app

# Set the working directory
WORKDIR ${WORK_DIR}

# Add the source code to app
COPY ./ ${WORK_DIR}

# Install all the dependencies
RUN npm install

# Generate the build of the application
RUN npm run build


### Stage 2: Serve app with nginx server ###

# Use official nginx image as the base image
FROM nginx:alpine

# Copy custom nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Copy the build output to replace the default nginx contents.
COPY --from=build /usr/src/app/output/static /usr/share/nginx/html

ENTRYPOINT ["nginx", "-g", "daemon off;"]
