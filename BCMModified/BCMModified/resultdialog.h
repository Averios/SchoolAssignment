#ifndef RESULTDIALOG_H
#define RESULTDIALOG_H

#include <QDialog>
#include <QTableWidget>
#include <QStackedWidget>
#include <QListWidget>
#include"WidgetList.h"
#include"TableContainer.h"
#include<vector>
using namespace std;
namespace Ui {
class ResultDialog;
}

class ResultDialog : public QDialog
{
    Q_OBJECT

public:
    explicit ResultDialog(QWidget *parent = 0);
    ~ResultDialog();

    void setTableSize(int row, int column);
    void addBaseTable(double **table);
    void addResult(int **signs);
private slots:
    void on_listWidget_currentRowChanged(int currentRow);

private:
    Ui::ResultDialog *ui;
    double **values;
    int row, column;
    int stepsCount;
    QTableWidget *theTable;
    QTableWidgetItem *theItem;
    QStackedWidget *theStack;
    QListWidget *theList;
};

#endif // RESULTDIALOG_H
