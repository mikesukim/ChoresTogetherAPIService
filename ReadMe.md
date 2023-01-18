# ChoresTogetherAPIService
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
1. After cloning, click "Open" or "File>Open" at IntelliJ, and select build.gradle from this project's directory
2. Now use Gradle to run Gradle's tasks at IntelliJ for development. For possible options for Gradle's tasks, please read build.gradle file.
3. To conduct local integration testing of your changes, build & run docker containers (installation step 4 & 5), and then use Postman/terminal to call APIs.

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
