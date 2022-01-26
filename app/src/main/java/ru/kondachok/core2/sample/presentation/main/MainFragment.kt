package ru.kondachok.core2.sample.presentation.main

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
import ru.kondachok.core2.core.Resource
import ru.kondachok.core2.core.isLoad
import ru.kondachok.core2.core.on
import ru.kondachok.core2.core.onData
import ru.kondachok.core2.databinding.FragmentMainBinding
import ru.kondachok.core2.sample.presentation.custom.CustomFragment
import ru.kondachok.core2.sample.presentation.list.ListFragment

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel by viewModels<MainViewModel>()
    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViewModel()

        binding.answer.setOnClickListener {
            viewModel.onAnswerClicked()
        }

        binding.btAnswersNoLoad.setOnClickListener {
            viewModel.onNoLoadClicked()
            navigateToList()
        }
        binding.btAnswersLoad.setOnClickListener {
            viewModel.onReloadClicked()
        }
        binding.btCustom.setOnClickListener {
            navigateToCustom()
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.answerFlow.collect {
                updateViewFromRes(it)
                it.on(
                    data = ::updateAnswerText,
                    error = { updateAnswerText("Произошла ошибка, ответа нет!") },
                    cancel = { updateAnswerText("Вопрос отменен!") }
                )
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.answerReloadFlow.collect {
                updateViewFromRes(it)
                it.onData(Fun { navigateToList() })
            }
        }
    }

    private fun <T> updateViewFromRes(resource: Resource<T>) {
        binding.progress.isVisible = resource.isLoad
        binding.answer.isVisible = !resource.isLoad
        binding.btAnswersLoad.isEnabled = !resource.isLoad
        binding.btAnswersNoLoad.isEnabled = !resource.isLoad
        binding.btCustom.isEnabled = !resource.isLoad
    }

    private fun updateAnswerText(it: String?) {
        binding.answer.text = it
    }

    private fun navigateToList() {
        parentFragmentManager
            .beginTransaction()
            .addToBackStack("list")
            .replace(R.id.container, ListFragment())
            .commit()
    }

    private fun navigateToCustom() {
        parentFragmentManager
            .beginTransaction()
            .addToBackStack("custom")
            .replace(R.id.container, CustomFragment())
            .commit()
    }
}
