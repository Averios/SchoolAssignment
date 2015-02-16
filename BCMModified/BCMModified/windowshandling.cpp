#include "windowshandling.h"

WindowsHandling::WindowsHandling(QObject *parent):QObject(parent)
{
}
void WindowsHandling::About(){
    OpenAbout = new AboutDialog();
    OpenAbout->show();
    OpenAbout->raise();
    OpenAbout->activateWindow();
}

void WindowsHandling::Credits(){
    if(OpenCredits!=NULL){
        OpenCredits = new CreditsDialog();
    }
    OpenCredits->show();
    OpenCredits->raise();
    OpenCredits->activateWindow();
}
