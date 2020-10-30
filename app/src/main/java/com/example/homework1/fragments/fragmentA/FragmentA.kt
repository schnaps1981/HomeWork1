package com.example.homework1.fragments.fragmentA

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.homework1.R
import kotlinx.android.synthetic.main.fragment_a.*


class FragmentA : Fragment(R.layout.fragment_a) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        gotoFragmentB.setOnClickListener {
            navController.navigate(R.id.fragmentB)
        }

        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })

        btnIncreaseSpeed.setOnClickListener {
            speedMeter.onSpeedChanged(speedMeter.getSpeed() + 5)
        }

        btnDecreaseSpeed.setOnClickListener {
            speedMeter.onSpeedChanged(speedMeter.getSpeed() - 5)
        }

        btnSetSpeed.setOnClickListener {
            val oldSpeedValue = speedMeter.getSpeed()
            val newSpeedValue = etSetSpeed.text.toString().toIntOrNull()

            newSpeedValue?.let { speed ->
                val animator = ValueAnimator.ofInt(oldSpeedValue, speed)
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.duration = 3000L

                animator.addUpdateListener { animation ->
                    speedMeter.onSpeedChanged(animation.animatedValue as Int)
                }
                animator.start()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentA()
    }
}