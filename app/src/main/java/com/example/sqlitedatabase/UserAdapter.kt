package com.example.sqlitedatabase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserView>() {

    private var userList: ArrayList<User> = ArrayList()
    private var onClickItem: ((User) -> Unit)? = null
    private var onClickDeleteItem: ((User) -> Unit)? = null

    fun addItems(items: ArrayList<User>){
        this.userList = items
        notifyDataSetChanged()
    }
    class UserView(var view: View): RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var email = view.findViewById<TextView>(R.id.tvEmail)
        var btnDelete = view.findViewById<TextView>(R.id.btnDelete)


        fun bindView(user:User){
            id.text = user.id.toString()
            name.text =user.name
            email.text = user.email
        }
    }
    fun setOnClickItem(callback:(User)->Unit){
        this.onClickItem = callback
    }
    fun setOnClickDeleteItem(callback:(User)->Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserView = UserView(LayoutInflater.from(parent.context).inflate(R.layout.card_items_std,parent,false))

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserView, position: Int) {
        val user = userList[position]
        holder.bindView(user)
        holder.itemView.setOnClickListener {onClickItem?.invoke(user)}
        holder.btnDelete.setOnClickListener{onClickDeleteItem?.invoke(user)}
    }
}