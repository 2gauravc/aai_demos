import boto3, botocore

# declare a function to vrify the bucket 
def verifyBucketName(s3Client,bucket):
    try:
        ## check if a bucket already exists in AWS
        s3Client.head_bucket(Bucket=bucket)
        # If the previous command is successful, the bucket is already in your account.
        raise SystemExit('This bucket has already been created')
    except botocore.exceptions.ClientError as e:
        error_code=int(e.response['Error']['Code'])
        if error_code == 404:
            ## If you receive a 404 error code, a bucket with that name
            ##  does not exist anywhere in AWS.
            print('Existing bucket Not Found, please proceed')
        if error_code == 403:
            ## If you receive a 403 error code, a bucket with that name exists 
            ##  in another AWS account.
            raise SystemExit('This bucket is already owned by another AWS Account')
        
# Create S3 resource using custom session
session = boto3.session.Session(profile_name='default')

# Retrieve region from session object
current_region = session.region_name

# Create a high-level resource from custom session
client = session.client('s3')

verifyBucketName(client, 'demo--bkt-serverless')