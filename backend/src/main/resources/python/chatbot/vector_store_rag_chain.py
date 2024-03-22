from pymongo import MongoClient
from langchain_openai import OpenAIEmbeddings
from langchain_mongodb import MongoDBAtlasVectorSearch
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.runnables import RunnablePassthrough
from langchain_core.output_parsers import StrOutputParser
import sys

MONGODB_URI = sys.argv[1]
OPENAI_API_KEY = sys.argv[2]
QUERY = sys.argv[3]

# Initialize MongoDB Python Client
client = MongoClient(MONGODB_URI)
DB_NAME = "sample_restaurants"
COLLECTION_NAME = "youtube_videos"
ATLAS_VECTOR_SEARCH_INDEX_NAME = "youtube_vector_search"
collection = client[DB_NAME][COLLECTION_NAME]

# Initialize Vector Store
embeddings = OpenAIEmbeddings(openai_api_key=OPENAI_API_KEY, model="text-embedding-ada-002")
vector_store = MongoDBAtlasVectorSearch.from_connection_string(
    connection_string=MONGODB_URI,
    namespace=DB_NAME + "." + COLLECTION_NAME,
    embedding=embeddings,
    embedding_key="transcriptEmbeddings",
    index_name=ATLAS_VECTOR_SEARCH_INDEX_NAME,
    text_key="transcript"
)
retriever = vector_store.as_retriever(search_type="similarity", search_kwargs={"k": 5})

# Generate context using the retriever, and pass the user question through
retrieve = {
    "context": retriever | (lambda docs: "\n\n".join([d.page_content for d in docs])),
    "question": RunnablePassthrough()
}

template = """Answer the question based only on the following context: \
{context}

Question: {question}
"""
# Define the chat prompt
prompt = ChatPromptTemplate.from_template(template)

# Define the model used for chat completion
model = ChatOpenAI(temperature=0, openai_api_key=OPENAI_API_KEY)

# Parse output as string
parse_output = StrOutputParser()

# Naive RAG chain
naive_rag_chain = (
    retrieve
    | prompt
    | model
    | parse_output
)

output = naive_rag_chain.invoke(QUERY)
print(output)
