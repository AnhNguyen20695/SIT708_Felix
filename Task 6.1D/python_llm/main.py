import os
from config import *
import requests
from flask import Flask, request, jsonify
import re

app = Flask(__name__)

# API setup
os.environ["HF_HOME"] = HF_CACHE_LOCATION
API_URL = "https://router.huggingface.co/novita/v3/openai/chat/completions"
HF_API_TOKEN = os.getenv('HUGGING_FACE_LLAMA_TOKEN', '')  # Use env var or default
os.environ["HF_HOME"] = HF_CACHE_LOCATION
HEADERS = {"Authorization": f"Bearer {HF_API_TOKEN}"}
MODEL = "deepseek/deepseek-v3-0324"
# MODEL = "meta-llama/Llama-2-7b-chat-hf"
# MODEL = "google/gemma-3-1b-it"

def fetchQuizFromLlama(student_topic):
    print("Fetching quiz from Hugging Face router API")
    payload = {
        "messages": [
            {
                "role": "user",
                "content": (
                    f"Generate a quiz with 3 questions to test students on the provided topic. "
                    f"For each question, generate 4 options where only one of the options is correct. "
                    f"Format your response as follows:\n"
                    f"**QUESTION 1:** [Your question here]?\n"
                    f"**OPTION A:** [First option]\n"
                    f"**OPTION B:** [Second option]\n"
                    f"**OPTION C:** [Third option]\n"
                    f"**OPTION D:** [Fourth option]\n"
                    f"**ANS:** [Correct answer letter]\n\n"
                    f"**QUESTION 2:** [Your question here]?\n"
                    f"**OPTION A:** [First option]\n"
                    f"**OPTION B:** [Second option]\n"
                    f"**OPTION C:** [Third option]\n"
                    f"**OPTION D:** [Fourth option]\n"
                    f"**ANS:** [Correct answer letter]\n\n"
                    f"**QUESTION 3:** [Your question here]?\n"
                    f"**OPTION A:** [First option]\n"
                    f"**OPTION B:** [Second option]\n"
                    f"**OPTION C:** [Third option]\n"
                    f"**OPTION D:** [Fourth option]\n"
                    f"**ANS:** [Correct answer letter]\n\n"
                    f"Ensure text is properly formatted. It needs to start with a question, then the options, and finally the correct answer. "
                    f"Follow this pattern for all questions. "
                    f"Here is the student topic:\n{student_topic}"
                )
            }
        ],
        "model": MODEL,
        "max_tokens": 500,
        "temperature": 0.7,
        "top_p": 0.9
    }

    response = requests.post(API_URL, headers=HEADERS, json=payload)
    if response.status_code == 200:
        result = response.json()["choices"][0]["message"]["content"]
        # print(result)
        return result
    else:
        raise Exception(f"API request failed: {response.status_code} - {response.text}")
    

def recommendQuizFromLlama(user_records):
    print("Fetching quiz from Hugging Face router API")
    print("user records: ",user_records)
    payload = {
        "messages": [
            {
                "role": "user",
                "content": (
                    f"Given a list of the scores for each topic, recommend a topic for a student to improve his score."
                    f"The student needs to improve his score for the topic with the lowest score in the list."
                    f"If there are more than one topic with the lowest score, randomly choose a topic from the list of topics with the lowest score."
                    f"Format your response in less than 5 words with only the name of the topic that you recommend."
                    f"Here is the student's list of the scores for each topic: [ {user_records} ]"
                )
            }
        ],
        "model": MODEL,
        "max_tokens": 500,
        "temperature": 0.7,
        "top_p": 0.9
    }

    response = requests.post(API_URL, headers=HEADERS, json=payload)
    if response.status_code == 200:
        result = response.json()["choices"][0]["message"]["content"]
        # print(result)
        return result
    else:
        raise Exception(f"API request failed: {response.status_code} - {response.text}")

ANS_LETTER_MAPPING = {
    "A":0,
    "B":1,
    "C":2,
    "D":3
}


def process_quiz(quiz_text):
    # questions = []
    questions = {}
    # Updated regex to match bolded format with numbered questions
    pattern = re.compile(
        r'\*\*QUESTION \d+:\*\* (.+?)\n'
        r'\*\*OPTION A:\*\* (.+?)\n'
        r'\*\*OPTION B:\*\* (.+?)\n'
        r'\*\*OPTION C:\*\* (.+?)\n'
        r'\*\*OPTION D:\*\* (.+?)\n'
        r'\*\*ANS:\*\* (.+?)(?=\n|$)',
        re.DOTALL
    )
    matches = pattern.findall(quiz_text)

    for match in matches:
        question = match[0].strip()
        options = [match[1].strip(), match[2].strip(), match[3].strip(), match[4].strip()]
        correct_ans = match[5].strip()

        # question_data = {
        #     "question": question,
        #     "options": options,
        #     "correct_answer": correct_ans
        # }
        # questions.append(question_data)
        questions[question] = {}
        for option_index in range(len(options)):
            questions[question][options[option_index]] = "true" if (option_index == ANS_LETTER_MAPPING.get(correct_ans)) else "false"

    return questions

@app.route('/getQuiz', methods=['GET'])
def get_quiz():
    print("Request received")
    student_topic = request.args.get('topic')
    if not student_topic:
        return jsonify({'error': 'Missing topic parameter'}), 400
    try:
        quiz = fetchQuizFromLlama(student_topic)
        # print(quiz)
        processed_quiz = process_quiz(quiz)
        if not processed_quiz:
            return jsonify({'error': 'Failed to parse quiz data', 'raw_response': quiz}), 500
        return jsonify({'quiz': processed_quiz}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/recommendTopic', methods=['GET'])
def recommend_topic():
    print("Request received")
    print("Request args: ",request.args)
    user_records = []
    for key in request.args.keys():
        user_records.append(f"{key}:{request.args.get(key)}")
    # student_topic = request.args.get('topic')
    # if not student_topic:
    #     return jsonify({'error': 'Missing topic parameter'}), 400
    try:
        topic = recommendQuizFromLlama(", ".join(user_records))
        print(topic)
        if not topic:
            return jsonify({'error': 'Failed to parse quiz data', 'raw_response': topic}), 500
        return jsonify({'topic': topic}), 200
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/test', methods=['GET'])
def run_test():
    return jsonify({'quiz': "test"}), 200

if __name__ == '__main__':
    port_num = 5000
    print(f"App running on port {port_num}")
    app.run(port=port_num, host="0.0.0.0")
