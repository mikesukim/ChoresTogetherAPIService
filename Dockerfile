# Dockerfile reference : https://docs.docker.com/engine/reference/builder/#expose
# base image
FROM openjdk:11
# copy all files at current directory to virtual directory
COPY . /src/ChoresTogetherAPIService
# set default virtual directory
WORKDIR /src/ChoresTogetherAPIService

# TODO: get region and stage as ARGs
# Required environment variables for AWS SDK
ENV AWS_ACCESS_KEY_ID=fakeMyKeyId
ENV AWS_SECRET_ACCESS_KEY=fakeSecretAccessKey

# In order for our service to run differently depends on Region and Stage (e.x  dynamoDB endpoint),
# these two environment variables must be specified ECS task definition as well, which will mask below default values.
# https://docs.aws.amazon.com/AmazonECS/latest/developerguide/taskdef-envfiles.html
# environment variables setting at ECS task definition will be same as running docker run with -e option,
# and specifying env variables with -e option will masks below two default values.
ENV STAGE=local
ENV AWS_REGION=us-west-2


# Jwt secret key only for local testing. production Jwt secret key will be pulled out of AWS Secrets Manager
ENV JWT_LOCAL_SECRET_KEY=localKey


# container listening port (not publishing port)
EXPOSE 8080
# commands to build image on top of openjdk:11 image
RUN ["./gradlew", "run-production-build", "-i"]
# one command to run the image
CMD ["java", "-jar", "build/libs/ChoresTogetherAPIService-1.0-SNAPSHOT.jar"]

# for faster local development (for skipping unit testing & checkstyle), uncomment below RUN and comment above RUN before running
# docker-compose build, docker-compose up
# RUN ["./gradlew", "run-development-build", "-i"]

