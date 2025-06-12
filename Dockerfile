FROM caddy:alpine

# Copy your Caddyfile
COPY Caddyfile /etc/caddy/Caddyfile

# Copy frontend static files
COPY frontend/ /srv/frontend/

# Run Caddy as root (Render requires it)
USER root
