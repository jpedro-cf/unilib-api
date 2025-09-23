#!/bin/sh
set -e

# Create keys directory
mkdir -p /app/keys

# Write PEM files from environment variables
echo "$PUBLIC_KEY_CONTENT" > /app/keys/public.pem
echo "$PRIVATE_KEY_CONTENT" > /app/keys/private.pem
chmod 600 /app/keys/private.pem

# Run Spring Boot
exec java -jar /app/app.jar