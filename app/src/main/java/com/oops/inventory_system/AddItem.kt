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
import com.google.firebase.firestore.FirebaseFirestore

class AddItem : DialogFragment() {

//    val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_additem, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val height = Resources.getSystem().displayMetrics.heightPixels * 0.8 // Increased from 0.5 to 0.8
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height.toInt())
        }
    }











    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val applyButton = view.findViewById<Button>(R.id.applyButton)
        val textFields = listOf(
            view.findViewById<EditText>(R.id.textField1),
            view.findViewById<EditText>(R.id.textField2),
            view.findViewById<EditText>(R.id.textField6),
            view.findViewById<EditText>(R.id.textField3),
            view.findViewById<EditText>(R.id.textField4),
            view.findViewById<EditText>(R.id.textField5),
            view.findViewById<EditText>(R.id.textField7)
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

            if (inputs.size < 6 || inputs.any { it.isEmpty() }) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val trackId = inputs[0].toIntOrNull() ?: -1
            val name = inputs[1]
            val category = inputs[2]
            val quantity = inputs[3].toIntOrNull() ?: -1
            val location = inputs[4]
            val price = inputs[5].toIntOrNull() ?: -1

            if (price < 0 || quantity < 0 || trackId < 0) {
                Toast.makeText(requireContext(), "Invalid Track ID, Price or Quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val itemData = hashMapOf(
                "trackId" to trackId,
                "name" to name,
                "category" to category,
                "quantity" to quantity,
                "location" to location,
                "price" to price
            )

            val firestoreManager = FirestoreManager()
            firestoreManager.addItem(
                itemData["trackId"] as Int,
                itemData["name"]?.toString() ?: "",
                itemData["quantity"] as Int,
                itemData["location"]?.toString() ?: "",
                itemData["price"] as Int,
                itemData["category"]?.toString() ?: ""
            ) { success, error ->
                if (success) {
                    Toast.makeText(requireContext(), "Item saved successfully!", Toast.LENGTH_SHORT).show()
                    dismiss() // Close the dialog after successful save
                } else {
                    Toast.makeText(requireContext(), "Error saving item: ${error?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }






    }
}






































