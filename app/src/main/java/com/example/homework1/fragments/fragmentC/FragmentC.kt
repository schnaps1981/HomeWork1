package com.example.homework1.fragments.fragmentC

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.homework1.R
import kotlinx.android.synthetic.main.fragment_c.*

class FragmentC : Fragment(R.layout.fragment_c) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        gotoFragmentA.setOnClickListener {
            navController.navigate(R.id.fragmentA)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentC()
    }

}