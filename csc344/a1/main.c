#include <stdio.h>
#include <string.h>
#include <stdlib.h>

struct cell {
    int data;
    struct cell *prev;
    struct cell *next;
};

struct Instruction {
    char writeVal;
    char moveDir;
    int newState;
};
struct Line {
    char line[50];
};


int fileRows(FILE *inputFile, char fileName[50]);

void processInputFile(FILE **allocateFile, int *numberOfRows);

void populateInstructionTable(int numberOfRows, int numberOfStates);

void readWriteHead(int tapeContent);

struct cell *addNewCell(int tapeContent);

void printTapeContents();

void processInitialTape();

void writingFinalTape(int endState, int currentState, int counter);

// Global Variables
struct Line **lines;
struct Instruction **instructions;
int const INSTRUCTION_INDEX = 4;
struct cell *head;
struct cell *tail;
struct cell *currentCell;

int main() {

    FILE *allocateFile;
    int numberOfRows;
    int endState;
    int startState;
    int currentState;
    int counter;
    int numberOfStates;

    processInputFile(&allocateFile, &numberOfRows);

    numberOfStates = atoi(lines[1][0].line);
    startState = atoi(lines[2][0].line);
    endState = atoi(lines[3][0].line);

    populateInstructionTable(numberOfRows, numberOfStates);

    processInitialTape();


    free(lines);
    lines = NULL;

    currentState = startState;


    currentCell = head;

    printf("Writing tape.......\n");
    printf("Initial tape contents: ");
    printTapeContents();
    printf("\n\nSimulation, in Detail\n");
    printf("\n");
    printf("State %d\n", currentState);
    printTapeContents();
    printf("\n");
    printf("^\n");

    counter = 1;
    writingFinalTape(endState, currentState, counter);
    printf("\nFinal tape contents: ");
    printTapeContents();

    // Don't touch this
    free(head);
    head = NULL;
    free(tail);
    tail = NULL;
    free(currentCell);
    currentCell = NULL;
    free(lines);
    lines = NULL;
    return 0;

}

int fileRows(FILE *inputFile, char fileName[50]) {
    int counter = 0;
    fscanf(inputFile, "%s", fileName);
    while ((fgets(fileName, 50, (FILE *) inputFile))) {
        counter++;
    }
    fclose(inputFile);
    return counter;
}

void processInputFile(FILE **allocateFile,
                      int *numberOfRows) {
    FILE *inputFile;
    char fileName[50];
    char initialFile[50];
    int counter = 0;

    printf("\nInput file: ");
    scanf("%s", fileName);
    strcpy(initialFile, fileName);

    inputFile = fopen(fileName, "r");
    (*allocateFile) = fopen(initialFile, "r");

    (*numberOfRows) = fileRows(inputFile, fileName);

    (lines) = (struct Line **) malloc((*numberOfRows) * sizeof(struct Line *));
    for (int i = 0; i < (*numberOfRows); i++) {
        (lines)[i] = (struct Line *) malloc(1 * sizeof(struct Line));
    }

    while ((fgets(initialFile, 50, (FILE *) (*allocateFile)))) {
        strcpy((lines)[counter][0].line, initialFile);
        counter++;
    }

    fclose(*allocateFile);
}

void populateInstructionTable(int numberOfRows, int numberOfStates) {

    int const standard_ASCII_Char = 128;
    instructions = (struct Instruction **) malloc(numberOfStates * sizeof(struct Instruction *));
    for (int i = 0; i < standard_ASCII_Char; i++) {
        instructions[i] = (struct Instruction *) malloc(standard_ASCII_Char * sizeof(struct Instruction));
    }
    for (int i = INSTRUCTION_INDEX; i < numberOfRows; i++) {

        int currentState;
        char readValue;
        char writeValue;
        char moveDirection;
        int newState;

        sscanf(lines[i][0].line, "(%d,%c)->(%c,%c,%d)", &currentState, &readValue, &writeValue, &moveDirection,
               &newState);

        instructions[currentState][readValue].writeVal = writeValue;
        instructions[currentState][readValue].moveDir = moveDirection;
        instructions[currentState][readValue].newState = newState;

    }
}

void processInitialTape() {
    char initialTape[50];
    char firstHeadVal[50];

    // removes \n from the string
    strcpy(firstHeadVal, "A");
    strcpy(initialTape, strcat(firstHeadVal,lines[0][0].line));
    initialTape[strlen(initialTape) - 1] = 0;

    for (int i = 0; i < strlen(initialTape); i++) {
        int tapeContent = initialTape[i];
        readWriteHead(tapeContent);
    }

}

void writingFinalTape(int endState, int currentState, int counter) {

        char writeVal = instructions[currentState][currentCell->data].writeVal;
        char moveDir = instructions[currentState][currentCell->data].moveDir;
        int newState = instructions[currentState][currentCell->data].newState;

        currentState = newState;

        printf("Write %c, Move %c, New State %d\n", writeVal, moveDir, newState);
        printf("%s", "------------");
        printf("\n");
        printf("State %d\n", currentState);
        printTapeContents();
        printf("\n");

        for (int i = 0; i < counter; i++) {
            printf(" ");
        }
        printf("^\n");

        currentCell->data = writeVal;
        if (moveDir == 'R') {
            if (currentCell->next == NULL) {
                readWriteHead('B');
            }
            currentCell = currentCell->next;
            counter++;
        } else {
            currentCell = currentCell->prev;
            counter--;
        }

        if(currentState != endState){
            writingFinalTape(endState, currentState, counter);
        }else{
            printf("%s", "Halt.\n");
        }
}


struct cell *addNewCell(int tapeContent) {
    struct cell *newCell = (struct cell *) malloc(sizeof(struct cell));
    newCell->data = tapeContent;
    newCell->prev = NULL;
    newCell->next = NULL;
    return newCell;
}

void  readWriteHead(int tapeContent) {
    struct cell *newCell = addNewCell(tapeContent);
    if (head == NULL) {
        head = newCell;
        tail = newCell;
        return;
    }
    newCell->prev = tail;
    tail->next = newCell;
    tail = newCell;

}

void printTapeContents() {
    struct cell *tape = head;
    while (tape != NULL) {
        printf("%c", tape->data);
        tape = tape->next;
    }
}