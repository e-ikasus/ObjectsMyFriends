// To create the image containing the database.
docker build -f DockerfileDB -t objects_my_friends_db:latest  .

// To create the image containing the application.
docker build -f DockerfileApp -t objects_my_friends:latest .

// To launch the database.
docker run -p 3306:3306 --name ObjectsMyFriendsDB --add-host localhost:127.0.0.1 objects_my_friends_db:latest

// To launch the application.
docker run -p 8080:8080 -p 8081:9990 --name ObjectsMyFriends --add-host localhost:127.0.0.1 objects_my_friends:latest

