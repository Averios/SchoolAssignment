#include "justresult.h"
#include "ui_justresult.h"

JustResult::JustResult(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::JustResult)
{
    ui->setupUi(this);
    stepsCount = 0;
    theStack = ui->stackedWidget;
    theList = ui->listWidget;
}

JustResult::~JustResult()
{
    delete ui;
}

void JustResult::setTableSize(int row, int column){
    this->row = row;
    this->column = column;
}

void JustResult::addBaseTable(double **table){
    this->values = table;
}

void JustResult::addResult(int **signs){
    theTable = new QTableWidget;
    theTable->setRowCount(row);
    theTable->setColumnCount(column);
    QString Converter;
    Converter = QString::number(++stepsCount);
    QListWidgetItem *itemses = new QListWidgetItem();
    itemses->setText("Step " + Converter);
    theList->addItem(itemses);
    for(int i = 0; i < row; i++){
        for(int j = 0; j < column; j++){
            theItem = new QTableWidgetItem;
            Converter = QString::number(values[i][j]);
            theItem->setText(Converter);
            switch (signs[i][j]) {
            case 1:
                theItem->setBackgroundColor(Qt::red);
                break;
            case 2:
                theItem->setBackgroundColor(Qt::green);
                break;
            case 3:
                theItem->setBackgroundColor(Qt::yellow);
                break;
            default:
                theItem->setBackgroundColor(Qt::white);
                break;
            }
            theTable->setItem(i, j, theItem);
        }
    }
    theTable->show();
    theTable->setEditTriggers(QAbstractItemView::NoEditTriggers);
    theStack->addWidget(theTable);
}

void JustResult::on_listWidget_currentRowChanged(int currentRow)
{
    theStack->setCurrentIndex(currentRow + 2);
}
