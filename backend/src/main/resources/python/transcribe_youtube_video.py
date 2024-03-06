import sys
import hashlib
import whisper
import os
from pytube import YouTube


def format_item(item):
    return {
        "time": item['start'],
        'text': item['text']
    }


#################################################################################################
# Download video - use MD5 to hash the file name when saving the video that has been downloaded.#
# This file we go to the 'temp_video_path' on project. This will later change to the YouTube    #
# videoId as the file name.

# note: brew install ffmpeg
#################################################################################################

def download_video(url, temp_path):
    yt = YouTube(url)
    hash_file = hashlib.md5()
    hash_file.update(yt.title.encode())
    file_name = f'{hash_file.hexdigest()}.mp4'
    yt.streams.first().download(temp_path, file_name)
    transcribe_video_file = '{}/{}'.format(temp_path, file_name)
    print('transcribing from file: ', transcribe_video_file)

    return {
        'file_name': transcribe_video_file,
        'title': yt.title
    }


def transcribe(url, temp_path):
    video = download_video(url, temp_path)
    transcribe_result = model.transcribe(video['file_name'])
    os.remove(video['file_name'])

    segments = []
    for item in transcribe_result['segments']:
        segments.append(format_item(item))

    # Must print segments for PythonScriptEngine input stream processing
    # Input stream will be read ny engine and parsed into a java.util.List
    for segment in segments:
        print(segment)


# get youtube url from args
youtube_url = sys.argv[1]
temp_video_path = sys.argv[2]
print('PythonScriptEngine received youtube url : ', youtube_url)
print('Temp video will be stored to path: ', temp_video_path)

model_name = 'base.en'
model = whisper.load_model(model_name)
transcribe(youtube_url, temp_video_path)
