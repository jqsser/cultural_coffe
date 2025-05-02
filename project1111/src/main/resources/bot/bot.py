import sounddevice as sd
from scipy.io.wavfile import write
import os
import google.generativeai as genai
import mysql.connector
import threading
import pygame
import time
from gtts import gTTS
from io import BytesIO
import tkinter as tk
from tkinter import messagebox
import re

# Configuration
SAMPLE_RATE = 44100
GEMINI_API_KEY = "AIzaSyCHumKm4DMrHQK6rjnjQzLSn2Z8bY-2y0c"
DB_CONFIG = {
    'host': 'localhost',
    'database': 'cafeculture',
    'user': 'root',
    'password': ''
}

# Initialize APIs
genai.configure(api_key=GEMINI_API_KEY)

# Initialize Pygame mixer
pygame.mixer.init(frequency=44100, buffer=1024)

# Global variables
is_recording = False
recorded_audio = None

# Enhanced system instruction


def fetch_table_contents():
    """Fetch the content of the `matching` table and format it into a string"""
    conn = None
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor(dictionary=True)

        cursor.execute("SELECT * FROM matching")
        matching_table_contents = cursor.fetchall()

        formatted_content = "\nTABLE matching:\n"
        for row in matching_table_contents:
            formatted_content += f"{row}\n"

        return formatted_content.strip()
    except mysql.connector.Error as e:
        print(f"Error fetching table data: {e}")
        return ""
    finally:
        if conn and conn.is_connected():
            cursor.close()
            conn.close()

# Updated system instruction for only sujet_rencontre input
table_data = fetch_table_contents()
system_instruction = f"""You are an advanced construction management AI assistant, your name is jqsserJb, and the name of the 
desktop app is Our coffee. Handle this table:

TABLE matching (id, user_id, name, sujet_rencontre, num_table)

The current content of this table is as follows:
{table_data}

The user will provide only the `sujet_rencontre` field to insert a new row.

When adding a new entry to the `matching` table:
- Always set `user_id = 35` (this is the current user ID).
- Set `nbr_personne_matchy = 4` .
- Only insert into `sujet_rencontre`.

Use this INSERT pattern:

INSERT INTO matching (user_id, name, sujet_rencontre, num_table ,nbr_personne_matchy) VALUES (35, 'actual_name', 'actual_sujet_rencontre', 'actual_num_table' , 4);

Make sure you follow this format exactly.
"""

model = genai.GenerativeModel(
    model_name="gemini-2.0-flash",
    generation_config={
        "temperature": 0.5,
        "max_output_tokens": 2048,
    },
    system_instruction=system_instruction
)

def text_to_speech(text):
    try:
        lang = 'ar' if any('\u0600' <= c <= '\u06FF' for c in text) else 'en'
        tts = gTTS(text=text, lang=lang, slow=False)
        fp = BytesIO()
        tts.write_to_fp(fp)
        fp.seek(0)
        pygame.mixer.music.load(fp)
        pygame.mixer.music.play()
        while pygame.mixer.music.get_busy():
            time.sleep(0.1)
    except Exception as e:
        print(f"Error generating speech: {str(e)}")

def start_recording():
    global is_recording, recorded_audio
    if is_recording:
        return
    is_recording = True
    print("\033[93mRecording started. Click the button to stop.\033[0m")
    recorded_audio = sd.rec(int(10 * SAMPLE_RATE), samplerate=SAMPLE_RATE, channels=1)

def stop_recording():
    global is_recording, recorded_audio
    if not is_recording:
        return
    is_recording = False
    print("\033[93mRecording stopped.\033[0m")
    sd.stop()
    output_path = "command.wav"
    write(output_path, SAMPLE_RATE, recorded_audio)
    return output_path

