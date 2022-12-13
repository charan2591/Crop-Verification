package org.agripmu.cropverification.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import org.agripmu.cropverification.R
import org.agripmu.cropverification.models.SearchModel
import org.agripmu.cropverification.viewmodels.SearchViewModel
import java.util.*
import kotlin.collections.ArrayList

class RecyclerSearchViewAdapter(val viewModel: SearchViewModel, var arrayList: ArrayList<SearchModel>
                                , val context: Context
) : RecyclerView.Adapter<RecyclerSearchViewAdapter.SearchViewHolder>(), Filterable{

    var arrayListFiltered = ArrayList<SearchModel>()

    init {
        arrayListFiltered = arrayList
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(itemModel: SearchModel)
        {
            itemView.findViewById<TextView>(R.id.name).text = itemModel.farmerName
            itemView.findViewById<TextView>(R.id.quantity).text = itemModel.quantity
            itemView.findViewById<TextView>(R.id.location).text = itemModel.location
            itemView.findViewById<TextView>(R.id.time).text = itemModel.eventTime
            itemView.findViewById<TextView>(R.id.price).text = itemModel.price
//            itemView.findViewById<ImageButton>(R.id.delete).setOnClickListener{
////                viewModel.remove(itemModel)
////                notifyItemRemoved(arrayList.indexOf(itemModel))
////                val intent = Intent(context,BottomBarActivity::class.java)
////                context.startActivity(intent)
//
//            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val root = LayoutInflater.from(context).inflate(R.layout.recycler_search_item,parent,false)
        return SearchViewHolder(root)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(arrayListFiltered[position])
    }

    override fun getItemCount(): Int {

        if(arrayListFiltered.size==0){
            Toast.makeText(context,"List is empty", Toast.LENGTH_LONG).show()
        }
        return arrayListFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                val filterResults = FilterResults()
//                filterResults.values = countryFilterList

                if (constraint == null || constraint.length == 0) {
                    arrayListFiltered = arrayList
                } else {
                    val resultsModel: ArrayList<SearchModel> = ArrayList()
                    val searchStr = constraint.toString().lowercase()
                    for (itemsModel in arrayList)
                    {
                        if (itemsModel.farmerName.lowercase().contains(searchStr)
                        //                        || itemsModel.location
//                                .contains(searchStr)
                        ) {
                            resultsModel.add(itemsModel)
                        }

                    }
                    arrayListFiltered = resultsModel
                }

                filterResults.values = arrayListFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                arrayListFiltered = results?.values as ArrayList<SearchModel>
                notifyDataSetChanged()
            }
        }
        }
}