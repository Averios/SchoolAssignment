#ifndef JUSTRESULT_H
#define JUSTRESULT_H

#include <QMainWindow>
#include <QTableWidget>
#include <QStackedWidget>
#include <QListWidget>

namespace Ui {
class JustResult;
}

class JustResult : public QMainWindow
{
    Q_OBJECT

public:
    explicit JustResult(QWidget *parent = 0);
    ~JustResult();
    void setTableSize(int row, int column);
    void addBaseTable(double **table);
    void addResult(int **signs);

private slots:
    void on_listWidget_currentRowChanged(int currentRow);

private:
    Ui::JustResult *ui;
    double **values;
    int row, column;
    int stepsCount;
    QTableWidget *theTable;
    QTableWidgetItem *theItem;
    QStackedWidget *theStack;
    QListWidget *theList;
};

#endif // JUSTRESULT_H
