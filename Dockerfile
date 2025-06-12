FROM caddy:alpine

# Copy your corrected Caddyfile
COPY Caddyfile /etc/caddy/Caddyfile

# Copy static frontend files
COPY frontend/ /srv/frontend/
