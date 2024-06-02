import boto3
import logging
from botocore.exceptions import ClientError
logger = logging.getLogger(__name__)# Set up logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def get_object(bucket, object_key):
    try:
        obj = bucket.Object(object_key)
        body = obj.get()['Body'].read()
        logger.info("Got object '%s' from bucket '%s'.", object_key, bucket.name)
    except ClientError:
        logger.exception("Couldn't get object '%s' from bucket '%s'.", object_key, bucket.name)
        raise
    else:
        return body

# Create S3 resource using custom session
session = boto3.session.Session()

# Create a high-level resource from custom session
resource = session.resource('s3')
bucket = resource.Bucket('demotebkt2812')
object_key= 'key.txt'

body = get_object(bucket, object_key) 
print(body) 

