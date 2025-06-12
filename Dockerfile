FROM debian:bullseye-slim

# Install necessary dependencies
RUN apt-get update && apt-get install -y curl tar ca-certificates && rm -rf /var/lib/apt/lists/*

# Download and extract Caddy v2.7.6
RUN curl -L https://github.com/caddyserver/caddy/releases/download/v2.7.6/caddy_2.7.6_linux_amd64.tar.gz -o caddy.tar.gz && \
    tar -xzf caddy.tar.gz && \
    mv caddy /usr/bin/caddy && \
    chmod +x /usr/bin/caddy && \
    rm caddy.tar.gz

# Copy frontend files and Caddyfile
COPY frontend /srv/frontend
COPY Caddyfile /etc/caddy/Caddyfile

# Expose HTTP port
EXPOSE 80

# Start Caddy
CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
