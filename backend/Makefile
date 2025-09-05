run-frontend:
	cd ../frontend && npm run dev

run-backend:
	export POSTGRES_DB=financy && export POSTGRES_USER=postgres && export POSTGRES_PASSWORD=postgres && export REDIS_PASSWORD=helloworld && mvn spring-boot:run

run-backend-docker:
	cd .. && docker-compose up backend

run-all-docker:
	cd .. && docker-compose up