work_path=$(pwd)
docker run -p 27017:27017 -v ${work_path}"/db":/data/db --name main-db --network bookie-system -d mongo:latest