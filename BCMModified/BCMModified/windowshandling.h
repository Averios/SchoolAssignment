#ifndef WINDOWSHANDLING_H
#define WINDOWSHANDLING_H

#include <QMainWindow>
#include <QObject>
#include "aboutdialog.h"
#include "creditsdialog.h"

class WindowsHandling : public QObject
{
    Q_OBJECT
public:
    AboutDialog *OpenAbout;
    CreditsDialog *OpenCredits;
    WindowsHandling(QObject* parent = 0);
public slots:
    void About();
    void Credits();
};

#endif // WINDOWSHANDLING_H
