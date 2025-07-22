# UniLib API

API for the UniLib project.

# 🛠️ Installation

-   Configure the public & private RSA keys:

```bash
cd ./src/main/resources

openssl genrsa -out private.pem
openssl rsa -in private.pem -pubout -out public.pem
```

-  Run docker dependencies if needed (it includes localstack, so you don't need to configure anything in your AWS account):

```bash
cd ./config

docker compose -f docker-compose.yml -f dev-docker-compose.yml up -d
```
- Run Terraform configs and export the environment variables:

```bash
cd ./config/terraform

terraform init
export $(cat ../.env | xargs -I% echo TF_VAR_%) && terraform apply
```

Make sure the environment variables are correct, following the example in ``./config/.env.example``:
```bash
DB_USER=...
DB_PASSWORD=...
DB_NAME=...
DB_PORT=...

PG_ADMIN_EMAIL=...
PG_ADMIN_PASSWORD=...

AWS_ACCESS=...
AWS_SECRET=...
AWS_REGION=...
```