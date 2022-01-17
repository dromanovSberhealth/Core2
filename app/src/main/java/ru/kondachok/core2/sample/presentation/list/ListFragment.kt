package ru.kondachok.core2.sample.presentation.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kondachok.core2.R
import ru.kondachok.core2.core.Fun
import ru.kondachok.core2.core.isData
import ru.kondachok.core2.core.isError
import ru.kondachok.core2.core.isLoad
import ru.kondachok.core2.core.onData
import ru.kondachok.core2.databinding.FragmentListBinding

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private val viewModel by viewModels<ListViewModel>()
    private val binding by viewBinding(FragmentListBinding::bind)

    private var adapter = AnswersAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter
        binding.recycler.itemAnimator = null
        binding.refresh.setOnRefreshListener { viewModel.refresh() }
        binding.errorText.setOnClickListener { viewModel.init() }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.answersFlow.collect { state ->
                binding.progress.isVisible = state.isLoad
                binding.errorText.isVisible = state.isError
                binding.recycler.isVisible = state.isData || state.isRefresh
                binding.refresh.isRefreshing = state.isRefresh
                state.onData(Fun { adapter.submitList(it) })
                state.onRefresh(Fun { adapter.submitList(it) })
            }
        }

        if (savedInstanceState == null) viewModel.init()
    }
}
