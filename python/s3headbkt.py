import boto3, botocore

# declare a function to vrify the bucket 
def verifyBucketName(s3Client,bucket):
    print("Checking for bucket: {}".format(bucket))
    try:
        ## check if a bucket already exists in AWS
        s3Client.head_bucket(Bucket=bucket)
        # If the previous command is successful, the bucket is already in your account.
        raise SystemExit('Bucket exists and is owned by this account.')
    except botocore.exceptions.ClientError as e:
        error_code=int(e.response['Error']['Code'])
        if error_code == 404:
            ## If you receive a 404 error code, a bucket with that name
            ##  does not exist anywhere in AWS.
            print('Error Code 404. No such bucket exists.')
        if error_code == 403:
            ## If you receive a 403 error code, a bucket with that name exists 
            ##  in another AWS account.
            raise SystemExit('Error Code 403. Permission errors in accessing bucket. Likely bucket owned by another account.')
        if error_code == 400:
            ## If you receive a 400 error code, a bucket with that name exists 
            ##  in another AWS region.
            raise SystemExit('Error Code 400. Bucket exists not in this region.')
# Create S3 client using custom session
session = boto3.session.Session(profile_name='default')

# Retrieve region from session object
current_region = session.region_name
print("Current region is: {}".format(current_region))

# Create a low level client from custom session
client = session.client('s3')

verifyBucketName(client, 'demobkt1977')