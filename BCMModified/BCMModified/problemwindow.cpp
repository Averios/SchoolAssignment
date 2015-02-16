#include "problemwindow.h"
#include "ui_problemwindow.h"
#include <vector>
#include<algorithm>
using namespace std;

ProblemWindow::ProblemWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::ProblemWindow)
{
    ui->setupUi(this);
    handler = new WindowsHandling();
    connect(ui->action_About,SIGNAL(triggered()),handler,SLOT(About()));
    connect(ui->action_Credits,SIGNAL(triggered()),handler,SLOT(Credits()));
    solved = false;
}

ProblemWindow::~ProblemWindow()
{
    delete ui;
}


void ProblemWindow::on_pushButton_clicked()
{
    if(!solved){
        //Solve the problem
    }
    solved = true;
    bool ok;
    result = new JustResult();
    answer = new AnswerWindow();
    QTableWidget *tabel = ui->tableWidget;
    QString converter;
    /*
        Parse the result
    */    
    PointingX = new vector< MyPair >[column];
    RealY = new vector < MyPair >[row];
    PointingY = new vector< MyPair >[row];

    TableContainer *problem;
    valueContainer = new double* [row];
    for(int i = 0; i < row; i++){
        valueContainer[i] = new double[column];
        for(int j = 0; j < column; j++){
            problem = new TableContainer;
            converter = tabel->item(i,j)->text();
            problem->active = 0;
            problem->value = converter.toDouble(&ok);
            problem->x = j;
            problem->y = i;
            (PointingY[i]).push_back(make_pair(problem->value,problem));
            (RealY[i]).push_back(make_pair(problem->value, problem));
            (PointingX[j]).push_back(make_pair(problem->value,problem));            
            valueContainer[i][j] = problem->value;
        }
    }
    result->setTableSize(row, column);
    result->addBaseTable(valueContainer);
    answer->setTableSize(row,column);
    SolveProblem();

    answer->renderTable();
    result->setWindowTitle("Steps");
    answer->setWindowTitle("Answer List");
    result->show();
    result->activateWindow();
    result->raise();
    answer->show();
    //Free the memory allocation
//    for(int i = 0; i < row; i++){
//        for(int j = 0; j < column; j++){
//            delete RealY[i][j].second;
//            delete PointingY[i][j].second;
//            delete RealX[j][i].second;
//            delete PointingX[j][i].second;
//        }
//    }
//    delete RealX;
//    delete RealY;
//    delete PointingX;
//    delete PointingY;
}

void ProblemWindow::SolveProblem(){
    bool up = ui->option1->isChecked();
    up = !up;
    answer->setBaseTable(valueContainer,up);
    double optimum;
    int count;
    for(int i = 0; i < row; i++){
        if(up){
            sort(PointingY[i].begin(), PointingY[i].end(), sorter);
        }
        else{
            sort(PointingY[i].begin(), PointingY[i].end(), backsort);
        }
        optimum = PointingY[i][0].first;
        count = 0;
        for(int j = 0; j < column; j++){
            if(PointingY[i][j].first == optimum){
                PointingY[i][j].second->active = 1;
            }
            else if(count < 1){
                count++;
                optimum = PointingY[i][j].first;
                PointingY[i][j].second->active = 1;
            }
            else break;
        }
    }

    sign = new int* [row];
    for(int i = 0; i < row; i++){
        sign[i] = new int[column];
        for(int j = 0; j < column; j++){
            sign[i][j] = RealY[i][j].second->active;
        }
    }
    result->addResult(sign);

    for(int i = 0; i < column; i++){
        if(up){
            sort(PointingX[i].begin(), PointingX[i].end(), sorter);
        }
        else{
            sort(PointingX[i].begin(), PointingX[i].end(), backsort);
        }
        optimum = PointingX[i][0].first;
        count = 0;
        for(int j = 0; j < row; j++){
            if(PointingX[i][j].first == optimum){
                PointingX[i][j].second->active += 2;
            }
            else if(count < 1){
                count++;
                optimum = PointingX[i][j].first;
                PointingX[i][j].second->active += 2;
            }
            else break;
        }
    }

    sign = new int* [row];
    for(int i = 0; i < row; i++){
        sign[i] = new int[column];
        for(int j = 0; j < column; j++){
            sign[i][j] = RealY[i][j].second->active;
        }
    }
    result->addResult(sign);
    bool straight;
    if(row > column){
        straight = true;
        point1 = row;
    }
    else {
        straight = false;
        point1 = column;
    }
    BestCase = new vector< MyPair >[point1];
    for(int i = 0; i < row; i++){
        for(int j = 0; j < column; j++){
            if(RealY[i][j].second->active > 0){
                if(straight){
                    BestCase[i].push_back(RealY[i][j]);
                }
                else{
                    BestCase[j].push_back(RealY[i][j]);
                }
            }
        }
    }
    int lengthen = BestCase[0].size();
    for(int i = 0; i < lengthen; i++){
        ListX.push_back(BestCase[0][i].second->x);
        ListY.push_back(BestCase[0][i].second->y);
        ComboCreator(1);
        ListX.pop_back();
        ListY.pop_back();
    }
}

void ProblemWindow::ComboCreator(int now){
    if(now < point1){
        int total = BestCase[now].size();
        for(int i = 0; i < total; i++){
            FoundX = std::find(ListX.begin(), ListX.end(), BestCase[now][i].second->x);
            FoundY = std::find(ListY.begin(), ListY.end(), BestCase[now][i].second->y);
            if(FoundX == ListX.end() && FoundY == ListY.end()){
                ListX.push_back(BestCase[now][i].second->x);
                ListY.push_back(BestCase[now][i].second->y);
                ComboCreator(now + 1);
                ListX.pop_back();
                ListY.pop_back();
            }
        }
    }
    else{
        double result = 0;

        sign = new int* [row];
        for(int i = 0; i < row; i++){
            sign[i] = new int[column];
            for(int j = 0; j < column; j++){
                sign[i][j] = 0;
            }
        }

        for(int i = 0; i < point1; i++){
            result += RealY[ListY[i]][ListX[i]].first;
            sign[ListY[i]][ListX[i]] = 1;
        }
        answer->addAnswer(result, sign);
    }
}

void ProblemWindow::setTable(int x, int y){
    row = y;
    column = x;
    ui->tableWidget->setRowCount(y);
    ui->tableWidget->setColumnCount(x);
}
