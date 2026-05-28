package com.onecall.ui.setup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.onecall.R
import com.onecall.core.BluetoothDevicePreference
import com.onecall.core.BluetoothManager
import com.onecall.databinding.FragmentMainSetupBinding

class MainSetupFragment : Fragment() {

    private var _binding: FragmentMainSetupBinding? = null
    private val binding get() = _binding!!

    private lateinit var btManager: BluetoothManager
    private lateinit var devicePrefs: BluetoothDevicePreference
    private var countdownTimer: CountDownTimer? = null
    private var pulseAnimator: AnimatorSet? = null

    private val bondReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                val device = if (android.os.Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                }
                val bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1)
                if (bondState == BluetoothDevice.BOND_BONDED && device != null) {
                    val name = try { device.name ?: device.address } catch (e: SecurityException) { device.address }
                    devicePrefs.savePairedDeviceAddress(device.address, name)
                    Toast.makeText(requireContext(), "Paired with $name", Toast.LENGTH_SHORT).show()
                    showPairedDevice(name)
                    stopDiscoverable()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMainSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btManager = BluetoothManager(requireContext())
        devicePrefs = BluetoothDevicePreference(requireContext())

        requireActivity().registerReceiver(bondReceiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))

        updateBluetoothStatus()
        checkPairedDevice()

        binding.btnMakeDiscoverable.setOnClickListener {
            if (!btManager.isBluetoothEnabled()) {
                btManager.enableBluetooth()
            } else {
                startDiscoverable()
            }
        }

        binding.btnEnableBt.setOnClickListener {
            btManager.enableBluetooth()
        }

        binding.btnForgetDevice.setOnClickListener {
            val pairedAddress = devicePrefs.getPairedDeviceAddress()
            if (pairedAddress != null) {
                try {
                    val device = BluetoothAdapter.getDefaultAdapter()?.getRemoteDevice(pairedAddress)
                    device?.let { btManager.removeBond(it) }
                } catch (e: Exception) {}
            }
            devicePrefs.clearPairedDevice()
            binding.cardPairedDevice.visibility = View.GONE
        }

        binding.btnContinueDashboard.setOnClickListener {
            findNavController().navigate(R.id.action_main_setup_to_dashboard)
        }
    }

    private fun updateBluetoothStatus() {
        if (btManager.isBluetoothEnabled()) {
            binding.tvBtStatus.setText(R.string.bluetooth_on)
            binding.btnEnableBt.visibility = View.GONE
        } else {
            binding.tvBtStatus.setText(R.string.bluetooth_off)
            binding.btnEnableBt.visibility = View.VISIBLE
        }
    }

    private fun checkPairedDevice() {
        val name = devicePrefs.getPairedDeviceName()
        if (name != null) showPairedDevice(name)
    }

    private fun showPairedDevice(name: String) {
        binding.cardPairedDevice.visibility = View.VISIBLE
        binding.tvPairedDeviceName.text = name
    }

    private fun startDiscoverable() {
        btManager.makeDiscoverable(60)
        startCountdown(60)
        startPulseAnimation()
    }

    private fun stopDiscoverable() {
        countdownTimer?.cancel()
        stopPulseAnimation()
        binding.tvCountdown.visibility = View.GONE
    }

    private fun startCountdown(seconds: Int) {
        binding.tvCountdown.visibility = View.VISIBLE
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCountdown.text = getString(R.string.discoverable_countdown, (millisUntilFinished / 1000).toInt())
            }
            override fun onFinish() {
                binding.tvCountdown.visibility = View.GONE
                stopPulseAnimation()
            }
        }.start()
    }

    private fun startPulseAnimation() {
        val outer = binding.pulseRingOuter
        val inner = binding.pulseRingInner

        val outerAlpha = ObjectAnimator.ofFloat(outer, "alpha", 0f, 0.6f, 0f).apply {
            duration = 1500; repeatCount = ObjectAnimator.INFINITE; interpolator = DecelerateInterpolator()
        }
        val outerScale = ObjectAnimator.ofFloat(outer, "scaleX", 0.8f, 1.2f).apply {
            duration = 1500; repeatCount = ObjectAnimator.INFINITE
        }
        val outerScaleY = ObjectAnimator.ofFloat(outer, "scaleY", 0.8f, 1.2f).apply {
            duration = 1500; repeatCount = ObjectAnimator.INFINITE
        }
        val innerAlpha = ObjectAnimator.ofFloat(inner, "alpha", 0f, 0.4f, 0f).apply {
            duration = 1500; repeatCount = ObjectAnimator.INFINITE; startDelay = 750
        }

        pulseAnimator = AnimatorSet().apply {
            playTogether(outerAlpha, outerScale, outerScaleY, innerAlpha)
            start()
        }
    }

    private fun stopPulseAnimation() {
        pulseAnimator?.cancel()
        binding.pulseRingOuter.alpha = 0f
        binding.pulseRingInner.alpha = 0f
    }

    override fun onResume() {
        super.onResume()
        updateBluetoothStatus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try { requireActivity().unregisterReceiver(bondReceiver) } catch (e: Exception) {}
        countdownTimer?.cancel()
        stopPulseAnimation()
        _binding = null
    }
}
