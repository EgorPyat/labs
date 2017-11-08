#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

#define STR_LEN 80

typedef struct list{
	char* string;
	struct list* next;
} List;

typedef struct headList{
	pthread_mutex_t synchronzer;
	List* list;
} headList;

headList* create_list(headList* head){
  head = (headList*)malloc(sizeof(headList));
	pthread_mutex_init(&head->synchronzer, NULL);
  head->list = NULL;
	return head;
}

void new_node(headList* head, char* str){
	if(head == NULL){
		printf("%s\n", "Head isnt exist!");
		return;
	}
	else if(head->list == NULL){
		pthread_mutex_lock(&head->synchronzer);
		head->list = (List*)malloc(sizeof(List));
		head->list->string = (char*)malloc(strlen(str) + 1);
		strcpy(head->list->string, str);
		pthread_mutex_unlock(&head->synchronzer);
	}
  else{
		pthread_mutex_lock(&head->synchronzer);
		List* tmp = (List*)malloc(sizeof(List));
    tmp->string = (char*)malloc(strlen(str) + 1);
    strcpy(tmp->string, str);
    tmp->next = head->list;
		head->list = tmp;
		pthread_mutex_unlock(&head->synchronzer);
  }
}

void print_list(headList *head){
	pthread_mutex_lock(&head->synchronzer);
  printf("\nList:\n");
	List* t = NULL;
	List* list = head->list;
	while(list != NULL){
		printf("	%s\n", list->string);
		t = list->next;
		list = t;
	}
	pthread_mutex_unlock(&head->synchronzer);
}

void swap(List* a, List* b){
	char* c = a->string;
	a->string = b->string;
	b->string = c;
}

void* sort_list(void* head){
	headList* headr = (headList*)head;
	for(;;){
		sleep(5);
		if(((headList*)head)->list == NULL){ continue; }
		pthread_mutex_lock(&headr->synchronzer);
		for(List* i = headr->list; i != NULL; i = i->next){
			for(List* j = i->next; j != NULL; j = j->next){
				if(strcmp(i->string, j->string) > 0){
					swap(i, j);
				}
			}
		}
		pthread_mutex_unlock(&headr->synchronzer);
	}
}

void freeList(headList* head){
	pthread_mutex_destroy(&head->synchronzer);
	List* list = head->list;
	while(list != NULL){
		List* tmp = list->next;
		free(list);
		list = tmp;
	}
	free(head);
}

int main(){
  char tmp[STR_LEN];
	headList *head = create_list(head);
	pthread_t sorter;
	pthread_create(&sorter, NULL, sort_list, (void*)head);

	for(;;){
		fgets(tmp, STR_LEN, stdin);
    tmp[strlen(tmp) - 1] = '\0';
		if(strcmp("", tmp) == 0){
      print_list(head);
		}
		else if(tmp[0] == '.'){
			break;
		}
		else{
      new_node(head, tmp);
    }
  }

	pthread_cancel(sorter);
	pthread_join(sorter, NULL);

	freeList(head);

	return 0;
}
