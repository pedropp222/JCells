# JCells
- CLI csv editor
- Includes 2 csv files for testing purposes
# Running
Build program then 
```bash
/.run.bat fileName
```
# Features
- CSV Cell parsing (needs ',' to separate cells)
- Expression parsing end evaluation (only plus operator)
- Simple operations

## Commands
```bash
print
```
- Prints the table of literal values of the cells
```bash
printe
```
- Prints the table of evaluated expression values
```bash
cv x y value
cv POSITION value
```
- Changes value of cell at X,Y or Position(eg. A1, B5, etc.) to value (only to int and string)
```bash
expr POSITION
```
- Evaluates and prints value of expression at position

## Issues
- Many
