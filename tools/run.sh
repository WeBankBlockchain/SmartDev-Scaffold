#!/usr/bin/env bash
LANG=zh_CN.UTF-8

function LOG_ERROR()
{
    local content=${1}
    echo -e "\033[31m"${content}"\033[0m"
}

function check_java(){
   version=$(java -version 2>&1 |grep version |awk '{print $3}')
   len=${#version}-2
   version=${version:1:len}

   IFS='.' arr=($version)
   IFS=' '
   if [ -z ${arr[0]} ];then
      echo "At least Java8 is required."
      exit 1
   fi
   if [ ${arr[0]} -eq 1 ];then
      if [ ${arr[1]} -lt 8 ];then
           echo "At least Java8 is required."
           exit 1
      fi
   elif [ ${arr[0]} -gt 8 ];then
          :
   else
       echo "At least Java8 is required."
       exit 1
   fi
}

function check_sol_dir(){
  SOL_DIR="$(pwd)/contracts"
  if [ ! -d "$SOL_DIR" ]; then
    echo " \" ${SOL_DIR} \" directory not exists. Please copy solidity files here"
    exit 0
  fi
  if [ ! "$(ls -A $SOL_DIR)" ]; then
    echo " \" ${SOL_DIR} \" directory is empty. Please copy solidity files here"
    exit 0
  fi
}

check_java
check_sol_dir
TOOLS_DIR=$(pwd)
ARTIFACT=$(grep artifact config.ini |  cut  -d '=' -f 2)
GROUP=$(grep group config.ini |  cut  -d '=' -f 2)
SELECTOR=$(grep selector config.ini |  cut  -d '=' -f 2)
COMPILER=$(grep compiler config.ini |  cut  -d '=' -f 2)
GRADLEVERSION=$(grep gradleVersion config.ini |  cut  -d '=' -f 2)
echo "GROUP=$GROUP"
echo "ARTIFACT=$ARTIFACT"
echo "SOL_DIR=$SOL_DIR"
echo "TOOLS_DIR=$TOOLS_DIR"
echo "SELECTOR=$SELECTOR"
echo "COMPILER=$COMPILER"
echo "GRADLEVERSION=$GRADLEVERSION"
ARTIFACT_DIR="$(pwd)/$ARTIFACT"
if [ -d "$ARTIFACT_DIR" ]; then
   echo "\"$ARTIFACT_DIR \" directory ALREADY exists. Please remove it if you want to override"
   exit 0
fi


echo start compiling scaffold...


cd ..
./gradlew clean shadowJar -PsolcVersion=$COMPILER
echo end compiling scaffold...

echo start generating $ARTIFACT...

if [ -z "$SELECTOR" ]; then
  java -jar dist/WeBankBlockchain-SmartDev-Scaffold.jar -g $GROUP -a $ARTIFACT -s $SOL_DIR -o $TOOLS_DIR -gv $GRADLEVERSION
else
  java -jar dist/WeBankBlockchain-Smartdev-Scaffold.jar -g $GROUP -a $ARTIFACT -s $SOL_DIR -o $TOOLS_DIR -gv $GRADLEVERSION -n $SELECTOR
fi



