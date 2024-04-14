import boto3

# Initialize a session using Amazon S3 credentials
session = boto3.Session()

# Initialize the S3 client
s3 = session.client('s3')

# List the buckets
buckets = s3.list_buckets()

# Print each bucket name
for bucket in buckets['Buckets']:
        print(bucket['Name'])