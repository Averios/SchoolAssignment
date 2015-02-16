#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include "problemwindow.h"
#include "windowshandling.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = 0);
    ~MainWindow();

private slots:
    void on_Next_clicked();

private:
    Ui::MainWindow *ui;
    ProblemWindow *problem;
    WindowsHandling *handler;
};

#endif // MAINWINDOW_H
