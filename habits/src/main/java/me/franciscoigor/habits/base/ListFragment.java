package me.franciscoigor.habits.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.franciscoigor.habits.R;

/**
 * ListFragment
 *
 * Base List container for manage item lists in RecyclerView
 */
public abstract class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;

    public ListFragment() {
        // Required empty public constructor
    }


    protected abstract void createFragment(Bundle savedInstanceState);

    protected abstract void setupAdapter(ItemAdapter adapter);

    public ArrayList<DataModel> getItems(String name){
        return DatabaseHelper.getAll(name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFragment(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = (RecyclerView) view
                .findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateView();

        return view;
    }

    public void updateView(){

        if (adapter == null) {
            adapter = getAdapter();
            setupAdapter(adapter);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public ItemAdapter getAdapter() {
        if (adapter == null){
            return newAdapter();
        }
        return adapter;
    }

    public abstract ItemAdapter newAdapter();

    public class ListItemHolder extends ItemHolder {

        private TextView mTitle, mDetails;

        public ListItemHolder(View view){
            super(view);
            mTitle = view.findViewById(R.id.list_item_title);
            mDetails = view.findViewById(R.id.list_item_details);
        }

        @Override
        public void bind(DataModel item) {
            mTitle.setText(item.getUUID());
            mDetails.setText(item.toString());
        }

    }


    public class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        private ArrayList<DataModel> list;

        public ItemAdapter(String name) {
            list = loadItems(name);
        }

        public void addItem(DataModel item){
            list.add(item);
            DatabaseHelper.insert(item);
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(getItemHolderLayout(), parent, false);
            return createItemHolder(view);
        }
        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            DataModel item = list.get(position);
            holder.bind(item);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }

        public boolean findItem(DataModel item) {
            return list.contains(item);
        }

        public ArrayList<DataModel> loadItems(String name) {
            return getItems(name);
        }


        public void updateItem(DataModel item) {
            DatabaseHelper.update(item);
            int pos=list.indexOf(item);
            if (pos==-1){
                this.notifyDataSetChanged();
            }else{
                this.notifyItemChanged(pos);
            }

        }

        public void deleteItem(DataModel item){
            int pos=list.indexOf(item);
            if (pos>-1){
                list.remove(pos);
                this.notifyItemRemoved(pos);
                DatabaseHelper.delete(item);
            }
        }

        public ArrayList<DataModel> findItems(String table, String[] columns, String where, String[] whereArgs){
            return DatabaseHelper.getItems(table, columns, where, whereArgs);
        }
    }

    protected int getItemHolderLayout(){
        return R.layout.list_item;
    }

    protected ItemHolder createItemHolder(View view){
        return new ListItemHolder(view);
    }


}
