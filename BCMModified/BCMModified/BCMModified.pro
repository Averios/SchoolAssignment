#-------------------------------------------------
#
# Project created by QtCreator 2014-04-21T06:41:07
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = BCMModified
TEMPLATE = app


SOURCES += main.cpp\
        mainwindow.cpp \
    problemwindow.cpp \
    windowshandling.cpp \
    aboutdialog.cpp \
    resultdialog.cpp \
    creditsdialog.cpp \
    WidgetList.cpp \
    Leftover.cpp \
    justresult.cpp \
    answerwindow.cpp

HEADERS  += mainwindow.h \
    problemwindow.h \
    windowshandling.h \
    aboutdialog.h \
    resultdialog.h \
    creditsdialog.h \
    WidgetList.h \
    TableContainer.h \
    justresult.h \
    answerwindow.h \
    AnswerHandler.h

FORMS    += mainwindow.ui \
    problemwindow.ui \
    aboutdialog.ui \
    resultdialog.ui \
    creditsdialog.ui \
    justresult.ui \
    answerwindow.ui

OTHER_FILES +=
