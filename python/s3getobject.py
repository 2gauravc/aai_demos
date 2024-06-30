import boto3
import logging
from botocore.exceptions import ClientError
logger = logging.getLogger(__name__)# Set up logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def get_object(bucket, object_key):
    try:
        obj = bucket.Object(object_key)
        response = obj.get()
        body = response['Body'].read()
        size = response['ContentLength']
        content_type = response['ContentType']
        
        logger.info("Got object '%s' from bucket '%s'.", object_key, bucket.name)
    except ClientError:
        logger.exception("Couldn't get object '%s' from bucket '%s'.", object_key, bucket.name)
        raise
    else:
        return body, size, content_type

# Create S3 resource using custom session
session = boto3.session.Session()

# Create a high-level resource from custom session
resource = session.resource('s3')
bucket = resource.Bucket('demotebkt2812')
object_key= 'airport-codes.csv'

body,size, content_type = get_object(bucket, object_key) 
print("Object size is {} MB".format(size/1000000)) 
print("Content Type is {}".format(content_type))

