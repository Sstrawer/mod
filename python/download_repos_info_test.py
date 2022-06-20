import math
import time
from joblib import Parallel, delayed
import requests

USER = "sstrawer"
# TOKEN = "14d353dfb27b03c5de0cbe56bab154cf6713dde2"
TOKEN = "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"


def save_repo(res):
    with open("repos-nofork-starsgt1-80w.csv", "a") as repo_file:
        for repository in res:
            name = repository["full_name"]
            stars = repository["stargazers_count"]
            lang = repository["language"]
            repo_file.write(f"{name},{stars},{lang}\n")


def get_page(a, b, page=1):
    print(f"get page {a} {b}")
    a = time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime(a))
    b = time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime(b))
    while True:
        try:
            response = requests.get(
                f"https://api.github.com/search/repositories?q=created:{a}..{b}+language:python+stars:>1&per_page=100&page={page}",
                auth=(USER, TOKEN),
                proxies={"http": "http://127.0.0.1:7788", "https": "http://127.0.0.1:7788"},
            )

            # if response.status_code == 403:
            #     time.sleep(30)
            if response.status_code == 200:
                res = response.json()
                return res
            else:
                time.sleep(30)
        except:
            time.sleep(30)


def get_condition(a, b, total, head100=[]):
    print(f"range {a},{b} : {total} repos found")
    condition_res = head100
    for page in range(1, math.ceil(total / 100) + 1):
        res = get_page(a, b, page=page)
        condition_res += res["items"]
    save_repo(condition_res)


def get_day(a, b):
    print(f"get day {a} {b}")
    res = get_page(a, b)
    total = res["total_count"]
    head100 = res["items"]
    if total < 1000:
        get_condition(a, b, total, head100=head100)
    else:
        shard_count = math.ceil(total / 1000)
        shard = (b - a) / shard_count
        for i in range(1, shard_count + 1):
            shard_start = shard * (i - 1) + a
            shard_end = shard * i + b
            get_page(shard_start, shard_end, head100=head100)
            head100 = None


def get_time_range(a, b):
    while a < b:
        get_day(a, a + 86400)
        a += 86400.001


if __name__ == "__main__":
    get_time_range(1201795200.0, 1655741180.2441275)
