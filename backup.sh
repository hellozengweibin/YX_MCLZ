#!/bin/bash


filename="aiBox.jar"
postfix=${filename##*.}

tmp=${filename##*/}
file=${tmp%.*}

backuptime=$(date "+%Y-%m-%d-%H-%M-%S")
newfile="${file}${backuptime}.${postfix}"

cp ${filename} ./bak/${newfile}
echo "backup ${filename} to ${newfile}"
