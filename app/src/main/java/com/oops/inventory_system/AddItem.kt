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
            val height = Resources.getSystem().displayMetrics.heightPixels * 0.5 // Half screen height
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height.toInt())
        }
    }











    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val applyButton = view.findViewById<Button>(R.id.applyButton)
        val textFields = listOf(
            view.findViewById<EditText>(R.id.textField1),
            view.findViewById<EditText>(R.id.textField2),
            view.findViewById<EditText>(R.id.textField3),
            view.findViewById<EditText>(R.id.textField4),
            view.findViewById<EditText>(R.id.textField5)
            //view.findViewById<EditText>(R.id.textcat)
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

            if (inputs.size < 5 || inputs.any { it.isEmpty() }) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price = inputs[2].toIntOrNull() ?: -1
            val quantity = inputs[4].toIntOrNull() ?: -1

            if (price < 0 || quantity < 0) {
                Toast.makeText(requireContext(), "Invalid Price or Quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val itemData = hashMapOf(
                "trackId" to inputs[0],
                "category" to inputs[1],
                "price" to price,
                "location" to inputs[3],
                "quantity" to quantity
            )

            val firestoreManager = FirestoreManager()
            firestoreManager.addItem(
                itemData["trackId"]?.toString() ?: "",
                itemData["category"]?.toString() ?: "",
                itemData["price"] as Int,
                itemData["location"]?.toString() ?: "",
                itemData["quantity"] as Int
            ) { success, error ->
                if (success) {
                    Toast.makeText(requireContext(), "Item saved successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Error saving item: ${error?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }






    }
}






































