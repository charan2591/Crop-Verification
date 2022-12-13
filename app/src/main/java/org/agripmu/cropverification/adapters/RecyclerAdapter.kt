package org.agripmu.cropverification.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomsheet.BottomSheetDialog
import org.agripmu.cropverification.models.MainModel
import org.agripmu.cropverification.viewmodels.MainViewModel
import org.agripmu.cropverification.R

class RecyclerAdapter(val viewModel: MainViewModel, val arrayList: ArrayList<MainModel>
                      , val context: Context) : RecyclerView.Adapter<RecyclerAdapter.MainViewHolder>(){

   inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemModel: MainModel)
        {
            itemView.findViewById<TextView>(R.id.title).text = itemModel.title
            itemView.findViewById<ImageButton>(R.id.delete).setOnClickListener{
//                viewModel.remove(itemModel)
//                notifyItemRemoved(arrayList.indexOf(itemModel))
//                val intent = Intent(context,BottomBarActivity::class.java)
//                context.startActivity(intent)

                showDialog(itemModel)

            }
        }

    }

    private fun showDialog(itemModel : MainModel)
    {
        // on below line we are creating a new bottom sheet dialog.
        val dialog = BottomSheetDialog(context)

        /* on below line we are inflating a layout file which we have created. */
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_del_dialog, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        val btnOk = view.findViewById<Button>(R.id.idBtnDelete)
        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)

        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnOk.setOnClickListener {
            // on below line we are calling a  Ok action and dismiss
            // method to close our dialog.
            viewModel.remove(itemModel)
            notifyItemRemoved(arrayList.indexOf(itemModel))
            dialog.dismiss()
        }
        btnClose.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            dialog.dismiss()
        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.
        dialog.setCancelable(false)

        // on below line we are setting
        // content view to our view.
        dialog.setContentView(view)

        // on below line we are calling
        // a show method to display a dialog.
        dialog.show()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val root = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false)
        return MainViewHolder(root)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
       holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {

        if(arrayList.size==0){
            Toast.makeText(context,"List is empty", Toast.LENGTH_LONG).show()
        }

       return arrayList.size
    }
}