# Step 1: Build Caddy with plugins (optional)
FROM golang:1.20 as builder

RUN git clone https://github.com/caddyserver/caddy.git && \
    cd caddy/cmd/caddy && \
    go build -o /caddy

# Step 2: Create minimal runtime image
FROM debian:bullseye-slim

# Install dependencies
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*

# Copy Caddy binary from builder
COPY --from=builder /caddy /usr/bin/caddy

# Copy site files and Caddyfile
COPY frontend /srv/frontend
COPY Caddyfile /etc/caddy/Caddyfile

# Set permissions and default command
RUN chmod +x /usr/bin/caddy

EXPOSE 80

CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
