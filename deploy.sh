image=admin-service
username=trivip002
version=latest

mvn clean install
docker build -t "$username"/"$image" .
echo Please Login DockerHub to push image
docker login --username="$username"
docker push "$username"/"$image":"$version"
echo Push Image Successful.