import boto3

# Initialize a session using Amazon S3 credentials
session = boto3.Session()

# Retrieve region from session object
current_region = session.region_name
print("current Region is", current_region)

# Low level client
# Initialize the S3 client
s3 = session.client('s3')

# List the buckets
buckets = s3.list_buckets()

# Print each bucket name
for bucket in buckets['Buckets']:
        print(bucket['Name'])

## High level Client - List each object in a bucket 
s3 = session.resource('s3')

my_bucket = s3.Bucket('bubeebkt')

for my_bucket_object in my_bucket.objects.all():
    print(my_bucket_object.key)