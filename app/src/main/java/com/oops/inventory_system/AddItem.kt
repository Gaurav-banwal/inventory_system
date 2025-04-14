package com.oops.inventory_system

import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddItem : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_additem, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val height = Resources.getSystem().displayMetrics.heightPixels * 0.5 // Half screen height
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height.toInt())
        }
    }









//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val applyButton = view.findViewById<Button>(R.id.applyButton)
//
//        applyButton.visibility = View.VISIBLE // Force visibility
//
//        applyButton.setOnClickListener {
//            val input1 = view.findViewById<EditText>(R.id.textField1).text.toString()
//            val input2 = view.findViewById<EditText>(R.id.textField2).text.toString()
//            Toast.makeText(requireContext(), "Entered: $input1, $input2", Toast.LENGTH_SHORT).show()
//        }
//    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val applyButton = view.findViewById<Button>(R.id.applyButton)
        val textFields = listOf(
            view.findViewById<EditText>(R.id.textField1),
            view.findViewById<EditText>(R.id.textField2),
            view.findViewById<EditText>(R.id.textField3),
            view.findViewById<EditText>(R.id.textField4),
            view.findViewById<EditText>(R.id.textField5)
        )

        applyButton.isEnabled = false // Disable initially

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                applyButton.isEnabled = textFields.all { it.text.isNotEmpty() }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        textFields.forEach { it.addTextChangedListener(textWatcher) }

        applyButton.setOnClickListener {
            val inputs = textFields.map { it.text.toString() }
            Toast.makeText(requireContext(), "Entered: ${inputs.joinToString(", ")}", Toast.LENGTH_SHORT).show()
        }
    }






}