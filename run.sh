function check_java(){
   version=$(java -version 2>&1 |grep version |awk '{print $3}')
   len=${#version}-2
   version=${version:1:len}

   IFS='.' arr=($version)
   IFS=' '
   if [ -z ${arr[0]} ];then
      LOG_ERROR "At least Java8 is required."
      exit 1
   fi
   if [ ${arr[0]} -eq 1 ];then
      if [ ${arr[1]} -lt 8 ];then
           LOG_ERROR "At least Java8 is required."
           exit 1
      fi
   elif [ ${arr[0]} -gt 8 ];then
          :
   else
       LOG_ERROR "At least Java8 is required."
       exit 1
   fi
}

function check_sol_dir(){
  SOL_DIR="contracts"
  if [ ! -d "$SOL_DIR" ]; then
    echo " \" ${SOL_DIR} \" directory not exists. Please copy solidity files here"
    exit 0
  fi
}

function check_conf_dir(){
  CONF_DIR="conf"
  if [ ! -d "$CONF_DIR" ]; then
    echo "\"${CONF_DIR} \" directory not exists. Please copy certs and config to this folder. See https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk/configuration.html for more info"
    exit 0
  fi
}

function clean(){
  rm -rf dist
  mkdir dist
  rm -rf artifacts
  mkdir artifacts
}


check_java
check_sol_dir
check_conf_dir
clean

GROUP=${1}
ARTIFACT=${2}
if [ -z "$GROUP" ]; then GROUP="org.example"; fi
if [ -z "$ARTIFACT" ]; then ARTIFACT="demo"; fi

echo group name : $GROUP
echo artifact name: $ARTIFACT

cd scaffold-cmd
gradle build
cd ..
cp -r scaffold-cmd/dist/* dist
cd dist
java -jar scaffold-cmd.jar -g $GROUP -a $ARTIFACT -s ../$SOL_DIR -c ../$CONF_DIR -o ../artifacts



#java -cp "apps/*:lib/*:conf/" console.common.ConsoleUtils $@


