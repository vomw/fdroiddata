import subprocess, glob, os

PROJECT = r'c:\Users\Administrator\Desktop\mo-todo'
os.chdir(PROJECT)

# Get all image files that were changed in 420f7cdb96
r = subprocess.run('git show 420f7cdb96 --name-only --pretty=format:', 
                    shell=True, capture_output=True, text=True, cwd=PROJECT)

all_files = [l.strip() for l in r.stdout.split('\n') if l.strip()]
img_files = [f for f in all_files if f.lower().endswith(('.jpg', '.jpeg', '.png'))]

print(f'Restoring {len(img_files)} image files...')
for f in img_files:
    subprocess.run(['git', 'checkout', '9a96d756d6', '--', f], 
                   capture_output=True, cwd=PROJECT)

print('Done')
