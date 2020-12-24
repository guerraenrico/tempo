package com.enricog.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.enricog.timer.models.TimerConfiguration
import com.enricog.ui_components.resources.TempoTheme
import dagger.hilt.android.AndroidEntryPoint

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

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (savedInstanceState == null) {
            val routineId = requireArguments().getLong(ARGUMENT_ROUTINE_ID)
            viewModel.load(TimerConfiguration(routineId))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    companion object {
        private const val ARGUMENT_ROUTINE_ID = "routine_id"

        fun newInstance(routineId: Long): TimerFragment {
            val args = Bundle()
            args.putLong(ARGUMENT_ROUTINE_ID, routineId)
            val fragment = TimerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}