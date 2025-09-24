data "aws_caller_identity" "current" {}

resource "aws_s3_bucket" "unilib_bucket" {
    bucket = var.DEV_MODE ? "unilib-bucket" : "unilib-bucket-${data.aws_caller_identity.current.account_id}"
}


resource "aws_s3_bucket_public_access_block" "public_access_block" {
  bucket = aws_s3_bucket.unilib_bucket.id

  block_public_acls       = true
  block_public_policy     = false
  ignore_public_acls      = true
  restrict_public_buckets = false
}

resource "aws_s3_bucket_policy" "public_images_policy" {
  bucket = aws_s3_bucket.unilib_bucket.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Sid       = "AllowPublicReadImages",
        Effect    = "Allow",
        Principal = "*",
        Action    = "s3:GetObject",
        Resource  = "${aws_s3_bucket.unilib_bucket.arn}/images/*"
      }
    ]
  })
}

resource "aws_s3_bucket_cors_configuration" "unilib_bucket_cors" {
  bucket = aws_s3_bucket.unilib_bucket.id

  cors_rule {
    allowed_headers = ["*"]
    allowed_methods = ["GET", "PUT", "POST", "DELETE", "HEAD"]
    allowed_origins = ["*"]
    expose_headers  = ["Access-Control-Allow-Origin", "ETag"]
    max_age_seconds = 3000
  }
}