import boto3
import time

# Create S3 resource using custom session
session = boto3.session.Session(profile_name='default')

# Retrieve region from session object
current_region = session.region_name

# Create a high-level resource from custom session
resource = session.resource('s3')
bucket = resource.Bucket('demobkt1977-1')

# Region-specific endpoints require the LocationConstraint parameter
bucket.create(
    CreateBucketConfiguration={'LocationConstraint': 'us-west-1'})

# Create a waiter that counts the number of seconds till the bucket is created 

t0 = time.time()
waiter = bucket.wait_until_exists('demobkt1977-1')
t1 = time.time()
total = t1-t0

print("Total time waited", total, "seconds")
