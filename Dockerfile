FROM debian:bullseye-slim

# Install required packages
RUN apt-get update && apt-get install -y curl unzip ca-certificates && rm -rf /var/lib/apt/lists/*

# Download latest Caddy binary (correct name)
RUN curl -LO https://github.com/caddyserver/caddy/releases/latest/download/caddy_2_linux_amd64.tar.gz && \
    tar -xzf caddy_2_linux_amd64.tar.gz && \
    mv caddy /usr/bin/caddy && \
    chmod +x /usr/bin/caddy && \
    rm caddy_2_linux_amd64.tar.gz

# Copy frontend files and Caddyfile
COPY frontend /srv/frontend
COPY Caddyfile /etc/caddy/Caddyfile

EXPOSE 80

CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
