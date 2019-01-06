work_path=$(pwd)
docker network create -d bridge bookie-system
docker run -p 27017:27017 --name central-bookie-db --network bookie-system -d mongo:latest
docker run -p 27018:27017 --name bet123-bookie-db --network bookie-system -d mongo:latest
docker run -p 27019:27017 -v ${work_path}"/db":/data/db --name event-organiser-db --network bookie-system -d mongo:latest
docker run --name activeMQ --network bookie-system -d webcenter/activemq:latest 
docker run -p 8081:8080 --name central-bookie --network bookie-system -d tonyzhang1002/ds-bookie-company:central-bookie
docker run -p 8082:8080 --name bet123-bookie --network bookie-system -d tonyzhang1002/ds-bookie-company:bet123-bookie
docker run --name event-organiser --network bookie-system -d tonyzhang1002/ds-bookie-company:event-organiser
