# --- Stage 1: Build Caddy with no plugins ---
FROM golang:1.20 as builder

# Set Go environment variables
ENV GO111MODULE=on

# Download and build Caddy from source
RUN git clone https://github.com/caddyserver/caddy.git /caddy

WORKDIR /caddy/cmd/caddy

RUN go build -o /caddy_build

# --- Stage 2: Minimal runtime image ---
FROM debian:bullseye-slim

# Install required CA certificates
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*

# Copy Caddy binary
COPY --from=builder /caddy_build /usr/bin/caddy

# Copy site files and config
COPY frontend /srv/frontend
COPY Caddyfile /etc/caddy/Caddyfile

# Give execute permissions to Caddy
RUN chmod +x /usr/bin/caddy

# Expose HTTP port
EXPOSE 80

# Start Caddy
CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
