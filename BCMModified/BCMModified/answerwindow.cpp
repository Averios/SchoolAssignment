#include "answerwindow.h"
#include "ui_answerwindow.h"

AnswerWindow::AnswerWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::AnswerWindow)
{
    ui->setupUi(this);
    theList = ui->listWidget;
    theStack = ui->stackedWidget;
}

void AnswerWindow::setBaseTable(double **base, bool maximized){
    this->value = base;
    this->maximized = maximized;
}

void AnswerWindow::addAnswer(double result, int **sign){
    theAnswer.push_back(make_pair(result, sign));
}

void AnswerWindow::setTableSize(int row, int column){
    this->row = row;
    this->column = column;
}

void AnswerWindow::renderTable(){
    if(maximized){
        sort(theAnswer.begin(), theAnswer.end(), BackSorter);
    }
    else{
        sort(theAnswer.begin(), theAnswer.end(), Sorter);
    }

    QString Converter;
    int **theSign;
    int totalTable = theAnswer.size();
    for(int k = 0; k < totalTable; k++){
        tables = new QTableWidget;
        tables->setRowCount(row);
        tables->setColumnCount(column);
        listItem = new QListWidgetItem;
        Converter = QString::number(theAnswer[k].first);
        listItem->setText(Converter);
        theList->addItem(listItem);
        theSign = theAnswer[k].second;
        for(int i = 0; i < row; i++){
            for(int j = 0; j < column; j++){
                items = new QTableWidgetItem;
                Converter = QString::number(value[i][j]);
                items->setText(Converter);
                int now = theSign[i][j];
                switch (now) {
                case 1:
                    items->setBackgroundColor(Qt::red);
                    break;
                default:
                    items->setBackgroundColor(Qt::white);
                    break;
                }
                tables->setItem(i, j, items);
            }
        }
        tables->show();
        tables->setEditTriggers(QAbstractItemView::NoEditTriggers);
        theStack->addWidget(tables);
    }
}

AnswerWindow::~AnswerWindow()
{
    delete ui;
}

void AnswerWindow::on_listWidget_currentRowChanged(int currentRow)
{
    theStack->setCurrentIndex(currentRow + 2);
}
