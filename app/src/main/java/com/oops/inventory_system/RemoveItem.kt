package com.oops.inventory_system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment

class RemoveItem : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_removeitem, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT  // Full screen width
            val height = (resources.displayMetrics.heightPixels * 0.4).toInt() // 60% of screen height
            it.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val removeButton = view.findViewById<Button>(R.id.removeButton)
        val trackIdField = view.findViewById<EditText>(R.id.trackIdField)
        val itemNameField = view.findViewById<EditText>(R.id.itemNameField)

        removeButton.isEnabled = false // Disable button initially

        val textWatcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                removeButton.isEnabled =
                    trackIdField.text.isNotEmpty() || itemNameField.text.isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        trackIdField.addTextChangedListener(textWatcher)
        itemNameField.addTextChangedListener(textWatcher)

        removeButton.setOnClickListener {
            val trackId = trackIdField.text.toString()
            val itemName = itemNameField.text.toString()

            Toast.makeText(requireContext(), "Removed: $trackId / $itemName", Toast.LENGTH_SHORT).show()
            dismiss() // Close the dialog
        }

    }

}