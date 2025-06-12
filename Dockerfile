# Use the official Caddy builder image so we can run as root
FROM caddy:builder AS builder

# Copy site content and Caddyfile into builder image
COPY frontend /srv/frontend
COPY Caddyfile /etc/caddy/Caddyfile

# Build final image with full permissions
FROM caddy:latest

COPY --from=builder /srv/frontend /srv/frontend
COPY --from=builder /etc/caddy/Caddyfile /etc/caddy/Caddyfile

CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
