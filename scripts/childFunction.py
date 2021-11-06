import re
import sys
import json
import pickle
import boto3
import hashlib
from collections import OrderedDict
from datetime import date

def lambda_handler(event, context):
    """Handler for child lambda"""
    method = event['method']
    time = event['logTime']
    today = date.today()
    s3 = boto3.resource('s3')
    bucket = "logawsbucket"
    key = "log/LogFileGenerator." + str(today) + ".pkl"
    
    # load the pickle file from s3 bucket
    logDict = pickle.loads(s3.Bucket(bucket).Object(key).get()['Body'].read())

    # If the method is 'getLogs'  - implies request comes from REST server
    if method == 'getlogs':
        minus = event['minus']
        plus = event['plus']
        pattern = event['pattern']
        md5 = False if event['md5'] == 'null' else True
        logs = getLogs(time, minus, plus, pattern, logDict, md5)
        return json.dumps({"logs": logs})

    return find(time, logDict)
        

def find(time, logDict):
    """Searches for the given time stamp"""
    try: 
        arr = logDict[hashcode_gen(time[:2])]
    except KeyError:
        arr = []
    idx, found = binarySearch(arr, time)
    return found

def getLogs(time, minus, plus, pattern, logDict, md5):
    """Searches for the range of time stamps"""
    minus_int = int(minus[:2])
    plus_int = int(plus[:2])
    
    if minus_int > plus_int:
        minus_int = 0
        minus = "00" + minus[2:]
    if plus_int < minus_int:
        plus_int = 23
        plus = "23" + plus[2:]
        
    keys = []

    # Convert range of time to list of keys 
    # Example: 00:00:00.000 to 06:00:00.000 will become
    # ["00", "01", "02", "03", "04", "05", "06"]
    for i in range(minus_int, plus_int+1):
        keys.append('0' + str(i) if len(str(i)) == 1 else str(i))
    
    return_list = list()
    for key in keys:
        try: 
            arr = logDict[hashcode_gen(key)]
        except KeyError:
            arr = []
        
        # call modified binary search twice - one for upper limit and one for lower limit
        lb = binarySearchModified(arr, minus, plus, "lb")
        ub = binarySearchModified(arr, minus, plus, "ub")

        p = re.compile(pattern)
        
        # elegant one-liner to find matching patterns - python is too good
        res = filter(lambda s: bool(p.match(s.split()[-1])), arr[lb:ub+1])

        # if md5 flag is set generate md5 hash
        if md5:
            return_list.extend(md5gen(list(res)))
        else:
            return_list.extend(list(res))
    
    return return_list

def md5gen(arr):
    """Generate md5 hash"""
    ret = []
    for s in arr:
        ret.append(hashlib.md5(s.encode('utf-8')).hexdigest())
    return ret

def binarySearch(arr, time):
    """Perform binary search to look for a time stamp"""
    top = 0
    bottom = len(arr)-1
    idx = 0
    found = False

    while top <= bottom and not found:
        mid = (top + bottom) // 2
        time_in_log = arr[mid].split()[0]
        if time_in_log == time:
            idx = mid
            found = True
        else:
            if time_in_log > time:
                bottom = mid - 1
            else:
                top = mid + 1
    return (idx, found)

def binarySearchModified(arr, minus, plus, flag):
    """Perform binary search to look for upper and lower limit within the specified range"""
    top = 0
    bottom = len(arr)-1
    idx = -1

    while top <= bottom:
        mid = (top + bottom) // 2
        time_in_log = arr[mid].split()[0]
        if minus < time_in_log and time_in_log < plus:
            idx = mid
            # Key modification in binary search
            # for finding lower and upper limit
            if flag == "lb":
                bottom = mid - 1
            elif flag == "ub":
                top = mid + 1
        else:
            if time_in_log > plus:
                bottom = mid - 1
            elif time_in_log < minus:
                top = mid + 1
    return idx


def hashcode_gen(s):
    """Mimic Java's hashCode in python"""
    if sys.version_info[0] >= 3:
        unicode = str
    try:
        s = unicode(s)
    except:
        try:
            s = unicode(s.decode('utf8'))
        except:
            raise Exception("Please enter a unicode type string or utf8 bytestring.")
    h = 0
    for c in s:
        h = int((((31 * h + ord(c)) ^ 0x80000000) & 0xFFFFFFFF) - 0x80000000)
    return h

