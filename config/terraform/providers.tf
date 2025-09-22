terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "6.0.0"
    }
  }
}

provider "aws" {
    region            = var.AWS_REGION
    access_key        = var.AWS_ACCESS
    secret_key        = var.AWS_SECRET

#     s3_use_path_style = true
#     skip_credentials_validation = true
#     skip_metadata_api_check     = true
#     skip_requesting_account_id  = true

#     endpoints {
#       s3             = "http://localhost:4566" # localstack (change later)
#       sqs            = "http://localhost:4566" # localstack (change later)
#     }
}