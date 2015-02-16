#ifndef WIDGETLIST_H
#define WIDGETLIST_H

#include<QWidget>
#include<QObject>
#include<QVBoxLayout>

class WidgetList : public QWidget
{
    Q_OBJECT

public:
    WidgetList(QWidget *parent = 0);
    ~WidgetList();

    void addWidget(QWidget*);
    void removeWidget(QWidget*);

    QList<QWidget*> getListWidget() const;
    QWidget* takeAt(int) const;
    int count() const;

private:
    QVBoxLayout* layout_;
};

#endif // WIDGETLIST_H
