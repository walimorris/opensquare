from openai import OpenAI
import sys


#################################################################################################
# Call OpenAi embedding API - passing key and text.                                             #
# example embedding: [0.0043171728029847145, 0.0006816589157097042,...]                         #
#                                                                                               #
#################################################################################################

def get_text_embeddings_ada_002(key, text_input):
    client = OpenAI(api_key=key)
    response = client.embeddings.create(
        input=text_input,
        model='text-embedding-ada-002'
    )
    print(response.data[0].embedding)


# get
openai_key = sys.argv[1]
text = sys.argv[2]

print('text: ', text)

get_text_embeddings_ada_002(openai_key, text)
