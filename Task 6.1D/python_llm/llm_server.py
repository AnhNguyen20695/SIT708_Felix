# Set env variables
import os
from config import *
hf_secret_key = os.environ.get("HUGGING_FACE_LLAMA_TOKEN","")
os.environ["HF_HOME"] = HF_CACHE_LOCATION


# Start script
import torch
from transformers import AutoTokenizer, AutoModelForCausalLM, BitsAndBytesConfig, pipeline
from huggingface_hub import login
import os

model_id = MODEL_ID
# Login to HuggingFace
# login()

bnb_config = BitsAndBytesConfig(
    load_in_4_bit=True,
    bnb_4bit_use_double_quant=True,
    bnb_4bit_quant_type="nf4",
    bnb_4bit_compute_dtype=torch.bfloat16
)

tokenizer = AutoTokenizer.from_pretrained(model_id)
tokenizer.pad_token = tokenizer.eos_token
model = AutoModelForCausalLM.from_pretrained(
    model_id,
    device_map="auto",
    # quantization_config=bnb_config
)

text_generator = pipeline(
    task="text-generation",
    model=model,
    tokenizer=tokenizer,
    max_new_tokens=30,
)


def get_response(prompt):
    response = text_generator(prompt,
                              truncation=True,
                              max_length=30)
    gen_text = response[0]['generated_text']
    return gen_text

prompt = "Explain who is Alex Ferguson"
llama_response = get_response(prompt)
print(llama_response)
