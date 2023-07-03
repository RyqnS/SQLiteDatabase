package com.example.sqlitedatabase


import android.content.Context
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBH (context:Context): SQLiteOpenHelper(context,dbName,null,dbVersion){

    companion object {
        private const val dbVersion  = 1
        private const val dbName = "user.db"
        private const val tableName = "tbl_user"
        private const val ID = "id"
        private const val Name = "name"
        private const val Email = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val myTable = ("CREATE TABLE " + tableName + " (" + ID + " INTEGER PRIMARY KEY," + Name + " TEXT," + Email + " TEXT" + ")")
        db?.execSQL(myTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    fun insertUser(user: User): Long {
        val db = this.writableDatabase

        val cv = ContentValues()
        cv.put(ID,user.id)
        cv.put(Name,user.name)
        cv.put(Email,user.email)

        val success = db.insert(tableName,null,cv)
        db.close()
        return success

    }

    fun getAllUsers():ArrayList<User>{
        val userList: ArrayList<User> = ArrayList()
        val query = "SELECT * FROM $tableName"
        val db = this.readableDatabase

        val cursor: Cursor?

        try{
            cursor = db.rawQuery(query,null)
        }catch(e:Exception){
            e.printStackTrace()
            db.execSQL(query)
            return ArrayList()
        }

        var id: Int
        var name: String
        var email: String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                email = cursor.getString(cursor.getColumnIndex("email"))
                val user = User(id,name,email)
                userList.add(user)

            }while(cursor.moveToNext())
        }

        return userList

    }

    fun updateUser(user:User): Int{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(ID,user.id)
        cv.put(Name,user.name)
        cv.put(Email,user.email)

        val success = db.update(tableName,cv,"id="+user.id,null)
        db.close()
        return success
    }

    fun deleteUserById(id:Int):Int{
        val db = this.writableDatabase

        val success = db.delete(tableName,"id=$id",null)
        db.close()
        return success

    }



}