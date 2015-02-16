#ifndef TABLECONTAINER_H
#define TABLECONTAINER_H
#include<algorithm>

typedef struct TableContainer{ int x;
                               int y;
                               double value;
                               int active;
                             }TableContainer;
typedef std::pair< double, TableContainer*> MyPair;


static bool sorter(const MyPair &i, const MyPair &j){
    return i.first < j.first;
}

static bool backsort(const MyPair &i, const MyPair &j){
    return i.first > j.first;
}

#endif // TABLECONTAINER_H
