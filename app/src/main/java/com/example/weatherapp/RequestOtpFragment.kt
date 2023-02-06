package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.FragmentRequestOtpBinding
import com.example.weatherapp.ui.MainActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class RequestOtpFragment : Fragment() {

    private var _binding: FragmentRequestOtpBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestOtpBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreen()
    }

    private fun initScreen() {
        binding.btnRequestOtp.setOnClickListener {
            val phone = binding.phone.text.toString()
            if (phone.length < 10) {
                Toast.makeText(requireContext(), "Invalid Phone", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            requestOTP(phone)
        }
    }

    private fun requestOTP(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Toast.makeText(requireContext(), "Phone number verified", Toast.LENGTH_SHORT)
                        .show()
                    MainActivity.start(requireContext())
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    p0.printStackTrace()
                    Toast.makeText(requireContext(), "Could not sent otp", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    p1: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, p1)
                    findNavController().navigate(
                        RequestOtpFragmentDirections.actionRequestOtpFragmentToVerifyOtpFragment2(
                            verificationId
                        )
                    )
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}