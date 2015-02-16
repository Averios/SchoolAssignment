#include "mainwindow.h"
#include "ui_mainwindow.h"

MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    handler = new WindowsHandling();
    connect(ui->action_About,SIGNAL(triggered()),handler,SLOT(About()));
    connect(ui->action_Credits,SIGNAL(triggered()),handler,SLOT(Credits()));
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_Next_clicked()
{
    int *Pegawai = new int;
    int *Tugas = new int;
    QString *xPegawai = new QString;
    QString *xTugas = new QString;
    *xPegawai = ui->Pegawai->text();
    *xTugas = ui->Tugas->text();
    *Pegawai = xPegawai->toInt();
    *Tugas = xTugas->toInt();
    problem = new ProblemWindow();
    problem->setWindowTitle("Problem Matrix");
    problem->show();
    problem->setTable(*Pegawai,*Tugas);
    problem->raise();
    problem->activateWindow();
    delete Pegawai;
    delete Tugas;
    delete xPegawai;
    delete xTugas;
//    this->close();
//    delete this;
}
