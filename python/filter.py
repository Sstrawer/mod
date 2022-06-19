# coding=utf-8
import hashlib
import os
from tqdm import tqdm
from joblib import Parallel, delayed
import logging


def filter(pyfile):
    try:
        if not pyfile.endswith(".py"):
            return
        if not os.path.isfile(pyfile):
            return
        size = os.path.getsize(pyfile)
        if not 0 < size < 1048576:
            return

        myhash = hashlib.md5()
        with open(pyfile, "rb") as f:
            for line in f:
                if len(line.decode('utf-8')) > 1000:
                    return
                myhash.update(line)
        md5 = myhash.hexdigest()

        pyfile_new = f"{output_filtered}/{size}-{md5}.py"
        if os.path.exists(pyfile_new):
            return
        
        os.rename(pyfile, pyfile_new)
        logging.info(f"{pyfile} -> {pyfile_new}")

    except Exception as e:
        logging.error(f"{pyfile} -> {e}")


def file_walk(input_dir):
    for root, dirs, files in os.walk(input_dir):
        for file in files:
            yield os.path.join(root, file)


if __name__ == "__main__":
    output_source = "output"
    output_filtered = "output-test/output-filtered"

    logging.basicConfig(level=logging.INFO)

    Parallel(prefer="threads")(
        delayed(filter)(filename) for filename in file_walk(output_source)
    )

    print("Done.")
