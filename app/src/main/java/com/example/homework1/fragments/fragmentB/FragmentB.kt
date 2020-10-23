package com.example.homework1.fragments.fragmentB

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.homework1.R
import com.example.homework1.utils.ContextResReader
import com.example.homework1.workers.RandomNumberWorker
import kotlinx.android.synthetic.main.fragment_b.*

class FragmentB : Fragment(R.layout.fragment_b) {

    private lateinit var resReader: ContextResReader
    private lateinit var workManager: WorkManager

    private val randomWork = OneTimeWorkRequestBuilder<RandomNumberWorker>()
        .setConstraints(Constraints.NONE)
        .build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        resReader = ContextResReader(requireContext())
        workManager = WorkManager.getInstance(requireContext())

        gotoFragmentC.setOnClickListener {
            navController.navigate(R.id.fragmentC)
        }

        btnGetRandomNumber.setOnClickListener {
            workManager.enqueue(randomWork)
        }

        WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(randomWork.id)
            .observe(viewLifecycleOwner, Observer {
                it?.let {
                    if (it.state == WorkInfo.State.SUCCEEDED) {
                        val result =
                            it.outputData.getInt(RandomNumberWorker.WORKER_RESULT_RANDOM_NUMBER, 0)

                        tvNumberValue.text = resReader.getString(R.string.numberValue, result)
                    }
                }
            })
    }

    override fun onPause() {
        workManager.cancelWorkById(randomWork.id)
        super.onPause()
    }


    companion object {
        @JvmStatic
        fun newInstance() = FragmentB()
    }
}