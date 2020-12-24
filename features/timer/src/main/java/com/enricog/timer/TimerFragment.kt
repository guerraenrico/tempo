package com.enricog.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.enricog.base_android.extensions.applyArguments
import com.enricog.base_android.extensions.viewLifecycleScope
import com.enricog.timer.models.TimerConfiguration
import com.enricog.timer.models.TimerViewState
import com.enricog.ui_components.resources.TempoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class TimerFragment : Fragment() {

    private val viewModel: TimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TempoTheme {
                    Timer(viewModel)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (savedInstanceState == null) {
            val routineId = requireArguments().getLong(ARGUMENT_ROUTINE_ID)
            viewModel.load(TimerConfiguration(routineId))
        }

        viewModel.viewState.onEach { viewState ->
            val enable = viewState is TimerViewState.Counting && viewState.enableKeepScreenOn
            toggleKeepScreenOnFlag(enable)
        }.launchIn(viewLifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toggleKeepScreenOnFlag(false)
    }

    private fun toggleKeepScreenOnFlag(enable: Boolean) {
        if (enable) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    companion object {
        private const val ARGUMENT_ROUTINE_ID = "routine_id"

        fun newInstance(routineId: Long): TimerFragment {
            return TimerFragment().applyArguments(
                ARGUMENT_ROUTINE_ID to routineId
            )
        }
    }
}