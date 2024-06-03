"""
Your module description
"""

import boto3

s3 = boto3.client('s3')

resp = s3.select_object_content(
    Bucket='demotebkt2812',
    Key='airport-codes.csv',
    ExpressionType='SQL',
    Expression="SELECT count(*) FROM s3object s where s.\"iso_country\" = 'US'",
    InputSerialization = {'CSV': {"FileHeaderInfo": "Use"}, 'CompressionType': 'NONE'},
    OutputSerialization = {'CSV': {}},
)

for event in resp['Payload']:
    if 'Records' in event:
        records = event['Records']['Payload'].decode('utf-8')
        print(records)
    elif 'Stats' in event:
        statsDetails = event['Stats']['Details']
        print("Stats details bytesScanned: ")
        print(statsDetails['BytesScanned'])
        print("Stats details bytesProcessed: ")
        print(statsDetails['BytesProcessed'])
        print("Stats details bytesReturned: ")
        print(statsDetails['BytesReturned'])
