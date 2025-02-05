#!/bin/bash

# modify the config
./config.sh

# Navigate to the project directory
cd ..

./olxpbenchmark -b web3benchmark -c config/runthread1.xml  --execute=true -o thread1  | tee log/thread1.log &
./olxpbenchmark -b web3benchmark -c config/runthread2.xml  --execute=true -o thread2  | tee log/thread2.log &
./olxpbenchmark -b web3benchmark -c config/runR21.xml  --execute=true -o R21  | tee log/R21.log &
./olxpbenchmark -b web3benchmark -c config/runR22.xml  --execute=true -o R22  | tee log/R22.log &
./olxpbenchmark -b web3benchmark -c config/runR23.xml  --execute=true -o R23  | tee log/R23.log &
./olxpbenchmark -b web3benchmark -c config/runR24.xml  --execute=true -o R24  | tee log/R24.log
