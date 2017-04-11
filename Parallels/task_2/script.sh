#!/bin/sh
#PBS -l walltime=00:00:30
#PBS -l select=1:ncpus=4:ompthreads=4

cd $PBS_O_WORKDIR
echo "OMP_NUM_THREADS = $OMP_NUM_THREADS"
echo
./a.out
