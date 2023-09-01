.PHONY: build run

IMAGE_NAME = receipt-processor
CONTAINER_NAME = fetch-container
TEST_IMAGE_NAME = receipt-processor-test
TEST_CONTAINER_NAME = fetch-test-container

build:
	docker build -t $(IMAGE_NAME) .

run:
	@echo "Running the app..."
	-docker rm $(CONTAINER_NAME)
	docker run --rm -p 8080:8080 --name $(CONTAINER_NAME) $(IMAGE_NAME)

stop:
	@echo "Stop the container..."
	-docker stop $(CONTAINER_NAME)

start:
	@echo "Starting a new container..."
	-docker rm $(CONTAINER_NAME)
	docker run --rm -p 8080:8080 --name $(CONTAINER_NAME) $(IMAGE_NAME)

restart: stop start

test:
	@echo "Building test image..."
	docker build -t $(TEST_IMAGE_NAME) -f Dockerfile-test .
	@echo "Running tests..."
	docker run --rm --name $(TEST_CONTAINER_NAME) $(TEST_IMAGE_NAME)

all: build run
