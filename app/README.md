Run command:
sudo ./gradlew bootrun --args='--spring.datasource.password=<ENTER-PASSWORD>'

Command to run mySQL DB
/usr/local/mysql/bin/mysql -u root -p

**_ todos _**

1. Create DTO and Entiry for all models. currently just have a single model for data transfer and entity for repos

2) add completionStatus (true/false) to the toDoListTask
3) create unit test
4) add data validation
   - ensure no sql injections, ensure any other standards we want to enforce
5) add error handling
6) we need to consider access control on all api requests.
   - for example, if we get a request to delete something, we need to make sure that our user has the correct access to do this. As of now, nothing is stopping someone from deleting any to-do list, even from a diff company, as long as they know the uuid (ovbs unlikely). But i feel like we should also be always sending userID to make sure the person submitting the request has the authorization to do so. Not sure if this will be taken care of with a sessions or tokens.

**_ Bug _**

1. server side logs have "o.hibernate.internal.util.EntityPrinter" debug statments whenever we read/write to DBs. these debug statments will currently print entity info such as user/buisness info (which contains sensative data).. not sure why this is happening

   - this seems like a security issue

   - This should fix 2 problems:
     - User and Buinsess info getting returned when toDoList is sent as response.
     - The need to include toDoList id for each task durring a put request

2) add completionStatus (true/false) to the toDoListTask
3) create unit test
4) add data validation
5) add error handling
6)

**_ Test commands _**
Important: All IDs must be present when updating! toDoListId, UserId, taskId's and \*\*\* toDoList with the corrusponding Id needs to be present for each task.. this needs to be fixed tbh..
curl -X PUT -H "Content-Type: application/json" -d '{
"toDoListId":"514412a6-4c31-4d49-b5be-dd63e34c39b1",
"listName": "UPDATE Test on aug 8",
"user": "49de9bd5-006b-49a7-a474-a607b53d8016",
"taskList":
[{
"taskId":"489ec12c-eb4d-4156-bd98-6a7411b79357",
"taskDescription":"UPDATE task 1 - Test on aug 8",
"taskDueDate":"Update rand",
"toDoList":"514412a6-4c31-4d49-b5be-dd63e34c39b1"
},
{
"taskId":"aacb18ea-1f6d-44c2-a788-618c8d22309f",
"taskDescription":"UPDATE task 2 - Test on aug 8",
"taskDueDate":"UPDATE rand",
"toDoList":"514412a6-4c31-4d49-b5be-dd63e34c39b1"
}]
**_ Test commands _**
curl -X PUT -H "Content-Type: application/json" -d
'{
"toDoListId":"903386cb-3829-4662-b977-9d438bf9d849",
"listName":"update - Test on aug 10",
"userId":"49de9bd5-006b-49a7-a474-a607b53d8016",
"taskList":
[{
"taskId":"39125fad-fa43-4f49-82c9-58c017f68eff",
"taskDescription":"update - task 1 - Test on aug 14",
"taskDueDate":"update -rand"
},
{
"taskId":"cc6c6053-6335-47bf-9d8d-7bf5da448072",
"taskDescription":"update - task 2 - Test on aug 14",
"taskDueDate":"update -rand"
}]
}' http://localhost:8080/modifyList

curl -X POST -H "Content-Type: application/json" -d '{
"listName": "Test on aug 8",
"user": "49de9bd5-006b-49a7-a474-a607b53d8016",
"taskList":
[{
"taskDescription":"task 1 - Test on aug 8",
"taskDueDate":"rand"
},
{
"taskDescription":"task 2 - Test on aug 8",
"taskDueDate":"rand"
}]
}' http://localhost:8080/create/list

To create update:
curl -X POST \
-H "Content-Type: application/json" \
-d '{
"userId": "758854eb-5f1e-4597-a4d7-2e41f0c7e568,
"businessId": "a3d25a9a-a25f-4db1-a46b-227e98dc4921",
"date": "2023-08-17",
"title": "Sample Update Title",
"description": "Sample Update Description",
"images": [
{
"updateImageId": "image-uuid-1",
"base64Image": "base64EncodedString1"
},
{
"updateImageId": "image-uuid-2",
"base64Image": "base64EncodedString2"
}
]
}' \
http://localhost:8080/create

To create Business:

curl -X POST \
-H "Content-Type: application/json" \
-d '{
"businessName": "MoonLight Connect",
"email": "moonlightconnect@gmail.com",
"phoneNumber": "(555) 555 - 5555",
"subscriptionType": "Tier-1"
}' \
http://localhost:8080/business/create

To create User:
curl -X POST \
-H "Content-Type: application/json" \
-d '{
"firstName":"Matthew",
"lastName":"Shamoon",
"userName":"mshamoon",
"email":"matt@hotmail.com",
"password":"password123",
"phoneNumber":"(555)-555 5555",
"role":"client",
"businessId":"26f7cc8f-88c2-41e8-90c0-62e51750bff9"
}' \
http://localhost:8080/user/create

To Get Users for specifc business:
curl "http://localhost:8080/user/get-users?businessId=26f7cc8f-88c2-41e8-90c0-62e51750bff9"

curl "http://localhost:8080/user/get-single-user?userId=093286b9-3543-435c-99a8-0d01183a63a8"

To create Project:
curl -X POST \
-H "Content-Type: application/json" \
-d '{
"address":"308 lakewood crescent",
"startDate":"Sept 2, 2023",
"endDate":"Dec 1, 2023",
"userId":"093286b9-3543-435c-99a8-0d01183a63a8",
"businessId":"26f7cc8f-88c2-41e8-90c0-62e51750bff9"
}' \
http://localhost:8080/project/createProject

To Get Projects for specifc User:
curl "http://localhost:8080/project/get-projects-for-client?clientUserId=093286b9-3543-435c-99a8-0d01183a63a8"

To get specific project:
curl "http://localhost:8080/project/get-single-project-for-client?projectId=5e69be7e-d79d-4c86-8413-d5fdacb1f6b8"

**_ Curl command to upload a file _**
`curl -X POST -F "file=@joesDocShareDocument.txt" http://localhost:8080/doc-share/upload`
