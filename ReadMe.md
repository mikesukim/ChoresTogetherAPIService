# ChoresTogetherAPIService (draft)
This readme doc is in progress.

This project also serves the purpose of exercising the best practice of developing the backend API service. 

Please contact mikesungunkim@gmail.com for any inquiries.
## Installation

1. clone this project to your local directory
```bash
git clone https://github.com/mikesukim/ChoresTogetherAPIService.git
```
2. open Docker Desktop (Docker installation requires if Docker-desktop is not installed)
3. enter the project's directory
```bash
cd ChoresTogetherAPIService
```
4. build docker containers for ChoresTogetherAPIService & Local dynamoDB by running
```bash
docker-compose build
```
5. run containers by running
```bash
docker-compose up
```
6. (first time only) create a table at local DynamoDB by opening new tap and running
```bash
aws dynamodb create-table \
--table-name User \
--attribute-definitions AttributeName=email,AttributeType=S \
--key-schema AttributeName=email,KeyType=HASH \
--provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
--endpoint-url http://localhost:8000 
```

Now setup is done. Make sure docker containers are running (docker-compose up) and test if the setup was done correctly by calling API.
```bash
 curl -X POST http://localhost:8080/users \
     -H "Content-Type: application/json"  \
     -d '{"email":"mikesungunkim@gmail.com"}'
```


## Development setup (IntelliJ)
1. after cloning, open this project at IntelliJ by selecting this project's directory
2. once opened, right-click anywhere in the project panel, and click "Link to Gradle"
3. now use gradle to run gradle's tasks at IntelliJ for development. For possible options for Gradle's tasks, please read build.gradle file.
4. before calling APIs, local dynamoDB setup is required.

## APIs
### Get user
___
#### Request
 `GET /users/{userEmail}`
```bash
 curl -i http://localhost:8080/users/mikesungkim@gmail.com
```
#### Response
```Json
 {"status" : "success"}
```

### Create user
___
#### Request
`POST /users`
```bash
 curl -X POST http://localhost:8080/users \
     -H "Content-Type: application/json"  \
     -d '{"email":"mikesungunkim@gmail.com"}'
```
#### Response
```Json
 {"status" : "success"}
```
```Json
 {"status":"error","message":"item already exists"}
```

## Contributing
TBA
## License
TBA
