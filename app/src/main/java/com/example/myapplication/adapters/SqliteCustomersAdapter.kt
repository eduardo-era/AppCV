package com.example.myapplication.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.pojos.Customers
import com.example.myapplication.views.SqliteActivity
import kotlinx.android.synthetic.main.sqlite_update_costumers.view.*
import kotlinx.android.synthetic.main.sqlite_update_customers_dialog.view.*
import java.util.ArrayList

class SqliteCustomersAdapter(val context: Context, val customers: ArrayList<Customers>): RecyclerView.Adapter<SqliteCustomersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.sqlite_update_costumers, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val customer = customers[position]
        holder.updateName.text = customer.customername
        holder.updateCredit.text = customer.maxcredit.toString()

        holder.updateButton.setOnClickListener {
            updateClient(customer, position)
        }

        holder.deleteBurron.setOnClickListener {
            deleteClient(customer, position)
        }
    }

    private fun deleteClient(customer:Customers, position: Int){
        AlertDialog.Builder(context).setTitle("Advertencia")
            .setMessage("Se eliminara el cliente").setPositiveButton("SI", DialogInterface.OnClickListener { dialog, i ->
                if(SqliteActivity.dbHandler.deleteCustomer(customer.customerid!!)){
                    customers.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, customers.size)
                    Toast.makeText(context, "Cliente eliminado", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            })
            .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, i -> })
            .setIcon(R.drawable.warning)
            .show()
    }

    private fun updateClient(customer: Customers, position: Int){
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sqlite_update_customers_dialog,null)
        val textname = view.findViewById<EditText>(R.id.sqlite_update_name_text)
        val txtCredit = view.findViewById<EditText>(R.id.sqlite_update_credit_text)

        textname.setText(customer.customername)
        txtCredit.setText(customer.customerid.toString())

        AlertDialog.Builder(context).setTitle("Actualizar Cliente")
            .setView(view)
            .setPositiveButton("ACTUALIZAR", DialogInterface.OnClickListener { dialog, i ->
                val isUpdate: Boolean = SqliteActivity.dbHandler.updateCustomer(customer.customerid.toString()
                    ,view.sqlite_update_name_text.text.toString(), view.sqlite_update_credit_text.text.toString())

                if(isUpdate){
                    customers[position].customername = view.sqlite_update_name_text.text.toString()
                    customers[position].maxcredit = view.sqlite_update_credit_text.text.toString().toDouble()
                    notifyDataSetChanged()
                    Toast.makeText(context, "Cliente Actualizado", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context, "Error Al Actualizar", Toast.LENGTH_SHORT).show()
                }
            }).setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialogInterface, i ->  })
            .show()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val updateName = itemView.sqlite_update_name
        val updateCredit = itemView.sqlite_update_credit
        val updateButton = itemView.sqlite_update_button
        val deleteBurron = itemView.sqlite_delete_button
    }
}