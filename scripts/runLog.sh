#!/bin/bash
cd LogFileGenerator-1
sbt run
python3 generatePickle.py
aws s3 cp pkl s3://logawsbucket/log --recursive
cd ~
echo "Completed..."
