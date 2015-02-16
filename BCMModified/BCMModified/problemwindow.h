#ifndef PROBLEMWINDOW_H
#define PROBLEMWINDOW_H

#include <QMainWindow>
#include "windowshandling.h"
#include "justresult.h"
#include "TableContainer.h"
#include "answerwindow.h"
#include <vector>

using namespace std;
namespace Ui {
class ProblemWindow;
}

class ProblemWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit ProblemWindow(QWidget *parent = 0);
    ~ProblemWindow();
    void setTable(int x,int y);

private slots:
    void on_pushButton_clicked();

private:
    Ui::ProblemWindow *ui;
    WindowsHandling *handler;
    JustResult *result;
    AnswerWindow *answer;
    bool solved;
    bool maximizing;
    vector < MyPair > *PointingX;
    vector < MyPair > *PointingY;
    vector < MyPair > *RealY;
    vector < MyPair > *BestCase;
    vector < int > ListX, ListY;
    vector < int >::iterator FoundX, FoundY;
    int row, column, point1;
    double **valueContainer;
    int **sign;
    void SolveProblem();
    void ComboCreator(int now);
};

#endif // PROBLEMWINDOW_H
