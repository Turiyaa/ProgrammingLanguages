#include <stdio.h>
#include "string.h"
#include <stdlib.h>

typedef struct Item {
    char name[50];
    double price;
};

int main() {
    char option[2];
    char name[50];
    int shelf;
    int slot;
    double price;

    printf("Enter <Shelves> <Slots>: ");
    scanf("%d %d", &shelf, &slot);

    struct Item **items;
    items = (struct Item **) malloc(shelf * sizeof(struct Item *));

    for (int i = 0; i < shelf; i++) {
        items[i] = (struct Item *) malloc(slot * sizeof(struct Item));
    }

    while (1) {
        printf("Enter <name>, <price>, <shelf>, <slot>: ");
        scanf("%50s %lf %d %d", name, &price, &shelf, &slot);

        strcpy(items[shelf][slot].name, name);
        items[shelf][slot].price = price;

        printf("Add more items (Y/N): ");
        scanf("%s", option);

        if (option[0] == 'N' || option[0] == 'n') {
            break;
        }
    }

    while (1) {
        printf("Enter <shelf> <slot> to search item: ");
        scanf("%d %d", &shelf, &slot);

        if (strcmp(items[shelf][slot].name, "") == 0) {
            printf("[%d] [%d] slot is empty\n", shelf, slot);
        } else {
            printf("Name: %s Price: %lf\n", items[shelf][slot].name, items[shelf][slot].price);
        }

        printf("Enter Y to Continue, N to exit (Y/N): ");
        scanf("%s", option);

        if (option[0] == 'N' || option[0] == 'n') {
            break;
        }
    }

    free(items);
    items = NULL;

    return 0;
}