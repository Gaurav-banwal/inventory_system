package com.oops.inventory_system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.firestore.FirebaseFirestore

class RemoveItem : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    private val firestoreManager = FirestoreManager()
    //1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_removeitem, container, false)
    }
//2 aspect ratio
    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT  // Full screen width
            val height = (resources.displayMetrics.heightPixels * 0.4).toInt() // 40% of screen height
            it.window?.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val removeButton = view.findViewById<Button>(R.id.removeButton)
        val trackIdField = view.findViewById<EditText>(R.id.trackIdField)
        val itemNameField = view.findViewById<EditText>(R.id.itemNameField)

        removeButton.isEnabled = false // disable button initially until the user tell the input

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
            //for track id
            if (trackId.isNotEmpty()) {
                // If Track ID is provided, use it to delete the item
                firestoreManager.deleteItem(trackId) { success, error ->
                    if (success) {
                        Toast.makeText(requireContext(), "Item with ID $trackId removed successfully", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Error removing item: ${error?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
                //for name
            } else if (itemName.isNotEmpty()) {
                // If only item name is provided, need to query for the item first
                db.collection("inventory")
                    .whereEqualTo("name", itemName)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            Toast.makeText(requireContext(), "No item found with name: $itemName", Toast.LENGTH_SHORT).show()
                        } else {
                            var deletedCount = 0
                            val totalToDelete = documents.size()//amount of same item wioth same document path
                            //delete all item with this name
                            for (document in documents) {
                                val docId = document.id
                                firestoreManager.deleteItem(docId) { success, error ->
                                    if (success) {
                                        deletedCount++
                                        if (deletedCount == totalToDelete) {
                                            Toast.makeText(requireContext(), "Removed $deletedCount items with name: $itemName", Toast.LENGTH_SHORT).show()
                                            dismiss()
                                        }
                                    } else {
                                        Toast.makeText(requireContext(), "Error removing some items: ${error?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Error searching for items: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}