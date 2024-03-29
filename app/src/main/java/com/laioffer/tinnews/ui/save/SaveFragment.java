package com.laioffer.tinnews.ui.save;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.FragmentSaveBinding;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.repository.NewsRepository;
import com.laioffer.tinnews.repository.NewsViewModelFactory;


public class SaveFragment extends Fragment {
    private FragmentSaveBinding binding;
    private SaveViewModel viewModel;


    public SaveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        binding = FragmentSaveBinding.bind(view);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SavedNewsAdapter savedNewsAdapter = new SavedNewsAdapter();

        binding.newsSavedRecyclerView.setAdapter(savedNewsAdapter);
        binding.newsSavedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        NewsRepository repository = new NewsRepository(requireContext());
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository)).get(SaveViewModel.class);

        viewModel.getAllSavedArticle().observe(getViewLifecycleOwner(), savedArticle -> {
            if (savedArticle != null) {
                savedNewsAdapter.setArticles(savedArticle);
            }
        });

        savedNewsAdapter.setItemCallback(new SavedNewsAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Article article) {
                SaveFragmentDirections.ActionNavigationSaveToNavigationDetails directions =
                        SaveFragmentDirections.actionNavigationSaveToNavigationDetails(article);
                NavHostFragment.findNavController(SaveFragment.this).navigate(directions);
            }

            @Override
            public void onRemoveFavorite(Article article) {
                viewModel.deleteSavedArticle(article);
            }
        });
    }
}