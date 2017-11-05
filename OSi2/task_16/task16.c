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

void new_node(List* head, char* str){
	if(head == NULL){
		printf("%s\n", "Head isnt exist!");
		return;
	}
  else if((head)->string == NULL){
		pthread_mutex_lock(&synchronzer);
    (head)->string = (char*)malloc(strlen(str) + 1);
    strcpy((head)->string, str);
		pthread_mutex_unlock(&synchronzer);
  }
  else{
		pthread_mutex_lock(&synchronzer);
		List* tmp = head;
		while(tmp->next != NULL){
			tmp = tmp->next;
		}
		tmp->next = (List*)malloc(sizeof(List));
    tmp->next->string = (char*)malloc(strlen(str) + 1);
    strcpy(tmp->next->string, str);
    tmp->next->next = NULL;
		pthread_mutex_unlock(&synchronzer);
  }
}

void print_list(List *head){
	pthread_mutex_lock(&synchronzer);
  printf("\nList:\n");
	List *t = NULL;
	while(head != NULL){
		printf("	%s\n", head->string);
		t = head->next;
		head = t;
	}
	pthread_mutex_unlock(&synchronzer);
}

void swap(List* a, List* b){
	char* c = a->string;
	a->string = b->string;
	b->string = c;
}

void* sort_list(void* head){
	for(;;){
		sleep(5);
		if(((List*)head)->string == NULL){ continue; }
		pthread_mutex_lock(&synchronzer);
		for(List* i = (List*)head; i != NULL; i = i->next){
			for(List* j = i->next; j != NULL; j = j->next){
				if(strcmp(i->string, j->string) > 0){
					swap(i, j);
				}
			}
		}
		pthread_mutex_unlock(&synchronzer);
	}
}

int main(){
  char tmp[64];
	List *head = create_list(head);
	pthread_t sorter;
	pthread_mutex_init(&synchronzer, NULL);
	pthread_create(&sorter, NULL, sort_list, (void*)head);

	for(;;){
		fgets(tmp, 64, stdin);
    tmp[strlen(tmp) - 1] = '\0';
		if(strcmp("", tmp) == 0){
      print_list(head);
		}
		else{
      new_node(head, tmp);
    }
  }

	return 0;
}
