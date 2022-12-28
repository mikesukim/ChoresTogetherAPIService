# Dockerfile reference : https://docs.docker.com/engine/reference/builder/#expose
# base image
FROM openjdk:11
# copy all files at current directory to virtual directory
COPY . /src/ChoresTogetherAPIService
# set default virtual directory
WORKDIR /src/ChoresTogetherAPIService

# TODO: get region and stage as ARGs
# Required environment variables for AWS SDK
ENV AWS_REGION=us-west-2
ENV AWS_ACCESS_KEY_ID=fakeMyKeyId
ENV AWS_SECRET_ACCESS_KEY=fakeSecretAccessKey

# container listening port (not publishing port)
EXPOSE 8080
# commands to build image on top of openjdk:11 image
RUN ["./gradlew", "create-jar", "-i"]
# one command to run the image
CMD ["java", "-jar", "build/libs/ChoresTogetherAPIService-1.0-SNAPSHOT.jar"]

