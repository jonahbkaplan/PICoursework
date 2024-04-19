import os
import json

auth_config = json.load(open("auth_config.json"))

workers = int(os.environ.get('GUNICORN_WORKERS', '2'))

threads = int(os.environ.get('GUNICORN_THREADS', '4'))

bind = os.environ.get("GUNICORN_BIND", f"{auth_config['host']}:{auth_config['port']}")

forwarded_allow_ips = "*"

secure_scheme_headers = {"X-Forwarded-Proto": "https"}