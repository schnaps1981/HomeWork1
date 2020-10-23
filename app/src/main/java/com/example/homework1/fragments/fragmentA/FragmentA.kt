package com.example.homework1.fragments.fragmentA

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.homework1.R
import com.example.homework1.utils.ContextResReader
import kotlinx.android.synthetic.main.fragment_a.*


class FragmentA : Fragment(R.layout.fragment_a) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        gotoFragmentB.setOnClickListener {
            navController.navigate(R.id.fragmentB)
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentA()
    }
}