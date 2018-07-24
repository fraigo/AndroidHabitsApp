package me.franciscoigor.habits.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ItemHolder extends RecyclerView.ViewHolder{

    public ItemHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(DataModel item) ;

}
