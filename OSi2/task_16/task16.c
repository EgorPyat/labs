#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

pthread_mutex_t synchronzer;

typedef struct list{
	char* string;
	struct list* next;
} List;

List* create_list(List* head){
  head = (List*)malloc(sizeof(List));
  head->string = NULL;
  head->next = NULL;
}

void new_node(List** head, char* str){
	List *t = NULL;
	if(*head == NULL){
		return;
	}
  else if((*head)->string == NULL){
    (*head)->string = (char*)malloc(strlen(str) + 1);
    strcpy((*head)->string, str);
  }
  else{
    t = (List*)malloc(sizeof(List));
    t->string = (char*)malloc(strlen(str) + 1);
    strcpy(t->string, str);
    t->next = *head;
    *head = t;
  }
}

void print_list(List *head){
  printf("\nList:\n");
	List *t = NULL;
	while(head != NULL){
		printf("	%s\n", head->string);
		t = head->next;
		head = t;
	}
}

int main(){
  char tmp[64];
	List *head = create_list(head);

	for(;;){
		fgets(tmp, 64, stdin);
    tmp[strlen(tmp) - 1] = '\0';
		if(strcmp("", tmp) == 0){
      print_list(head);
		}
		else{
      new_node(&head, tmp);
    }
  }

	return 0;
}

// List* reverse(List *head){
//   List *p = NULL;
//   List *t = NULL;
//   while(head != NULL){
//     p = head;
//     head = head->next;
//     p->next = t;
//     t = p;
//   }
//   return t;
// }
