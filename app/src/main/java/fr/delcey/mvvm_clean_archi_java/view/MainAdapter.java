package fr.delcey.mvvm_clean_archi_java.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import fr.delcey.mvvm_clean_archi_java.R;
import fr.delcey.mvvm_clean_archi_java.view.model.PropertyUiModel;

// ListAdapter is a "super RecyclerView.Adapter", very smart.
class MainAdapter extends ListAdapter<PropertyUiModel, MainAdapter.MainViewHolder> {

    MainAdapter() {
        super(new DiffCallback());
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewType;
        private final TextView textViewAddress;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewType = itemView.findViewById(R.id.item_main_tv_type);
            textViewAddress = itemView.findViewById(R.id.item_main_tv_address);
        }

        void bind(PropertyUiModel model) {
            textViewType.setText(model.getType());
            textViewAddress.setText(model.getMainAddress());
        }
    }

    private static class DiffCallback extends DiffUtil.ItemCallback<PropertyUiModel> {

        @Override
        public boolean areItemsTheSame(@NonNull PropertyUiModel oldItem, @NonNull PropertyUiModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull PropertyUiModel oldItem, @NonNull PropertyUiModel newItem) {
            return oldItem.equals(newItem);
        }
    }
}
