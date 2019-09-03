package fr.delcey.mvvm_clean_archi_java.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.delcey.mvvm_clean_archi_java.R;
import fr.delcey.mvvm_clean_archi_java.view.model.PropertyUiModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final MainAdapter adapter = new MainAdapter();
        RecyclerView recyclerView = findViewById(R.id.main_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(MainViewModel.class);

        // We "wire" the LiveData : we observe it and any time the database changes, it will change
        // the LiveData, and the observer will be triggered, calling "onChanged". This is at this
        // moment that we tell the adapter to change its data with the special method "submitList"
        mViewModel.getUiModelsLiveData().observe(this, new Observer<List<PropertyUiModel>>() {
            @Override
            public void onChanged(List<PropertyUiModel> propertyUiModels) {
                adapter.submitList(propertyUiModels);
            }
        });


        Button insertRandomPropertyButton = findViewById(R.id.main_btn_insert);

        // The view (in this case, the Activity) *NEVER* has intelligence about business, it's only
        // a dumb "visualization" of what the ViewModel tell it. So, we just listen to click to the
        // button and tell the ViewModel to do its stuff. Nothing more.
        insertRandomPropertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.addNewProperty();
            }
        });
    }
}