def execute_sql(sql):
    if not sql:
        return False

    conn = None
    try:
        conn = mysql.connector.connect(**DB_CONFIG)
        cursor = conn.cursor()
        cursor.execute(sql)
        if sql.strip().upper().startswith('SELECT'):
            results = cursor.fetchall()
            columns = [desc[0] for desc in cursor.description]
            return {"columns": columns, "data": results}
        else:
            conn.commit()
            return {"affected_rows": cursor.rowcount}
    except mysql.connector.Error as err:
        print(f"\033[91mSQL Error: {err}\033[0m")
        if conn:
            conn.rollback()
        return {"error": str(err)}
    finally:
        if conn and conn.is_connected():
            cursor.close()
            conn.close()

def format_results(query_result):
    if 'error' in query_result:
        return f"Database error: {query_result['error']}"
    if 'affected_rows' in query_result:
        return f"Operation completed. Affected {query_result['affected_rows']} records."
    if 'data' in query_result:
        summary = []
        for row in query_result['data'][:5]:
            summary.append(", ".join([f"{col}: {val}" for col, val in zip(query_result['columns'], row)]))
        return "Here are the results: " + ". ".join(summary)
    return "Operation successful"

import re  # Add this if not already at the top

def process_command(audio_path):
    """Full command processing pipeline"""
    try:
        current_table_data = fetch_table_contents()
        
        dynamic_model = genai.GenerativeModel(
            model_name="gemini-2.0-flash",
            generation_config={
                "temperature": 0.5,
                "max_output_tokens": 2048,
            },
            system_instruction=system_instruction.replace(
                "CURRENT TABLE DATA PLACEHOLDER",
                current_table_data
            )
        )
        
        audio_file = genai.upload_file(audio_path)
        response = dynamic_model.generate_content(
            [f"Current database state:\n{current_table_data}\nProcess this command:", audio_file]
        )

        sql = ""
        message = response.text.strip()

        # Try extracting name and sujet_rencontre manually
        matches = re.findall(r"'([^']+)'", response.text)
        
        if len(matches) >= 2:
            name = matches[0].replace("'", "''")
            sujet = matches[1].replace("'", "''")
            table = matches[2].replace("'", "''")
            sql = f"INSERT INTO matching (user_id, name, sujet_rencontre, num_table,nbr_personne_matchy) VALUES (35, '{name}', '{sujet}', {table},4);"

        # Execute the SQL if it's valid
        result = None
        if sql and sql != ";":
            valid_commands = ["SELECT", "INSERT", "UPDATE", "DELETE", "CREATE", "DROP", "ALTER", "TRUNCATE"]
            first_word = sql.split()[0].upper() if sql else ""

            if first_word in valid_commands:
                result = execute_sql(sql)
                if result and 'error' not in result:
                    formatted_result = format_results(result)
                    full_response = f"{message}\n{formatted_result}"
                else:
                    full_response = f"{message}\nError executing SQL: {result.get('error', 'Unknown error')}"
            else:
                full_response = f"{message}\nNote: SQL command was not executed as it was invalid."
        else:
            full_response = f"{message}\nNote: Could not generate a valid SQL command."

        print(f"\033[94mASSISTANT:\033[0m {full_response}")
        threading.Thread(target=text_to_speech, args=(full_response,)).start()

    except Exception as e:
        error_msg = f"Error processing command: {str(e)}"
        print(f"\033[91mERROR:\033[0m {error_msg}")
        threading.Thread(target=text_to_speech, args=(error_msg,)).start()

def on_button_click():
    global is_recording
    if is_recording:
        audio_path = stop_recording()
        process_command(audio_path)
    else:
        start_recording()

def main():
    print("\033[1;36mConstruction Voice Assistant Ready\033[0m")
    text_to_speech("Ready for construction site commands")

    root = tk.Tk()
    root.title("Construction Voice Assistant")
    root.geometry("300x150")
    root.configure(bg="black")

    button = tk.Button(root, text="Start Recording", font=("Arial", 14), bg="yellow", fg="black", command=on_button_click)
    button.pack(expand=True, fill="both", padx=20, pady=20)

    def update_button_text():
        if is_recording:
            button.config(text="Stop Recording", bg="lightcoral", fg="black")
        else:
            button.config(text="Start Recording", bg="yellow", fg="black")
        root.after(100, update_button_text)

    update_button_text()
    root.mainloop()

if __name__ == "__main__":
    main()
