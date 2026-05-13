import os
import re

base_dir = r"c:\Users\User\Midaxus-1.0\src\main\resources"
static_dir = os.path.join(base_dir, "static")
templates_dir = os.path.join(base_dir, "templates")

if not os.path.exists(templates_dir):
    os.makedirs(templates_dir)

replacements = [
    (r'href="css/', r'href="/css/'),
    (r'src="js/', r'src="/js/'),
    (r'src="img/', r'src="/img/'),
    (r'href="login\.html"', r'href="/login"'),
    (r'href="register\.html"', r'href="/register"'),
    (r'href="index\.html"', r'href="/"'),
    (r'href="forgotpassword\.html"', r'href="/forgotpassword"'),
    (r'href="reset-password\.html"', r'href="/reset-password"'),
    (r'href="landing\.html"', r'href="/"'),
    (r'= "login\.html"', r'= "/login"'),
    (r'= "index\.html"', r'= "/"'),
]

for filename in os.listdir(static_dir):
    if filename.endswith(".html"):
        filepath = os.path.join(static_dir, filename)
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()

        for old, new in replacements:
            content = re.sub(old, new, content)

        new_filepath = os.path.join(templates_dir, filename)
        with open(new_filepath, 'w', encoding='utf-8') as f:
            f.write(content)
        
        os.remove(filepath)
        print(f"Migrated {filename}")
