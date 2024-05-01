"""
This code translates a given pice of text from English to Hindi
"""
import boto3 

import boto3

def translate_text(text, source_lang, target_lang):
    # Create a client with AWS Translate
    client = boto3.client('translate')
    
    # Call the translate_text method from AWS Translate
    response = client.translate_text(
        Text=text,
        SourceLanguageCode=source_lang,
        TargetLanguageCode=target_lang
    )
    
    # Extracting the translated text
    translated_text = response['TranslatedText']
    return translated_text

# Text to be translated from English to Hindi
input_text = "Hi my name is Gaurav. I love to  play cricket and practice yoga. What do you like to do?"
translated_text = translate_text(input_text, 'en', 'hi')

print(f"Original: {input_text}")
print(f"Translated: {translated_text}")