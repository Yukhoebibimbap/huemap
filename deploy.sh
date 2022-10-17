REPOSITOYRY=/home/ubuntu/huemap
cd $REPOSITOYRY

cd backend
chmod +x gradlew
cd ..

echo "> 실행"
docker-compose up --build -d
