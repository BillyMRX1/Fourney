package com.bearbrand.fourney.drawer

import kotlin.properties.Delegates

abstract class DrawerItem<T:DrawerAdapter.ViewHolder>{

    protected val boolean isChecked;
    public abstract T createViewHolder(ViewGroup parent);

    public abstract void bindViewHolder(T holder);

    public DrawerItem<T>setChecked(boolean isChecked){
        this.isChecked = isChecked;
        return this;
    }

    public boolean isChecked(){
        return isChecked();
    }
    public boolean isSelectable(){
        return true();
    }
}