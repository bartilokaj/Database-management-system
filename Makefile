# ------------------------------
# Makefile for Database Management System
# Builds Docker image and prepares container with persistent data
# ------------------------------

# Docker image and container names
IMAGE_NAME = blokaj-dbms
CONTAINER_NAME = blokaj-dbms
HOST_DATA_DIR = ./data
CONTAINER_DATA_DIR = /data
PORT = 7000


# ------------------------------
# Build Docker image
# ------------------------------
docker-build:
	mvn clean compile jib:dockerBuild -DskipTests

# ------------------------------
# Run Docker container with persistent volume
# ------------------------------
docker-run:
	mkdir -p $(HOST_DATA_DIR)
	# Stop and remove existing container if exists
	-docker stop $(CONTAINER_NAME)
	-docker rm $(CONTAINER_NAME)
	# Run new container
	docker run -d \
		--name $(CONTAINER_NAME) \
		-p $(PORT):$(PORT) \
		-v $(HOST_DATA_DIR):$(CONTAINER_DATA_DIR) \
		$(IMAGE_NAME)
	@echo "Docker container '$(CONTAINER_NAME)' is now running on port $(PORT)."
	@echo "Host data directory '$(HOST_DATA_DIR)' is mounted to '/data' inside the container for persistence."

# ------------------------------
# Full workflow: build image and run container
# ------------------------------
docker: docker-build
	@echo "Docker image '$(IMAGE_NAME)' built. Run the container with your data volume mounted to /data so that state can be preserved between sessions"
	@echo "Target 'docker-run' specifies port to be 7000, and binds data directory of the project root, to docker /data"

