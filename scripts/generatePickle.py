from collections import OrderedDict
from datetime import date
import pickle
import sys

def java_string_hashcode(s):
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

def main():
    """Read today's log file and generate a hashed pickle file"""
    today = date.today()
    with open('log/LogFileGenerator.' + str(today) + '.log', 'r') as f:
        d = OrderedDict()
        lines = f.readlines()
        for line in lines:
            hashCode = java_string_hashcode(line[:2])
            try:
                d[hashCode].append(line)
            except KeyError:
                d[hashCode] = list()
                d[hashCode].append(line)
    
    with open('pkl/LogFileGenerator.' + str(today) + '.pkl', 'wb') as f:
        pickle.dump(d, f)

if __name__ == "__main__":
    main()

