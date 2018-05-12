set -o nounset
reset
javac -nowarn GameSolver.java
java GameSolver $1
