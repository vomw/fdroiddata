import subprocess, os

PROJECT = r'c:\Users\Administrator\Desktop\mo-todo'
os.chdir(PROJECT)

# Get list of image files changed in commit 420f7cdb96
r = subprocess.run(['git', 'show', '420f7cdb96', '--name-only', '--pretty=format:'], 
                    capture_output=True, text=True)

all_files = r.stdout.strip().split('\n')
img_exts = ('.jpg', '.jpeg', '.png')
img_files = [f for f in all_files if f.lower().endswith(img_exts) and f.strip()]
print(f'Found {len(img_files)} image files to restore')

# Also need to restore .gitlab-ci.yml
files_to_restore = img_files + ['.gitlab-ci.yml']
print(f'Restoring {len(files_to_restore)} files total from upstream (9a96d756d6)')

for f in files_to_restore:
    result = subprocess.run(['git', 'checkout', '9a96d756d6', '--', f],
                           capture_output=True, text=True)
    if result.returncode != 0:
        print(f'  FAILED: {f} - {result.stderr.strip()}')
    else:
        print(f'  OK: {f}')

print('\nDone. Checking git status...')
r = subprocess.run(['git', 'status', '--short'], capture_output=True, text=True)
print(r.stdout[:2000])
