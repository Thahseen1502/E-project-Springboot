FROM caddy:alpine

# Copy your Caddyfile to the official config location
COPY Caddyfile /etc/caddy/Caddyfile

# Copy frontend static files
COPY frontend/ /srv/frontend/

# Set working directory (optional)
WORKDIR /srv

# Run Caddy manually (Render needs this!)
CMD ["caddy", "run", "--config", "/etc/caddy/Caddyfile", "--adapter", "caddyfile"]
