package ru.kondachok.core2.sample.presentation.custom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import ru.kondachok.core2.R
import ru.kondachok.core2.core.isA
import ru.kondachok.core2.databinding.FragmentCustomBinding

@AndroidEntryPoint
class CustomFragment : Fragment(R.layout.fragment_custom) {

    private val viewModel by viewModels<CustomVIewModel>()
    private val binding by viewBinding(FragmentCustomBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                //Можно работать с кастомным ViewState
                if(it is StateInit) {
                    binding.answer.text = "Нажми меня"
                }
                // При этом использовать расширениы для State
                if(it.isA<Nothing, StateClicked>()) {
                    binding.answer.text = "А уже все!"
                }
            }
        }

        binding.answer.setOnClickListener {
            viewModel.onClick()
        }
    }
}
