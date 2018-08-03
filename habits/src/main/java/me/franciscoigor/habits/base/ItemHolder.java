package me.franciscoigor.habits.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * ItemHolder
 *
 * Base item holder controller for RecyclerView items
 */
public abstract class ItemHolder extends RecyclerView.ViewHolder{

    public ItemHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(DataModel item) ;

}
