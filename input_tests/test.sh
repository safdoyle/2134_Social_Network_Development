#!/bin/sh

PREFIX=test
TESTS='00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16'
PROG=FriendRecommender
count=0
DEBUG=0

if [ "$1" == "debug" ]; then
  let "DEBUG=1"
fi

if [ ! -f $PROG.java ]; then
  echo Error: $PROG.java is not in the current directory 
  exit 1
fi

javac $PROG.java 
if [ $? -eq 0 ]; then 
  for T in $TESTS; do
    if [ -f $PREFIX.$T.in ]; then  
      echo ===========================================
      echo Test file: $PREFIX.$T.in 
      java $PROG < $PREFIX.$T.in > $PREFIX.$T.out
      if diff --strip-trailing-cr $PREFIX.$T.out $PREFIX.$T.gold > /dev/null; then
        echo " " PASSED
        let "count = count + 1"
      elif [ $DEBUG -eq 1 ]; then
        echo Test $T failed, here are the differences
        echo My program output "                     " Expected program output
        echo ================= "                     " =======================
        diff --strip-trailing-cr -W 80 -y $PREFIX.$T.out $PREFIX.$T.gold
        exit 1
      else
        echo " " FAILED
      fi
      # rm $PREFIX.$T.out
    else
      echo Oops: file $PREFIX.$T.in does not exist!
    fi
  done
fi

echo =============================================
echo $count tests passed
