#ifndef ANSWERHANDLER_H
#define ANSWERHANDLER_H

#include <algorithm>
#include <vector>
using namespace std;
typedef pair < double, int** > AnsPair;

static bool Sorter(const AnsPair &i, const AnsPair &j){
    return i.first > j.first;
}

static bool BackSorter(const AnsPair &i, const AnsPair &j){
    return i.first < j.first;
}

#endif // ANSWERHANDLER_H
