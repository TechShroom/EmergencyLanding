#!/usr/bin/env python3
from pathlib import Path
from hashlib import sha512
from sh import convert
from itertools import chain
import os

FONT = 'AnonymousPro'
SIZE = 36
SOURCE_DIR = Path('ascii-source')
TARGET_DIR = Path('ascii-images')

def boolify(star):
    if isinstance(star, str):
        return star != '0'
    if isinstance(star, (bool, int)):
        return bool(star)
    # Throw exception?
    return bool(star)

VERBOSE = boolify(os.environ.get('MA_VERBOSE', False))

_vmsg = ['[VERBOSE]']
def vprint(*args, sep=' ', end='\n', prefix=True):
    if VERBOSE:
        if prefix:
            args = chain(_vmsg, args)
        print(*args, sep=sep, end=end)

# saved one-time arguments
most_convert = convert.bake('-size', '4000x4000', 'canvas:transparent', '-font', FONT, '-pointsize', SIZE, '-fill', 'black',
                            '-annotate', '+15+15')

def hash_file(path, hasher=None, blocksize=65536):
    hasher = hasher or sha512()
    with path.open('rb') as f:
        buf = f.read(blocksize)
        while len(buf) > 0:
            hasher.update(buf)
            buf = f.read(blocksize)
        return hasher.hexdigest()

def get_hash_file_location(path):
    return path.parent / '.mkart-hash' / path.name

def convert_file(path_from, path_to):
    stored_hash_path = get_hash_file_location(path_from)
    stored_hash_path.parent.mkdir(exist_ok=True)
    stored_hash = stored_hash_path.read_text() if stored_hash_path.exists() else ''
    current_hash = hash_file(path_from)
    if stored_hash == current_hash:
        vprint("Hash in", stored_hash_path, "matches current hash, skipping", path_from)
        return
    stored_hash_path.write_text(current_hash)
    vprint("Converting", path_from, "to", path_to, '...', end=' ')
    most_convert('@' + str(path_from), '-trim', '+repage', str(path_to))
    vprint("Done!", prefix=False)

def is_txt(p):
    b = p.name.endswith('.txt')
    if not b:
        vprint('Filtering', p)
    return b
def viter(itr):
    for f in itr:
        vprint('Iterating', f)
        yield f
def main():
    TARGET_DIR.mkdir(exist_ok=True)
    file_prefilter = SOURCE_DIR.iterdir()
    if VERBOSE:
        file_prefilter = viter(file_prefilter)
    for src_file in filter(is_txt, file_prefilter):
        try:
            convert_file(src_file, TARGET_DIR / (src_file.name[:-3] + 'png'))
        except:
            import traceback
            traceback.print_exc()

if __name__ == '__main__':
    main()
