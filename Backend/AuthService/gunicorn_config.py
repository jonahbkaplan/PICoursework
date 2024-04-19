import os
import json

config = json.load(open("config.json"), "r")

workers = int(os.environ.get('GUNICORN_WORKERS', '2'))

threads = int(os.environ.get('GUNICORN_THREADS', '4'))

bind = os.environ.get("GUNICORN_BIND", f"{config['host']}:{config['port']}")

forwarded_allow_ips = "*"

secure_scheme_headers = {"X-Forwarded-Proto": "https"}