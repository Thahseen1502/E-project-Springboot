FROM debian:bullseye-slim

# Install curl and unzip to fetch caddy binary
RUN apt-get update && apt-get install -y curl unzip ca-certificates && rm -rf /var/lib/apt/lists/*

# Download and install Caddy binary
RUN curl -fsSL https://github.com/caddyserver/caddy/releases/latest/download/caddy_linux_amd64.tar.gz | tar -xz -C /usr/bin caddy

# Copy frontend and Caddyfile
COPY frontend /srv/frontend
COPY Caddyfile /etc/caddy/Caddyfile

# Give permissions
RUN chmod +x /usr/bin/caddy

# Expose HTTP
EXPOSE 80

# Start Caddy
CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
