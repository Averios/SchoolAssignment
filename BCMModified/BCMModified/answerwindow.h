#ifndef ANSWERWINDOW_H
#define ANSWERWINDOW_H

#include <QMainWindow>
#include <algorithm>
#include <vector>
#include <QTableWidget>
#include <QListWidget>
#include <QStackedWidget>
#include "AnswerHandler.h"
using namespace std;
namespace Ui {
class AnswerWindow;
}

class AnswerWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit AnswerWindow(QWidget *parent = 0);
    ~AnswerWindow();
    void setTableSize(int row, int column);
    void setBaseTable(double **base, bool maximized);
    void addAnswer(double result, int **sign);
    void renderTable();
private slots:
    void on_listWidget_currentRowChanged(int currentRow);

private:
    Ui::AnswerWindow *ui;
    vector < AnsPair > theAnswer;
    QListWidget *theList;
    QStackedWidget *theStack;
    double **value;
    int row, column;
    bool maximized;
    QTableWidget *tables;
    QTableWidgetItem *items;
    QListWidgetItem *listItem;
};

#endif // ANSWERWINDOW_H
