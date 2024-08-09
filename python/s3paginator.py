import boto3 

# Create a boto3 client for S3
client = boto3.client('s3')

# Create a paginator object for the 'list_objects_v2' operation
paginator = client.get_paginator('list_objects_v2')

# Use the paginator to paginate through the results
page_iterator = paginator.paginate(
    Bucket='bubeebkt',
    PaginationConfig={'MaxItems':20}
)

# Iterate over each page of results
for page in page_iterator:
    # Check if 'Contents' is in the page (it won't be if the bucket is empty)
    if 'Contents' in page:
        for obj in page['Contents']:
            print(obj['Key'])
    else:
        print("No objects found.")
