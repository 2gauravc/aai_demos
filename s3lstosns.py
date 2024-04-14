import boto3

# Initialize a session using Amazon S3 credentials
session = boto3.Session()

# Initialize the S3 client
s3 = session.client('s3')

# List the buckets
buckets = s3.list_buckets()

msg_text = ""
# Print each bucket name
for bucket in buckets['Buckets']:
        msg_text+=(bucket['Name'] + "\n")
 
#convert         
# send to sns topic 
sns = session.client('sns')
response = sns.publish(
    TopicArn='arn:aws:sns:us-east-1:821052193763:demo_topic',    
    Message=msg_text)
print("All Done\n")