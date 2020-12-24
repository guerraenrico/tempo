package com.enricog.routines.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.enricog.ui_components.resources.TempoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoutineFragment : Fragment() {

    private val viewModel: RoutinesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TempoTheme {
                    Routines(viewModel)
                }
            }
        }
    }

    companion object {
        fun newInstance(): RoutineFragment {
            return RoutineFragment()
        }
    }
}