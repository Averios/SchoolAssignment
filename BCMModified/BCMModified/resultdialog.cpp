#include "resultdialog.h"
#include "ui_resultdialog.h"

ResultDialog::ResultDialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::ResultDialog)
{

    ui->setupUi(this);
    stepsCount = 0;
    theStack = ui->stackedWidget;
    theList = ui->listWidget;
}

ResultDialog::~ResultDialog()
{
    delete ui;
}

void ResultDialog::setTableSize(int row, int column){
    this->row = row;
    this->column = column;
}

void ResultDialog::addBaseTable(double **table){
    this->values = table;
}

void ResultDialog::addResult(int **signs){
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
            default:
                theItem->setBackgroundColor(Qt::white);
                break;
            }
            theTable->item(i, j)->setText(Converter);
        }
    }
    theTable->show();
    theStack->addWidget(theTable);
}

void ResultDialog::on_listWidget_currentRowChanged(int currentRow)
{
    theStack->setCurrentIndex(currentRow + 2);
}
