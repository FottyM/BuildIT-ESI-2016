FROM mhart/alpine-node:5
WORKDIR /source/app
# Expose the port 8001
EXPOSE 8001
# Set the default command to run when a container starts
ENTRYPOINT npm run start
RUN npm install -g gulp gulp-cli typings typescript ts-node concurrently lite-server
RUN npm install --no-bin-links
RUN npm install --no-bin-links
# Install app dependencies
COPY package.json .
RUN npm install
# Copy your code in the docker image
COPY . .
#Build
RUN npm run build




