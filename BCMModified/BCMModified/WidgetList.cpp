#include"WidgetList.h"

WidgetList::WidgetList(QWidget *parent)
    : /**/QWidget(parent)
      /**/,layout_(new QVBoxLayout(this))
{
    this->setLayout(layout_);
}

WidgetList::~WidgetList(){
    delete layout_;
}

void WidgetList::removeWidget(QWidget* widget)
{
    layout_->removeWidget(widget);
}

void WidgetList::addWidget(QWidget* widget)
{
    layout_->addWidget(widget);
}

QWidget* WidgetList::takeAt(int index) const
{
    return layout_->takeAt(index)->widget();
}

int WidgetList::count() const
{
    return layout_->count();
}
