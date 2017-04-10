#!/bin/bash
#PBS -q S3432065
#PBS -l walltime=00:02:00
#PBS -l select=2:ncpus=8:mpiprocs=8:mem=2000m,place=scatter
#PBS -m n

cd $PBS_O_WORKDIR
MPI_NP=$(wc -l $PBS_NODEFILE | awk '{ print $1 }')

echo ""Number of MPI process: $MPI_NP""
echo 'File $PBS_NODEFILE:'
cat  $PBS_NODEFILE
echo

# use key -machinefile for Intel MPI
# mpirun -machinefile $PBS_NODEFILE -np $MPI_NP ./a.out
# use key -hostfile for OpenMPI
mpirun -hostfile $PBS_NODEFILE -np $MPI_NP ./a.out
